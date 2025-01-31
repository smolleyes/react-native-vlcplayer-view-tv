package ch.ifocusit.andoid.player

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext

data class VideoSource(
    val uri: String,
    val time: Long = 0
)

class VideoManager(private val context: Context) {
    private var player: VlcPlayer? = null
    private var view: VideoView? = null
    private var fullscreen = false
    private var originalOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    private var originalParent: ViewGroup? = null
    private var originalLayoutParams: ViewGroup.LayoutParams? = null
    private var fullscreenContainer: FrameLayout? = null

    fun createPlayer(source: VideoSource): VlcPlayer {
        player = VlcPlayer(context, source)
        return player!!
    }

    fun setView(view: VideoView) {
        this.view = view
        player?.let { view.addView(it) }
    }

    fun toggleFullscreen() {
        val activity = (context as ReactContext).currentActivity ?: return
        if (fullscreen) {
            exitFullscreen(activity)
        } else {
            enterFullscreen(activity)
        }
    }

    private fun enterFullscreen(activity: Activity) {
        val decorView = activity.window.decorView as ViewGroup
        val view = view ?: return

        originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        originalParent = view.parent as? ViewGroup
        originalLayoutParams = view.layoutParams

        originalParent?.removeView(view)

        fullscreenContainer = FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            addView(view)
        }

        decorView.addView(fullscreenContainer)
        fullscreen = true
    }

    private fun exitFullscreen(activity: Activity) {
        val view = view ?: return
        fullscreenContainer?.removeView(view)
        fullscreenContainer?.parent?.let {
            (it as ViewGroup).removeView(fullscreenContainer)
        }

        originalParent?.addView(view, originalLayoutParams)
        activity.requestedOrientation = originalOrientation

        fullscreen = false
        fullscreenContainer = null
    }

    fun isFullscreen() = fullscreen

    fun release() {
        player?.release()
        player = null
        view = null
    }
}
