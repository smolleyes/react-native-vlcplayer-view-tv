package ch.ifocusit.andoid.player

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.util.Log
import android.view.ViewGroup
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ch.ifocusit.andoid.player.VideoPlayerModule.ProgressInfo
import ch.ifocusit.andoid.player.VideoPlayerModule.VideoInfo
import expo.modules.core.interfaces.Arguments
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import org.videolan.libvlc.MediaPlayer.Event


@Suppress("SameParameterValue")
@SuppressLint("ViewConstructor")
class VideoView(context: Context, appContext: AppContext) : ExpoView(context, appContext),
    DefaultLifecycleObserver {

    var willEnterPiP: Boolean = false
    var isInFullscreen: Boolean = false
        private set

    private val onLoaded by EventDispatcher<VideoInfo>()
    private val onLoading by EventDispatcher<Unit>()
    internal val onProgress by EventDispatcher<ProgressInfo>()
    private val onPaused by EventDispatcher<Boolean>()
    private val onEnded by EventDispatcher<Unit>()
    private val onError by EventDispatcher<Unit>()
    internal val onAudioDelayChanged by EventDispatcher<Long>()
    internal val onTextDelayChanged by EventDispatcher<Long>()

    private var timeChanged = 0L
        set(value) {
            val seconds = value / 1000
            if (field != seconds && videoPlayer != null) {
                onProgress(ProgressInfo(value, videoPlayer!!.player.position))
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
                                onLoaded(videoInfo)
                            } else {
                                onLoading(Unit)
                            }
                        }
                    }

                    Event.TimeChanged -> {
                        this.timeChanged = event.timeChanged;
                    }

                    Event.Playing, Event.Paused, Event.Stopped -> {
                        val playing = player.isPlaying
                        this.keepScreenOn = playing
                        onPaused(!playing)
                    }

                    Event.EndReached -> {
                        onEnded(Unit)
                    }

                    Event.EncounteredError -> {
                        onError(Unit)
                    }
                }
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        videoPlayer?.release()
        super.onDestroy(owner)
    }
}
