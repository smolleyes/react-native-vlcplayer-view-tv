import { Platform, TouchableOpacity, TouchableOpacityProps } from 'react-native';

export const Focussable = ({ onPress, style, children }: TouchableOpacityProps) => {
  return (
    <TouchableOpacity activeOpacity={1} style={[style, { opacity: Platform.isTV ? 0.2 : 1 }]} onPress={onPress}>
      {children}
    </TouchableOpacity>
  );
};
