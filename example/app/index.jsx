import { MaterialIcons } from '@expo/vector-icons';
import React, { useEffect, useRef, useState } from 'react';
import { Pressable, StatusBar, StyleSheet, Text, TVEventHandler, View } from 'react-native';
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import { Chapter, useVideoPlayer, VideoPlayer, VideoSource, VideoView } from 'react-native-vlc-media-player-view';
import { VideoViewRef } from 'react-native-vlc-media-player-view/VideoView';

export default function App() {
  const [source, setSource] = useState<VideoSource>();

  const uri = 'http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4';

  const appStyles = useAppStyles();
  const buttons = useButtonStyles();

  useEffect(() => {
    StatusBar.setBackgroundColor('black');
    StatusBar.setTranslucent(true);
  }, []);

  return (
    <GestureHandlerRootView style={appStyles.container}>
      <View style={appStyles.content}>
        <Pressable
          onPress={() => setSource({ uri, time: 6000 })}
          style={({ focused }) => ({
            ...buttons.default,
            backgroundColor: focused ? '#FF79C1' : '#555'
          })}
        >
          <MaterialIcons name="play-arrow" size={20} color={'white'} />
          <Text style={buttons.text}>play</Text>
        </Pressable>
        <Pressable
          onPress={() => setSource(undefined)}
          style={({ focused }) => ({
            ...buttons.default,
            backgroundColor: focused ? '#FF79C1' : '#555'
          })}
        >
          <Text style={buttons.text}>close</Text>
          <MaterialIcons name="close" size={20} color={'white'} />
        </Pressable>
      </View>
      {source && <Player source={source} onBack={() => setSource(undefined)} />}
    </GestureHandlerRootView>
  );
}

type PlayerProps = {
  onBack: (player: VideoPlayer) => void;
  source: VideoSource;
};

const Player = ({ onBack, source }: PlayerProps) => {
  const videoViewRef = useRef<VideoViewRef>();

  const player = useVideoPlayer({ initOptions: ['--http-reconnect', '--codec=all', '--avcodec-hw=any'] }, player => {
    player.title = 'Big Buck Bunny';
  });

  player.play(source);

  const [showSkipIntro, setShowSkipIntro] = useState(false);
  const [intro, setIntro] = useState<Chapter>();

  useEffect(() => {
    TVEventHandler.addListener(event => {
      if (event.eventType === 'bottom' && !videoViewRef.current?.isControlBarVisible) {
        videoViewRef.current?.showControlBar(true);
      }
    });
  }, []);

  return (
    <>
      <VideoView
        ref={videoViewRef}
        player={player}
        style={{ flex: 1, width: '100%', backgroundColor: '#121212' }}
        onLoaded={() => {
          // source.time && (player.time = source.time);
          setIntro(player.chapters.find(c => c.name.match(/(opening)/i)));

          // set fullscreen programmatically
          // videoViewRef.current?.setFullscreen(true);

          // set time manually
          // player.time = 4 * 60 * 1000 + 49 * 1000;

          // change audio audio/subtitles tracks
          // player.selectedAudioTrackId = player.audioTracks[player.audioTracks.length - 1].id;
          // player.selectedTextTrackId = player.textTracks[player.textTracks.length - 1].id;
        }}
        alwaysFullscreen={false}
        onNext={() => console.log('next')}
        onPrevious={() => console.log('previous')}
        onBack={() => onBack(player)}
        onProgress={e => {
          const time = e.nativeEvent.time;
          const isInIntro = !!intro && time >= intro.timeOffset && time < intro.timeOffset + intro.duration - 1;
          setShowSkipIntro(isInIntro);
        }}
      />
      {intro && showSkipIntro && (
        <View style={{ position: 'absolute', bottom: 16, right: 16, zIndex: 1000 }}>
          <MaterialIcons.Button
            name="skip-next"
            size={20}
            onPress={() => {
              videoViewRef.current?.showControlBar(true);
              player.time = intro.timeOffset + intro.duration;
            }}
          >
            Skip Intro
          </MaterialIcons.Button>
        </View>
      )}
    </>
  );
};

const useAppStyles = function () {
  return StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'space-between',
      alignItems: 'center',
      width: '100%',
      height: '100%',
      gap: 20,
      backgroundColor: '#121212'
    },
    content: {
      flex: 0,
      width: '100%',
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      gap: 30,
      paddingTop: 50
    }
  });
};

const useButtonStyles = function () {
  return StyleSheet.create({
    default: {
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: '#44475A',
      borderRadius: 5,
      paddingVertical: 10,
      paddingHorizontal: 20,
      gap: 5
    },
    text: { color: 'white' },
    focused: { backgroundColor: '#FF79C5' }
  });
};
