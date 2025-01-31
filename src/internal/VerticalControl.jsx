import { ReactNode } from 'react';
import { StyleSheet } from 'react-native';
import LinearGradient from 'react-native-linear-gradient';
import VerticalSlider from './components/VerticalSlider';

type VerticalControlProps = {
  value: number;
  title: string;
  align: 'left' | 'right';
  icon?: ReactNode;
};

export const VerticalControl = ({ value, title, align, icon }: VerticalControlProps) => {
  return (
    <LinearGradient
      colors={align === 'right' ? ['transparent', 'rgba(18, 18, 18, 0.8)'] : ['rgba(18, 18, 18, 0.8)', 'transparent']}
      start={align === 'right' ? { x: 0.6, y: 0 } : { x: 0, y: 0 }}
      end={align === 'right' ? { x: 1, y: 0 } : { x: 0.4, y: 0 }}
      style={[styles.container, { alignItems: align === 'right' ? 'flex-end' : 'flex-start' }]}
    >
      <VerticalSlider
        containerStyle={{ height: '70%', maxHeight: 300 }}
        value={value}
        label={title}
        labelPosition={align === 'right' ? 'left' : 'right'}
        icon={icon}
      />
    </LinearGradient>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    flex: 1,
    width: '100%',
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center'
  }
});
