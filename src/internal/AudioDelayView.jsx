import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { StyleSheet, Text, View } from 'react-native';
import { Focussable } from './components/Focussable';
import { useTimeoutEffect } from './hooks/useTimeoutEffect';

export const AudioDelayView = ({ player, onClose }) => {
  const [delay, setDelay] = useTimeoutEffect(player.audioDelay, value => (player.audioDelay = value));

  return (
    <View style={styles.container}>
      <View style={styles.part}>
        <Text style={{ color: '#ff8c00', fontSize: 14, flex: 1, textAlign: 'center', fontWeight: 'bold' }}>Décalage audio</Text>
        <Focussable onPress={onClose}>
          <MaterialIcons name="close" size={30} color="white" style={{ paddingHorizontal: 10 }} />
        </Focussable>
      </View>
      <View style={[styles.part, { justifyContent: 'space-around', paddingVertical: 10 }]}>
        <Focussable onPress={() => setDelay(delay => delay - 50)}>
          <MaterialIcons name="arrow-back-ios-new" size={25} color="white" style={{ paddingHorizontal: 20 }} />
        </Focussable>
        <Text style={{ color: 'white', fontSize: 20 }}>{delay} ms</Text>
        <Focussable onPress={() => setDelay(delay => delay + 50)}>
          <MaterialIcons name="arrow-forward-ios" size={30} color="white" style={{ paddingHorizontal: 20 }} />
        </Focussable>
      </View>
      <View style={styles.part}>
        <Focussable onPress={() => setDelay(0)}>
          <Text
            style={{
              color: 'white',
              fontSize: 14,
              flex: 1,
              textAlign: 'right',
              fontWeight: 'bold',
              paddingHorizontal: 20,
              paddingVertical: 5
            }}
          >
            Réinitialiser
          </Text>
        </Focussable>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: 80,
    right: 0,
    flex: 1,
    justifyContent: 'center',
    width: 300,
    gap: 5
  },
  part: {
    backgroundColor: 'rgba(12,12,12,0.8)',
    padding: 2,
    width: '100%',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    borderRadius: 3
  }
});
