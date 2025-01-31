package ch.ifocusit.andoid.player

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.widget.FrameLayout
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

/**
 * A custom Android view that sets up a VLC MediaPlayer with a VLCVideoLayout.
 * This version has been refactored to remove Expo references and instead use standard Android/Kotlin.
 *
 * @constructor Creates a new PlayerView with the given Context.
 */
@SuppressLint("ViewConstructor")
class PlayerView(context: Context) : FrameLayout(context) {

    private val libVLC: LibVLC = LibVLC(context)

    private val videoLayout = VLCVideoLayout(context).also {
        it.setBackgroundColor(Color.BLACK)
    }

    /**
     * The underlying VLC MediaPlayer instance, attached to our videoLayout.
     * We set default scaling to SURFACE_FIT_SCREEN.
     */
    internal val player: MediaPlayer = MediaPlayer(libVLC).also {
        it.attachViews(videoLayout, null, true, true)
        it.videoScale = MediaPlayer.ScaleType.SURFACE_FIT_SCREEN
        addView(videoLayout)
    }

    /**
     * A data class that sets the media URI (local or web).
     * Optionally, a time (in milliseconds) for initial seek.
     */
    data class VideoSource(
        val uri: String,
        val time: Long? = null
    )

    /**
     * The current video source. When changed, the player is stopped,
     * reinitialized with the new source, and playback starts automatically.
     */
    var source: VideoSource? = null
        set(value) {
            if (field == value) {
                return
            }
            // stop any existing playback
            field?.let { player.stop() }

            field = value
            if (field != null) {
                val media = createMedia(field!!.uri)
                player.media = media
                media.release()

                // Start playing
                player.play()

                // Seek if a time was provided
                if (field!!.time != null) {
                    player.time = field!!.time
                }
            }
        }

    /**
     * Helper function to create a Media object from a URI string.
     * Supports both http(s) URIs and file paths.
     */
    private fun createMedia(uri: String): Media {
        return if (uri.startsWith("http")) {
            Media(libVLC, Uri.parse(uri))
        } else {
            Media(libVLC, uri)
        }
    }
}
