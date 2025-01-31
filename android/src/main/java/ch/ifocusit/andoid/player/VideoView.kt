package ch.ifocusit.andoid.player

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ch.ifocusit.andoid.player.VideoPlayerModule.ProgressInfo
import ch.ifocusit.andoid.player.VideoPlayerModule.VideoInfo
import com.facebook.react.bridge.Arguments
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.facebook.react.views.view.ReactViewGroup
import org.videolan.libvlc.MediaPlayer.Event

@SuppressLint("ViewConstructor")
class VideoView(context: ThemedReactContext) : ReactViewGroup(context), DefaultLifecycleObserver {

    private val eventEmitter = context.getJSModule(RCTEventEmitter::class.java)

    var willEnterPiP: Boolean = false
    var isInFullscreen: Boolean = false
        private set

    private fun emitEvent(eventName: String, params: Any? = null) {
        val reactContext = context as ThemedReactContext
        val event = Arguments.createMap().apply {
            when (params) {
                is VideoInfo -> putMap("nativeEvent", videoInfoToWritableMap(params))
                is ProgressInfo -> putMap("nativeEvent", progressInfoToWritableMap(params))
                is Boolean -> putMap("nativeEvent", Arguments.createMap().apply {
                    putBoolean("payload", params)
                })
            }
        }
        eventEmitter.receiveEvent(id, eventName, event)
    }

    private var timeChanged = 0L
        set(value) {
            val seconds = value / 1000
            if (field != seconds && videoPlayer != null) {
                emitEvent("onProgress", ProgressInfo(value, videoPlayer!!.player.position))
            }
            field = seconds
        }

    var videoPlayer: VlcPlayer? = null
        set(value) {
            if (field != null) {
                field?.release()
            }
            removeAllViews()
            field = value
            if (field != null) {
                addView(
                    field!!.videoLayout,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                listenPlayerEvents(field!!)
                field!!.view = this
            }
        }

    init {
        VideoManager.registerVideoView(this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    private fun listenPlayerEvents(sharedObject: VlcPlayer) {
        val player = sharedObject.player
        player.setEventListener { event ->
            run {
                when (event.type) {
                    Event.Buffering -> {
                        if (sharedObject.sourceChanged()) {
                            val videoInfo = sharedObject.loadVideoInfo()
                            if (videoInfo != null) {
                                emitEvent("onLoaded", videoInfo)
                            } else {
                                emitEvent("onLoading")
                            }
                        }
                    }

                    Event.TimeChanged -> {
                        this.timeChanged = event.timeChanged
                    }

                    Event.Playing, Event.Paused, Event.Stopped -> {
                        val playing = player.isPlaying
                        this.keepScreenOn = playing
                        emitEvent("onPaused", !playing)
                    }

                    Event.EndReached -> {
                        emitEvent("onEnded")
                    }

                    Event.EncounteredError -> {
                        emitEvent("onError")
                    }
                }
            }
        }
    }

    private fun videoInfoToWritableMap(info: VideoInfo) = Arguments.createMap().apply {
        putMap("track", Arguments.createMap().apply {
            putString("id", info.track.id)
            putString("name", info.track.name)
        })
        putMap("videoSize", Arguments.createMap().apply {
            putInt("width", info.videoSize.width)
            putInt("height", info.videoSize.height)
        })
        putBoolean("seekable", info.seekable)
        putDouble("duration", info.duration.toDouble())
        putArray("audioTracks", Arguments.createArray().apply {
            info.audioTracks.forEach { track ->
                pushMap(Arguments.createMap().apply {
                    putString("id", track.id)
                    putString("name", track.name)
                })
            }
        })
        putArray("textTracks", Arguments.createArray().apply {
            info.textTracks.forEach { track ->
                pushMap(Arguments.createMap().apply {
                    putString("id", track.id)
                    putString("name", track.name)
                })
            }
        })
    }

    private fun progressInfoToWritableMap(info: ProgressInfo) = Arguments.createMap().apply {
        putDouble("time", info.time.toDouble())
        putDouble("position", info.position.toDouble())
    }

    override fun onDestroy(owner: LifecycleOwner) {
        videoPlayer?.release()
        super.onDestroy(owner)
    }
}
