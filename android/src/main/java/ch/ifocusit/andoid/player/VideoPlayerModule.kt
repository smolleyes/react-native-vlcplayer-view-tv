package ch.ifocusit.andoid.player

import android.app.Activity
import android.content.pm.ActivityInfo
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.videolan.libvlc.interfaces.IMedia

@ReactModule(name = "VideoPlayerModule")
class VideoPlayerModule(private val reactContext: ReactApplicationContext) : SimpleViewManager<VideoView>() {

    private val scope = CoroutineScope(Dispatchers.Main)

    data class PlayerConfiguration(val initOptions: List<String>)

    data class VideoSource(
        val uri: String,
        val time: Long? = null,
        val options: List<String>
    )

    data class Dimensions(val width: Int, val height: Int)

    data class VideoInfo(
        val track: Track,
        val videoSize: Dimensions,
        val seekable: Boolean,
        val duration: Long,
        val audioTracks: List<Track>,
        val textTracks: List<Track>
    )

    data class ProgressInfo(val time: Long, val position: Float)

    data class Track(val id: String, val name: String)

    data class Position(val delta: Float, val fastSeeking: Boolean = false)

    data class Chapter(
        val timeOffset: Long, val duration: Long, val name: String
    )

    override fun getName(): String = "VideoPlayerModule"

    override fun createViewInstance(reactContext: ThemedReactContext): VideoView {
        return VideoView(reactContext)
    }

    @ReactProp(name = "player")
    fun setPlayer(view: VideoView, player: VlcPlayer) {
        view.videoPlayer = player
    }

    @ReactMethod
    fun lockOrientationLandscape(promise: Promise) {
        try {
            getCurrentActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERROR", e)
        }
    }

    @ReactMethod
    fun unlockOrientation(promise: Promise) {
        try {
            getCurrentActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            promise.resolve(true)
        } catch (e: Exception) {
            promise.reject("ERROR", e)
        }
    }

    private fun sendEvent(eventName: String, params: WritableMap?) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }

    // Convert data classes to WritableMap for React Native
    private fun VideoInfo.toWritableMap(): WritableMap {
        return Arguments.createMap().apply {
            putMap("track", track.toWritableMap())
            putMap("videoSize", Arguments.createMap().apply {
                putInt("width", videoSize.width)
                putInt("height", videoSize.height)
            })
            putBoolean("seekable", seekable)
            putDouble("duration", duration.toDouble())
            putArray("audioTracks", Arguments.createArray().apply {
                audioTracks.forEach { pushMap(it.toWritableMap()) }
            })
            putArray("textTracks", Arguments.createArray().apply {
                textTracks.forEach { pushMap(it.toWritableMap()) }
            })
        }
    }

    private fun Track.toWritableMap(): WritableMap {
        return Arguments.createMap().apply {
            putString("id", id)
            putString("name", name)
        }
    }

    private fun ProgressInfo.toWritableMap(): WritableMap {
        return Arguments.createMap().apply {
            putDouble("time", time.toDouble())
            putDouble("position", position.toDouble())
        }
    }

    override fun onDropViewInstance(view: VideoView) {
        super.onDropViewInstance(view)
        getCurrentActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        VideoManager.unregisterVideoView(view)
    }

    companion object {
        const val REACT_CLASS = "VideoPlayerModule"
    }
}
