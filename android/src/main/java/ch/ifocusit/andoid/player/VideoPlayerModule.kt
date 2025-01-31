package ch.ifocusit.andoid.player

import android.app.Activity
import android.content.pm.ActivityInfo
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record
import kotlinx.coroutines.launch
import org.videolan.libvlc.interfaces.IMedia

class VideoPlayerModule : Module() {

    data class PlayerConfiguration(@Field val initOptions: List<String>) : Record

    data class VideoSource(
        @Field val uri: String,
        @Field val time: Long? = null,
        @Field val options: List<String>
    ) : Record

    data class Dimensions(@Field val width: Int, @Field val height: Int) : Record

    data class VideoInfo(
        @Field val track: Track,
        @Field val videoSize: Dimensions,
        @Field val seekable: Boolean,
        @Field val duration: Long,
        @Field val audioTracks: List<Track>,
        @Field val textTracks: List<Track>
    ) : Record

    data class ProgressInfo(@Field val time: Long, @Field val position: Float) : Record

    data class Track(@Field val id: String, @Field val name: String) : Record

    data class Position(@Field val delta: Float, @Field val fastSeeking: Boolean = false) : Record

    data class Chapter(
        @Field val timeOffset: Long, @Field val duration: Long, @Field val name: String
    ) : Record

    private val activity: Activity
        get() = appContext.activityProvider?.currentActivity ?: throw Exceptions.MissingActivity()

    override fun definition() = ModuleDefinition {
        Name("VideoPlayerModule")

        View(VideoView::class) {
            Events(
                "onLoaded",
                "onLoading",
                "onProgress",
                "onPaused",
                "onEnded",
                "onError",
                "onAudioDelayChanged",
                "onTextDelayChanged"
            )

            Prop("player") { view: VideoView, player: VlcPlayer ->
                view.videoPlayer = player
            }

            AsyncFunction("lockOrientationLandscape") {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                true
            }
            AsyncFunction("unlockOrientation") {
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                true
            }

            OnViewDestroys { view: VideoView ->
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                VideoManager.unregisterVideoView(view)
            }
        }

        Class(VlcPlayer::class) {
            Constructor { config: PlayerConfiguration? ->
                val player = VlcPlayer(activity.applicationContext, appContext, config)
                return@Constructor player
            }

            Property("source")
                .get { ref: VlcPlayer -> ref.source }
                .set { ref: VlcPlayer, source: VideoSource ->
                    appContext.mainQueue.launch {
                        ref.source = source
                    }
                }

            Property("isSeekable")
                .get { ref: VlcPlayer -> ref.player.isSeekable }
            Property("time")
                .get { ref: VlcPlayer -> ref.player.time }
                .set { ref: VlcPlayer, timeInMillis: Long ->
                    ref.setTime(timeInMillis)
                }
            Function("setTimeDelta") { ref: VlcPlayer, delta: Long ->
                appContext.mainQueue.launch { ref.setTimeDelta(delta) }
            }
            Property("position")
                .get { ref: VlcPlayer -> ref.player.position }
                .set { ref: VlcPlayer, positionPercentage: Float ->
                    appContext.mainQueue.launch { ref.setPosition(positionPercentage, false) }
                }
            Function("setPositionDelta") { ref: VlcPlayer, position: Position ->
                appContext.mainQueue.launch {
                    ref.setPositionDelta(position.delta, position.fastSeeking)
                }
            }

            Property("audioTracks")
                .get { ref: VlcPlayer ->
                    ref.player.getTracks(IMedia.Track.Type.Audio).orEmpty()
                        .map { Track(it.id, it.name) }
                }
            Property("selectedAudioTrackId")
                .get { ref: VlcPlayer ->
                    ref.player.getSelectedTrack(IMedia.Track.Type.Audio)?.id
                }
                .set { ref: VlcPlayer, id: String ->
                    appContext.mainQueue.launch {
                        ref.player.selectTracks(IMedia.Track.Type.Audio, id)
                    }
                }
            Function("unselectAudioTrack") { ref: VlcPlayer ->
                appContext.mainQueue.launch { ref.player.unselectTrackType(IMedia.Track.Type.Audio) }
            }

            Property("selectedTextTrackId")
                .get { ref: VlcPlayer ->
                    ref.player.getSelectedTrack(IMedia.Track.Type.Text)?.id
                }
                .set { ref: VlcPlayer, id: String ->
                    appContext.mainQueue.launch {
                        ref.player.selectTracks(IMedia.Track.Type.Text, id)
                    }
                }
            Property("textTracks")
                .get { ref: VlcPlayer ->
                    ref.player.getTracks(IMedia.Track.Type.Text).orEmpty()
                        .map { Track(it.id, it.name) }
                }
            Function("unselectTextTrack") { ref: VlcPlayer ->
                appContext.mainQueue.launch { ref.player.unselectTrackType(IMedia.Track.Type.Text) }
            }

            Property("isPlaying")
                .get { ref: VlcPlayer -> ref.player.isPlaying && ref.player.time != ref.player.length }
            Property("paused")
                .get { ref: VlcPlayer -> !ref.player.isPlaying }
                .set { ref: VlcPlayer, paused: Boolean ->
                    appContext.mainQueue.launch {
                        if (paused) ref.player.pause() else ref.player.play()
                    }
                }
            Function("play") { ref: VlcPlayer, source: VideoSource? ->
                appContext.mainQueue.launch { ref.play(source) }
            }
            Function("pause") { ref: VlcPlayer ->
                appContext.mainQueue.launch { ref.pause() }
            }
            Function("togglePlay") { ref: VlcPlayer ->
                appContext.mainQueue.launch {
                    if (ref.player.isPlaying) ref.pause() else ref.play()
                }
            }
            Function("stop") { ref: VlcPlayer ->
                appContext.mainQueue.launch { ref.stop() }
            }

            Function("release") { ref: VlcPlayer ->
                appContext.mainQueue.launch { ref.release() }
            }

            Property("audioDelay")
                .get { ref: VlcPlayer -> ref.player.audioDelay / 1000 }
                .set { ref: VlcPlayer, delayInMillis: Long ->
                    appContext.mainQueue.launch {
                        ref.setAudioDelay(delayInMillis)
                    }
                }
            Property("textDelay")
                .get { ref: VlcPlayer -> ref.player.spuDelay / 1000 }
                .set { ref: VlcPlayer, delayInMillis: Long ->
                    appContext.mainQueue.launch {
                        ref.setTextDelay(delayInMillis)
                    }
                }

            Property("isLoading")
                .get { ref: VlcPlayer -> ref.sourceChanged() }

            Property("videoInfo")
                .get { ref: VlcPlayer -> ref.videoInfo }

            Property("progressInfo")
                .get { ref: VlcPlayer -> ref.progressInfo() }

            Property("chapters").get { ref: VlcPlayer ->
                ref.player.getChapters(-1).orEmpty()
                    .map { Chapter(it.timeOffset, it.duration, it.name) }
            }
        }

        OnActivityEntersForeground {
            VideoManager.onAppForegrounded()
        }

        OnActivityEntersBackground {
            VideoManager.onAppBackgrounded()
        }
    }
}
