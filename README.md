# react-native-vlcplayer-view-tv

A React Native VLC media player view with full controls, optimized for TV platforms.

## Installation

```bash
# Using yarn
yarn add https://github.com/smolleyes/react-native-vlcplayer-view-tv.git

# Using npm
npm install https://github.com/smolleyes/react-native-vlcplayer-view-tv.git
```

### Dependencies

This package requires the following peer dependencies:

```bash
yarn add @react-native-community/slider react-native-brightness react-native-fullscreen-chz react-native-gesture-handler react-native-linear-gradient react-native-reanimated react-native-vector-icons react-native-volume-manager
```

## Usage

```jsx
import { LitePlayerView } from 'react-native-vlcplayer-view-tv';

export default function App() {
  const [paused, setPaused] = useState(false);

  const uri = 'http://example.com/video.mp4';

  return (
    <View style={{ flex: 1 }}>
      <LitePlayerView
        source={{ uri, time: 0 }}
        style={{ width: '100%', height: '100%' }}
        paused={paused}
      />
    </View>
  );
}
```

### Advanced Usage with Full Controls

```jsx
import { VideoView, useVideoPlayer } from 'react-native-vlcplayer-view-tv';

export default function App() {
  const player = useVideoPlayer();

  return (
    <View style={{ flex: 1 }}>
      <VideoView
        player={player}
        style={{ width: '100%', height: '100%' }}
        source={{ uri: 'http://example.com/video.mp4' }}
      />
    </View>
  );
}
```

## Features

- Full video playback controls
- Audio track selection
- Subtitle support
- Brightness and volume controls
- Gesture controls for seeking and volume
- TV remote control support
- Fullscreen support

## Props

### LitePlayerView Props

| Prop | Type | Description |
|------|------|-------------|
| source | Object | Video source with uri and optional start time |
| style | ViewStyle | Container style |
| paused | boolean | Control video playback state |

### VideoView Props

| Prop | Type | Description |
|------|------|-------------|
| player | VideoPlayer | Player instance from useVideoPlayer hook |
| source | Object | Video source configuration |
| style | ViewStyle | Container style |
| onBack | Function | Callback when back button is pressed |
| onFullscreen | Function | Callback when fullscreen is toggled |

## License

MIT
