package ch.ifocusit.andoid.player

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule

class VideoPlayerModule(reactContext: ReactContext) : ReactContextBaseJavaModule(reactContext) {
    private val players = mutableMapOf<Int, VlcPlayer>()
    private var nextPlayerId = 1

    override fun getName() = "VideoPlayerModule"

    @ReactMethod
    fun createPlayer(source: ReadableMap, promise: Promise) {
        try {
            val uri = source.getString("uri") ?: throw IllegalArgumentException("uri is required")
            val time = if (source.hasKey("time")) source.getDouble("time").toLong() else 0L

            val videoSource = VideoSource(uri, time)
            val videoManager = VideoManager(reactApplicationContext)
            val player = videoManager.createPlayer(videoSource)

            val playerId = nextPlayerId++
            players[playerId] = player

            promise.resolve(playerId)
        } catch (e: Exception) {
            promise.reject("CREATE_PLAYER_ERROR", e)
        }
    }

    @ReactMethod
    fun releasePlayer(playerId: Int) {
        players.remove(playerId)?.release()
    }

    @ReactMethod
    fun setTime(playerId: Int, time: Double) {
        players[playerId]?.time = time.toLong()
    }

    @ReactMethod
    fun play(playerId: Int) {
        players[playerId]?.play()
    }

    @ReactMethod
    fun pause(playerId: Int) {
        players[playerId]?.pause()
    }

    @ReactMethod
    fun togglePlay(playerId: Int) {
        players[playerId]?.togglePlay()
    }

    @ReactMethod
    fun stop(playerId: Int) {
        players[playerId]?.stop()
    }

    @ReactMethod
    fun setVolume(playerId: Int, volume: Double) {
        players[playerId]?.volume = volume.toInt()
    }

    @ReactMethod
    fun setAudioDelay(playerId: Int, delay: Double) {
        players[playerId]?.audioDelay = delay.toLong()
    }

    @ReactMethod
    fun setSelectedAudioTrack(playerId: Int, trackId: String) {
        players[playerId]?.selectedAudioTrackId = trackId
    }

    @ReactMethod
    fun setSelectedTextTrack(playerId: Int, trackId: String) {
        players[playerId]?.selectedTextTrackId = trackId
    }

    @ReactMethod
    fun unselectTextTrack(playerId: Int) {
        players[playerId]?.unselectTextTrack()
    }

    @ReactMethod
    fun toggleFullscreen(playerId: Int) {
        val player = players[playerId] ?: return
        val videoManager = VideoManager(reactApplicationContext)
        videoManager.toggleFullscreen()
    }

    private fun sendEvent(eventName: String, params: WritableMap?) {
        reactApplicationContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit(eventName, params)
    }
}
