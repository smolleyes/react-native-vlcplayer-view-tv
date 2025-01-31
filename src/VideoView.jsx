import React from 'react';
import { requireNativeComponent } from 'react-native';

const NativeVideoView = requireNativeComponent('VideoView');

export function VideoView(props) {
  return <NativeVideoView {...props} />;
}
