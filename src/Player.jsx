import { createRef, PureComponent } from 'react';
import { requireNativeComponent } from 'react-native';
import { VideoPlayerModule } from './VideoPlayerModule';

export const RNPlayerView = requireNativeComponent('VideoPlayerModule');

export function useVideoPlayer(config, setup) {
  return new VideoPlayer(() => {
    const player = new VideoPlayerModule.VlcPlayer(config);
    setup?.(player);
    return player;
  }, [JSON.stringify(config)]);
}

export class PlayerView extends PureComponent {
  nativeRef = createRef();

  render() {
    const { player, ...props } = this.props;
    const playerId = getPlayerId(player);

    return <RNPlayerView player={playerId} ref={this.nativeRef} {...props} />;
  }
}

export function getPlayerId(player) {
  if (player instanceof VideoPlayerModule.VlcPlayer) {
    return player.id;
  }
  if (typeof player === 'number') {
    return player;
  }
  return null;
}
