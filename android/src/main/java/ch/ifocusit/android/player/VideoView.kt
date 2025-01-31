package ch.ifocusit.android.player

import android.content.Context
import android.view.ViewGroup
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class VideoView(context: Context) : ViewGroup(context) {
    private var player: VlcPlayer? = null
        init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
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

    fun setPlayer(player: VlcPlayer) {
        this.player = player
        addView(player)
    }


    fun emitEvent(eventName: String, params: Any? = null) {
        val event = Arguments.createMap().apply {
            if (params != null) {
                when (params) {
                    is Boolean -> putBoolean("value", params)
                    is Int -> putInt("value", params)
                    is Double -> putDouble("value", params)
                    is String -> putString("value", params)
                    else -> putString("value", params.toString())
                }
            }
        }

        val reactContext = context as ThemedReactContext
        reactContext
            .getJSModule(RCTEventEmitter::class.java)
            .receiveEvent(id, eventName, event)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        player?.release()
    }
}
