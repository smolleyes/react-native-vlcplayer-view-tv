import React, { forwardRef } from 'react';
import { StyleSheet, View } from 'react-native';
import { Gesture, GestureDetector } from 'react-native-gesture-handler';
import Animated, {
  useAnimatedStyle,
  useSharedValue,
  withSpring
} from 'react-native-reanimated';

export const calculatePanChangeValue = (position, min, max, step, height) => {
  'worklet';
  const value = ((height - position) / height) * (max - min) + min;
  return Math.round(value / step) * step;
};

const VerticalSlider = forwardRef(
  (
    {
      width = 4,
      height = 200,
      borderRadius = 2,
      minimumValue = 0,
      maximumValue = 1,
      step = 0.1,
      minimumTrackTintColor = '#fff',
      maximumTrackTintColor = 'rgba(255, 255, 255, 0.7)',
      thumbTintColor = '#fff',
      thumbSize = 15,
      value = 0,
      onValueChange,
      onSlidingStart,
      onSlidingComplete
    },
    ref
  ) => {
    const position = useSharedValue(0);
    const isSliding = useSharedValue(false);

    const thumbAnimatedStyle = useAnimatedStyle(() => ({
      transform: [{ translateY: position.value }]
    }));

    const gesture = Gesture.Pan()
      .onBegin(() => {
        isSliding.value = true;
        onSlidingStart?.();
      })
      .onUpdate(e => {
        position.value = Math.max(0, Math.min(height - thumbSize, e.absoluteY));
        const newValue = calculatePanChangeValue(position.value, minimumValue, maximumValue, step, height - thumbSize);
        onValueChange?.(newValue);
      })
      .onEnd(() => {
        position.value = withSpring(
          height - thumbSize - ((value - minimumValue) / (maximumValue - minimumValue)) * (height - thumbSize)
        );
        isSliding.value = false;
        onSlidingComplete?.();
      });

    return (
      <GestureDetector gesture={gesture}>
        <View style={[styles.container, { width, height }]}>
          <View
            style={[
              styles.track,
              {
                width,
                height,
                borderRadius,
                backgroundColor: maximumTrackTintColor
              }
            ]}
          />
          <View
            style={[
              styles.minimumTrack,
              {
                width,
                borderRadius,
                backgroundColor: minimumTrackTintColor,
                height: ((value - minimumValue) / (maximumValue - minimumValue)) * height
              }
            ]}
          />
          <Animated.View
            style={[
              styles.thumb,
              {
                width: thumbSize,
                height: thumbSize,
                borderRadius: thumbSize / 2,
                backgroundColor: thumbTintColor,
                transform: [{ translateY: height - thumbSize }]
              },
              thumbAnimatedStyle
            ]}
          />
        </View>
      </GestureDetector>
    );
  }
);

const styles = StyleSheet.create({
  container: {
    justifyContent: 'flex-end'
  },
  track: {
    position: 'absolute',
    left: 0,
    bottom: 0
  },
  minimumTrack: {
    position: 'absolute',
    left: 0,
    bottom: 0
  },
  thumb: {
    position: 'absolute',
    left: -5.5
  }
});

export default VerticalSlider;
