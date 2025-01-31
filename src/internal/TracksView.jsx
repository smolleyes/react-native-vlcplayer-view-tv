import { Ionicons } from '@expo/vector-icons';
import React, { useState } from 'react';
import { ScrollView, StyleSheet, Text, View } from 'react-native';
import { VideoPlayer } from '../Player.types';
import { Focussable } from './components/Focussable';

type TracksViewProps = {
  player: VideoPlayer;
  onClose: () => void;
};

export const TracksView = ({ player, onClose: close }: TracksViewProps) => {
  const [audioSelected, setAudioSelected] = useState(player.selectedAudioTrackId);
  const [textSelected, setTextSelected] = useState(player.selectedTextTrackId);

  return (
    <View style={styles.background}>
      <View style={styles.container}>
        <View style={styles.parts}>
          <View style={styles.part}>
            <Text style={styles.separation}>Audio</Text>
            <ScrollView contentContainerStyle={styles.tracks}>
              {player.audioTracks.map((track, index) => (
                <Focussable
                  key={index}
                  onPress={() => {
                    player.selectedAudioTrackId = track.id;
                    setAudioSelected(track.id);
                  }}
                  style={styles.track}
                >
                  <View style={styles.track}>
                    <View style={{ width: 24 }}>
                      {track.id === audioSelected && <Ionicons name="checkmark-sharp" size={24} color="white" />}
                    </View>
                    <Text key={'audio' + index} style={[{ color: 'white' }, track.id !== audioSelected ? { opacity: 0.5 } : {}]}>
                      {track.name}
                    </Text>
                  </View>
                </Focussable>
              ))}
            </ScrollView>
          </View>
          <View style={styles.part}>
            <Text style={styles.separation}>Sous-titre</Text>
            <ScrollView contentContainerStyle={styles.tracks}>
              <Focussable
                onPress={() => {
                  player.unselectTextTrack();
                  setTextSelected(null);
                }}
                style={styles.track}
              >
                <View style={styles.track}>
                  <View style={{ width: 24 }}>{!textSelected && <Ionicons name="checkmark-sharp" size={24} color="white" />}</View>
                  <Text style={[{ color: 'white' }, textSelected ? { opacity: 0.5 } : {}]}>Aucun</Text>
                </View>
              </Focussable>
              {player.textTracks.map((track, index) => (
                <Focussable
                  key={index}
                  onPress={() => {
                    player.selectedTextTrackId = track.id;
                    setTextSelected(track.id);
                  }}
                  style={styles.track}
                >
                  <View style={styles.track}>
                    <View style={{ width: 24 }}>
                      {track.id === textSelected && <Ionicons name="checkmark-sharp" size={24} color="white" />}
                    </View>
                    <Text key={'text' + index} style={[{ color: 'white' }, track.id !== textSelected ? { opacity: 0.5 } : {}]}>
                      {track.name}
                    </Text>
                  </View>
                </Focussable>
              ))}
            </ScrollView>
          </View>
        </View>
        <Focussable onPress={close} style={{ paddingBottom: 10, paddingRight: 20 }}>
          <Text style={{ color: 'white', paddingHorizontal: 30, paddingVertical: 20, paddingTop: 0 }}>Fermer</Text>
        </Focussable>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  background: {
    position: 'absolute',
    flex: 1,
    width: '100%',
    height: '100%',
    justifyContent: 'flex-end',
    alignItems: 'center'
  },
  container: {
    backgroundColor: 'rgba(0,0,0,0.7)',
    maxWidth: 500,
    minHeight: 212,
    justifyContent: 'flex-start',
    alignItems: 'flex-end'
  },
  parts: {
    padding: 20,
    flex: 1,
    width: '100%',
    height: '100%',
    flexDirection: 'row',
    justifyContent: 'flex-start',
    alignItems: 'center',
    gap: 20
  },
  part: {
    flex: 1,
    width: '100%',
    height: '100%',
    justifyContent: 'flex-start',
    gap: 15
  },
  tracks: {
    gap: 20
  },
  track: {
    flexDirection: 'row',
    alignItems: 'center',
    gap: 10,
    height: 20
  },
  separation: {
    color: 'white',
    paddingBottom: 5,
    borderBottomColor: 'white',
    borderBottomWidth: 2
  }
});
