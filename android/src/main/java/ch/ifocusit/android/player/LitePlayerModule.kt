package ch.ifocusit.android.player

import android.view.View
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class LitePlayerModule : SimpleViewManager<PlayerView>() {
    override fun getName() = "LitePlayerModule"

    override fun createViewInstance(context: ThemedReactContext): PlayerView {
        return PlayerView(context)
    }

    @ReactProp(name = "source")
    fun setSource(view: PlayerView, source: ReadableMap?) {
        if (source == null) return
        val uri = source.getString("uri") ?: return
        val time = if (source.hasKey("time")) source.getDouble("time").toLong() else 0L
        view.setSource(VideoSource(uri, time))
    }

    @ReactProp(name = "paused")
    fun setPaused(view: PlayerView, paused: Boolean) {
        view.setPaused(paused)
    }
}
