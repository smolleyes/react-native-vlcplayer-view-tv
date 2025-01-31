package ch.ifocusit.android.player

import android.content.Context
import android.view.SurfaceView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.interfaces.IMedia
import org.videolan.libvlc.interfaces.IVLCVout

class VlcPlayer(context: Context, private val source: VideoSource) : SurfaceView(context), IVLCVout.Callback {
    private val libVlc: LibVLC
    private val mediaPlayer: MediaPlayer
    private var currentTime: Long = source.time
    private var currentVolume = 100
    private var currentAudioDelay: Long = 0

    init {
        val options = arrayOf(
            "--no-drop-late-frames",
            "--no-skip-frames",
            "--rtsp-tcp",
            "-vvv"
        )

        libVlc = LibVLC(context, ArrayList<String>().apply { addAll(options) })
        mediaPlayer = MediaPlayer(libVlc)

        holder.setKeepScreenOn(true)
        mediaPlayer.vlcVout.setVideoView(this)
        mediaPlayer.vlcVout.addCallback(this)
        mediaPlayer.vlcVout.attachViews()

        val media = Media(libVlc, source.uri)
        media.setHWDecoderEnabled(true, false)
        media.addOption(":network-caching=1000")

        mediaPlayer.media = media
        media.release()

        mediaPlayer.setEventListener { event ->
            when (event.type) {
                MediaPlayer.Event.Playing -> notifyPlaying()
                MediaPlayer.Event.Paused -> notifyPaused()
                MediaPlayer.Event.EndReached -> notifyEnded()
                MediaPlayer.Event.EncounteredError -> notifyError()
                MediaPlayer.Event.TimeChanged -> notifyProgress()
                MediaPlayer.Event.MediaChanged -> notifyLoaded()
            }
        }

        if (currentTime > 0) {
            mediaPlayer.time = currentTime
        }
    }

    private fun notifyPlaying() {
        (parent as? PlayerView)?.emitEvent("playing")
    }

    private fun notifyPaused() {
        (parent as? PlayerView)?.emitEvent("paused")
    }

    private fun notifyEnded() {
        (parent as? PlayerView)?.emitEvent("ended")
    }

    private fun notifyError() {
        (parent as? PlayerView)?.emitEvent("error")
    }

    private fun notifyProgress() {
        currentTime = mediaPlayer.time
        (parent as? PlayerView)?.emitEvent("progress", mapOf(
            "time" to currentTime,
            "position" to mediaPlayer.position
        ))
    }

    private fun notifyLoaded() {
        (parent as? PlayerView)?.emitEvent("loaded", mapOf(
            "duration" to mediaPlayer.length,
            "audioTracks" to mediaPlayer.audioTracks?.map { track ->
                mapOf(
                    "id" to track.id,
                    "name" to track.name
                )
            },
            "textTracks" to mediaPlayer.spuTracks?.map { track ->
                mapOf(
                    "id" to track.id,
                    "name" to track.name
                )
            }
        ))
    }

    fun play() {
        mediaPlayer.play()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun togglePlay() {
        if (mediaPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun setTime(time: Long) {
        currentTime = time
        mediaPlayer.time = time
    }

    fun setVolume(volume: Int) {
        currentVolume = volume
        mediaPlayer.volume = volume
    }

    fun setAudioDelay(delay: Long) {
        currentAudioDelay = delay
        mediaPlayer.audioDelay = delay * 1000 // Convert to microseconds
    }

    fun release() {
        mediaPlayer.vlcVout.detachViews()
        mediaPlayer.vlcVout.removeCallback(this)
        mediaPlayer.release()
        libVlc.release()
    }

    override fun onSurfacesCreated(vlcVout: IVLCVout) {}

    override fun onSurfacesDestroyed(vlcVout: IVLCVout) {}

    var selectedAudioTrackId: String = ""
        set(value) {
            field = value
            mediaPlayer.audioTracks?.find { it.id.toString() == value }?.let {
                mediaPlayer.audioTrack = it.id
            }
        }

    var selectedTextTrackId: String = ""
        set(value) {
            field = value
            mediaPlayer.spuTracks?.find { it.id.toString() == value }?.let {
                mediaPlayer.spuTrack = it.id
            }
        }

    fun unselectTextTrack() {
        mediaPlayer.spuTrack = -1
    }
}
