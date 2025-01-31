package ch.ifocusit.andoid.player

import android.content.Context
import android.view.ViewGroup
import com.facebook.react.uimanager.ThemedReactContext

class PlayerView(context: Context) : ViewGroup(context) {
    private var player: VlcPlayer? = null
    private var videoManager: VideoManager? = null
    private var paused = false

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        videoManager = VideoManager(context)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l
        val height = b - t

        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.layout(0, 0, width, height)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)

        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        }
    }

    fun setSource(source: VideoSource) {
        player?.release()
        player = videoManager?.createPlayer(source)
        player?.let { addView(it) }
    }

    fun setPaused(paused: Boolean) {
        this.paused = paused
        if (paused) {
            player?.pause()
        } else {
            player?.play()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        player?.release()
        player = null
        videoManager = null
    }
}
