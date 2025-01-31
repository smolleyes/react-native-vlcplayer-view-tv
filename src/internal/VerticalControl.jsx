import { StyleSheet, Text, View } from 'react-native';
import VerticalSlider from './components/VerticalSlider';

export const VerticalControl = ({ value, title, align, icon }) => {
  return (
    <View style={[styles.container, align === 'right' ? { right: 20 } : { left: 20 }]}>
      <View style={styles.content}>
        {icon}
        <Text style={styles.title}>{title}</Text>
        <Text style={styles.value}>{Math.round(value * 100)}%</Text>
      </View>
      <VerticalSlider
        value={value}
        minimumValue={0}
        maximumValue={1}
        step={0.01}
        minimumTrackTintColor="#ff8c00"
        maximumTrackTintColor="lightgray"
        thumbTintColor="#ff8c00"
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    top: 80,
    backgroundColor: 'rgba(12,12,12,0.8)',
    padding: 10,
    borderRadius: 3,
    flexDirection: 'row',
    gap: 10,
    alignItems: 'center'
  },
  content: {
    alignItems: 'center',
    gap: 10
  },
  title: {
    color: 'white',
    fontSize: 12,
    fontWeight: 'bold'
  },
  value: {
    color: 'white',
    fontSize: 12
  }
});
