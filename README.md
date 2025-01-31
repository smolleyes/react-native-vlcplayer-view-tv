# react-native-vlc-media-player-view

[![NPM Version](https://img.shields.io/npm/v/react-native-vlc-media-player-view)](https://www.npmjs.com/package/react-native-vlc-media-player-view)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

React Native VLC media player view with full controls.

https://github.com/user-attachments/assets/4db406bc-2a8e-4344-b52a-a9480c092273

Use the latest libvlc library version!

```gradle
dependencies {
    implementation 'org.videolan.android:libvlc-all:4.0.0-eap15'
}
```

## Installation

```sh
npm install react-native-vlc-media-player-view
```

Take a look at the peer dependencies defined in the [package.json](https://github.com/jboz/react-native-vlc-media-player-view/blob/main/package.json).

## Example

```tsx
import { useVideoPlayer, VideoView } from 'react-native-vlc-media-player-view';

export default function App() {
  const player = useVideoPlayer({}, player => {
    player.source = { uri: 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4' };
    player.play();
  });

  return <VideoView player={player} />;
}
```

To who more: [example application](<[VideoView.types.ts](https://github.com/jboz/react-native-vlc-media-player-view/blob/main/example/App.tsx)>)

## Usage

Look at types definitions:

- [VideoView.types.ts](https://github.com/jboz/react-native-vlc-media-player-view/blob/main/src/VideoView.types.ts)
- [Player.types.ts](https://github.com/jboz/react-native-vlc-media-player-view/blob/main/src/Player.types.ts)

## Development

```shell
git clone https://github.com/jboz/react-native-vlc-media-player-view.git
cd example
npx expo run:android
```
