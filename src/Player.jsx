// Converted from Player.tsx to plain JavaScript, removing all Expo references.
// This is a purely React Native version, relying on native modules for playback logic.

import React, { useState, useEffect, createRef, PureComponent } from 'react';
import { requireNativeComponent } from 'react-native';
import VideoPlayerModule from './VideoPlayerModule';

/**
 * The native view associated with VideoPlayerModule.
 */
const RNPlayerView = requireNativeComponent('VideoPlayerModule');

/**
 * A React hook that creates and manages a VLC video player instance.
 * Similar to the original useReleasingSharedObject logic, but replaced with a standard JS approach.
 *
 * @param {Object} config - Optional configuration object for initializing the player.
 * @param {Function} [setup] - Optional callback invoked with the new player immediately after creation.
 * @returns {Object|null} The current player instance.
 */
export function useVideoPlayer(config, setup) {
  const [player, setPlayer] = useState(null);

  useEffect(() => {
    const newPlayer = new VideoPlayerModule.VlcPlayer(config);
    if (typeof setup === 'function') {
      setup(newPlayer);
    }
    setPlayer(newPlayer);

    // Cleanup on unmount or config change
    return () => {
      if (newPlayer && typeof newPlayer.release === 'function') {
        newPlayer.release();
      }
    };
  }, [config, setup]);

  return player;
}

/**
 * A class-based React component for rendering the native VLC player view.
 * 
 * Props:
 * - player (number | object): The player instance or ID.
 * - Other props are spread onto the underlying native view.
 */
export class PlayerView extends PureComponent {
  constructor(props) {
    super(props);
    this.nativeRef = createRef();
  }

  render() {
    const { player, ...props } = this.props;
    const playerId = getPlayerId(player);

    return <RNPlayerView player={playerId} ref={this.nativeRef} {...props} />;
  }
}

/**
 * Helper to retrieve a unique player ID if the object uses some internal identifier like __expo_shared_object_id__.
 * This is a carryover from the original code. 
 * 
 * @param {number|Object} player - The player, which can be a number or an object instance.
 * @returns {number|null} The resolved ID or null if none is found.
 */
export function getPlayerId(player) {
  // If this is an object created by VideoPlayerModule.VlcPlayer, it might have the shared object ID.
  if (player && player.__expo_shared_object_id__) {
    return player.__expo_shared_object_id__;
  }
  if (typeof player === 'number') {
    return player;
  }
  return null;
}
