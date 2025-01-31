import { useState } from 'react';
import { Button, View } from 'react-native';
import { LitePlayerView } from 'react-native-vlcplayer-view-tv';

export default function App() {
  const [paused, setPaused] = useState(false);

  const uri = 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4';

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <LitePlayerView
        source={{ uri, time: 3 * 60 * 1000 }}
        style={{ width: '80%', height: '80%', backgroundColor: 'black' }}
        paused={paused}
      />
      <Button title="play/pause" onPress={() => setPaused(!paused)} />
    </View>
  );
}
