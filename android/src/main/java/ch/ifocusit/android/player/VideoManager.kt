package ch.ifocusit.android.player

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.ViewGroup
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class VideoManager constructor(private val reactContext: ReactContext) : SimpleViewManager<VideoLayout>() {
  fun createPlayer(source: VideoSource): VlcPlayer {
    return VlcPlayer(reactContext, source)
  }

  fun toggleFullscreen() {
    // no-op for now
  }

  override fun getName(): String = "VideoView"

override fun createViewInstance(themedReactContext: ThemedReactContext): VideoLayout {
    return VideoLayout(themedReactContext)
}

  @ReactProp(name = "fullscreen", defaultBoolean = false)
  fun setFullscreen(videoLayout: VideoLayout, fullscreen: Boolean) {
    videoLayout.post {
      videoLayout.toggleFullscreen(fullscreen)
    }
  }
}

/**
 * A custom VideoView that integrates with VlcPlayer.
 */
class VideoLayout constructor(private val reactContext: ReactContext) : FrameLayout(reactContext) {
  private var player: VlcPlayer? = null
  private var isFullscreen = false

  init {
    layoutParams = LayoutParams(
      LayoutParams.MATCH_PARENT,
      LayoutParams.MATCH_PARENT
    )
    player = VlcPlayer(context, VideoSource(uri = "")) // blank source initially
    addView(player)
  }

  fun toggleFullscreen(fullscreen: Boolean) {
    val activity = reactContext.currentActivity ?: return
    if (fullscreen == isFullscreen) return

    if (fullscreen) {
      enterFullscreen(activity)
    } else {
      exitFullscreen(activity)
    }
  }

  private fun enterFullscreen(activity: Activity) {
    val originalOrientation = activity.requestedOrientation
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    (parent as? ViewGroup)?.removeView(this)

    val decorView = activity.window.decorView as ViewGroup
    val container = FrameLayout(context).apply {
      layoutParams = LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.MATCH_PARENT
      )
      addView(this)
    }
    decorView.addView(container)
    isFullscreen = true
  }

  private fun exitFullscreen(activity: Activity) {
    val decorView = activity.window.decorView as ViewGroup
    val container = parent as? FrameLayout ?: return

    container.removeView(this)
    decorView.removeView(container)

    // Switch orientation back if needed
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

    isFullscreen = false
  }

  fun release() {
    player?.release()
    player = null
  }
}

/**
 * Represents a VLC video source.
 */
data class VideoSource(
  val uri: String,
  val time: Long = 0
)
