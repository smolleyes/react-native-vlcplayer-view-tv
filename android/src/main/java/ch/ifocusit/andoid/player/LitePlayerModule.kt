package ch.ifocusit.andoid.player

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class LitePlayerModule(reactContext: ReactApplicationContext) : SimpleViewManager<PlayerView>() {
    override fun getName(): String = "LitePlayerModule"

    override fun createViewInstance(reactContext: ThemedReactContext): PlayerView {
        return PlayerView(reactContext)
    }

    @ReactProp(name = "source")
    fun setSource(view: PlayerView, source: VideoSource) {
        view.source = source
    }

    @ReactProp(name = "paused")
    fun setPaused(view: PlayerView, paused: Boolean) {
        if (paused) view.player.pause() else view.player.play()
    }
}
