package ch.ifocusit.andoid.player

/**
 * This manager stores references to PlayerView instances and can pause them if the app is backgrounded.
 * Originally used for a more advanced useCase with Picture in Picture, fullscreen checks, etc. 
 * Now simplified for a standard React Native approach.
 */
object VideoManager {

    // Map each PlayerView by its unique view ID
    private val playerViews = mutableMapOf<Int, PlayerView>()

    /**
     * Register a PlayerView so we can keep track of it globally if needed.
     *
     * @param playerView The view to track.
     */
    fun registerPlayerView(playerView: PlayerView) {
        playerViews[playerView.id] = playerView
    }

    /**
     * Unregister a PlayerView. Stop and release its player if needed.
     *
     * @param playerView The view to stop tracking.
     */
    fun unregisterPlayerView(playerView: PlayerView) {
        playerView.player.stop()
        playerViews.remove(playerView.id)
    }

    /**
     * Invoked when the app is foregrounded (brought to front). 
     * Currently, no special behavior is implemented here.
     */
    fun onAppForegrounded() = Unit

    /**
     * Invoked when the app is backgrounded (sent to background).
     * Here, we simply pause all the players since there's no PiP or advanced 
     * background handling logic anymore.
     */
    fun onAppBackgrounded() {
        for (playerView in playerViews.values) {
            playerView.player.pause()
        }
    }
}
