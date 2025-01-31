import { requireNativeModule } from 'expo-modules-core';

import { VideoPlayer } from './Player.types';

type VideoPlayerModuleProps = {
  VlcPlayer: typeof VideoPlayer;
};

export const VideoPlayerModule = requireNativeModule<VideoPlayerModuleProps>('VideoPlayerModule');
