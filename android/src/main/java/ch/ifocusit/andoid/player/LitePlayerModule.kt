package ch.ifocusit.andoid.player

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.bridge.ReadableMap

/**
 * A typical React Native ViewManager in Kotlin for the LitePlayerModule.
 * This replaces the previous Expo-based Module approach.
 *
 * Expects a corresponding JS component calling requireNativeComponent('LitePlayerModule').
 * The prop "source" is treated as a simple string or object in JavaScript, which you can parse below.
 * The "paused" prop is a boolean controlling playback state.
 */
class LitePlayerModule(private val reactContext: ReactApplicationContext) : SimpleViewManager<PlayerView>() {

  override fun getName(): String {
    // This name must match the string passed to requireNativeComponent('LitePlayerModule') in JS
    return "LitePlayerModule"
  }

  override fun createViewInstance(context: ThemedReactContext): PlayerView {
    // Construct our custom PlayerView (remove any references to Expo)
    return PlayerView(context)
  }

  /**
   * Each annotated @ReactProp maps to a prop in JS that can be passed down to this native view.
   * The property "source" can be a string or object in JS. We'll accept a ReadableMap for more advanced usage,
   * or a String if you want e.g. a direct URI. Shown here as a map for flexibility.
   */
  @ReactProp(name = "source")
  fun setSource(view: PlayerView, sourceMap: ReadableMap?) {
    // The original code used a custom VideoSource object. We can parse the map as needed.
    if (sourceMap != null && sourceMap.hasKey("uri")) {
      val uri = sourceMap.getString("uri")
      if (uri != null) {
        view.source = VideoSource(uri) // or however your PlayerView handles it
      }
    }
  }

  @ReactProp(name = "paused", defaultBoolean = false)
  fun setPaused(view: PlayerView, paused: Boolean) {
    val player = view.player
    if (player != null) {
      if (paused) {
        player.pause()
      } else {
        player.play()
      }
    }
  }
}
