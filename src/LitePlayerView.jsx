import React from 'react';
import { requireNativeComponent } from 'react-native';

/**
 * A simple LitePlayerView component that uses a native component named "LitePlayerModule".
 * Original TypeScript and expo-modules-core references removed.
 * 
 * @param {Object} props 
 * @param {Object} props.style - The style object for the view.
 * @param {Object} props.source - The video source object (e.g. { uri: string }).
 * @param {boolean} props.paused - Whether the player is paused.
 */
function LitePlayerView(props) {
  return <NativeLitePlayer {...props} />;
}

const NativeLitePlayer = requireNativeComponent('LitePlayerModule');

export default LitePlayerView;
