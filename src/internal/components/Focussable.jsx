import { TouchableOpacity } from 'react-native';

export const Focussable = ({ onPress, style, children }) => {
  return (
    <TouchableOpacity
      onPress={onPress}
      style={style}
      activeOpacity={0.5}
      hitSlop={{ top: 10, bottom: 10, left: 10, right: 10 }}
    >
      {children}
    </TouchableOpacity>
  );
};
