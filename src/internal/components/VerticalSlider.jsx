import React, { ReactNode } from 'react';
import { LayoutChangeEvent, StyleProp, StyleSheet, Text, View, ViewStyle } from 'react-native';
import {
  Gesture,
  GestureDetector,
  GestureStateChangeEvent,
  GestureUpdateEvent,
  PanGestureChangeEventPayload,
  PanGestureHandlerEventPayload
} from 'react-native-gesture-handler';
import Animated, { runOnJS, useAnimatedStyle, useSharedValue } from 'react-native-reanimated';

export type TSliderProps = {
  width?: number;
  height?: number;
  onChange?: (value: number) => void;
  value?: number;
  containerStyle?: StyleProp<ViewStyle>;
  label?: string;
  labelPosition?: 'right' | 'left';
  icon?: ReactNode;
};

export type TSliderRef = {};

export const calculatePanChangeValue = (position: number, min: number, max: number, step: number, height: number): number => {
  'worklet';
  const sliderPosition = Math.min(Math.max(height - position, 0), height);
  let value = (sliderPosition / height) * (max - min) + min;
  value = Math.round(value / step) * step;
  value = Math.min(Math.max(value, min), max);
  return value;
};

const VerticalSlider = React.forwardRef<TSliderRef, TSliderProps>(
  ({ onChange = () => {}, value: currentValue = 0, containerStyle = {}, label, labelPosition = 'right', icon }, ref) => {
    let barHeight = useSharedValue<number>(0);

    const min = 0;
    const max = 1;
    const step = 0.1;

    // Gesture handler
    const handleGesture = (type: 'BEGIN' | 'CHANGE' | 'END') => (eventY: number) => {
      let value = calculatePanChangeValue(eventY, min, max, step, barHeight.value);
      // point.value = withSpring(value, animationConfig);
      (type === 'BEGIN' || type === 'CHANGE') && runOnJS(onChange)(value);
    };
    const onGestureStart = (event: GestureStateChangeEvent<PanGestureHandlerEventPayload>) => handleGesture('BEGIN')(event.y);
    const onGestureChange = (event: GestureUpdateEvent<PanGestureHandlerEventPayload & PanGestureChangeEventPayload>) =>
      handleGesture('CHANGE')(event.y);
    const onGestureEnd = (event: GestureStateChangeEvent<PanGestureHandlerEventPayload>) => handleGesture('END')(event.y);

    const panGesture = Gesture.Pan()
      .onBegin(onGestureStart)
      .onChange(onGestureChange)
      .onEnd(onGestureEnd)
      .onFinalize(onGestureEnd)
      .runOnJS(true);

    // Ref methods
    React.useImperativeHandle(ref, () => ({}));

    const slider = useAnimatedStyle(() => {
      const heightPercentage = ((currentValue - min) / (max - min)) * 100;
      const style: ViewStyle = {
        backgroundColor: 'white',
        height: `${heightPercentage}%`
      };
      return style;
    }, [currentValue]);

    const onBarLayout = (event: LayoutChangeEvent) => (barHeight.value = event.nativeEvent.layout.height);

    const percent = () => Math.floor(((currentValue - min) / (max - min)) * 100);

    return (
      <View style={[styles.container, containerStyle]}>
        {label && labelPosition === 'left' && <Text style={styles.label}>{label}</Text>}
        <GestureDetector gesture={panGesture}>
          <View style={styles.barContainer}>
            <Text style={styles.indicator}>{percent()} %</Text>
            <View style={styles.bar} onLayout={onBarLayout}>
              <View style={styles.box}>
                <Animated.View style={[styles.box, slider]} />
              </View>
            </View>
            {icon}
          </View>
        </GestureDetector>
        {label && labelPosition === 'right' && <Text style={styles.label}>{label}</Text>}
      </View>
    );
  }
);

const styles = StyleSheet.create({
  container: {
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    flexDirection: 'row'
  },
  barContainer: {
    height: '100%',
    justifyContent: 'center',
    alignItems: 'center',
    width: 50,
    gap: 20
  },
  bar: {
    flex: 1,
    width: 5,
    height: '100%',
    backgroundColor: '#808080',
    borderRadius: 50
  },
  box: {
    overflow: 'hidden',
    position: 'absolute',
    bottom: 0,
    width: '100%',
    height: '100%',
    borderRadius: 50
  },
  indicator: {
    fontSize: 18,
    color: '#FFFFFF'
  },
  label: {
    fontSize: 24,
    color: '#FFFFFF'
  }
});

export default VerticalSlider;
