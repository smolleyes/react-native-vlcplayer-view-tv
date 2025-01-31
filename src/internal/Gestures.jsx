import { StyleSheet, View } from 'react-native';
import { Gesture, GestureDetector } from 'react-native-gesture-handler';

export const calculatePanChangeDelta = (position, initialPosition, relativeHeight) => {
  'worklet';
  return (initialPosition - position) / relativeHeight;
};

export const ControlsGestures = ({
  onSingleTap,
  onDoubleTapCenter,
  onDoubleTapLeft,
  onDoubleTapRight,
  onVerticalSlideLeft,
  onVerticalSlideLeftEnd,
  onVerticalSlideRight,
  onVerticalSlideRightEnd
}) => {
  const singleTap = Gesture.Tap().onEnd(() => onSingleTap?.());

  const doubleTapCenter = Gesture.Tap()
    .numberOfTaps(2)
    .onEnd(() => onDoubleTapCenter?.());

  const doubleTapLeft = Gesture.Tap()
    .numberOfTaps(2)
    .onEnd(() => onDoubleTapLeft?.());

  const doubleTapRight = Gesture.Tap()
    .numberOfTaps(2)
    .onEnd(() => onDoubleTapRight?.());

  const verticalSlideLeft = Gesture.Pan()
    .onUpdate(e => {
      const delta = calculatePanChangeDelta(e.absoluteY, e.startY, 200);
      onVerticalSlideLeft?.(delta);
    })
    .onEnd(() => onVerticalSlideLeftEnd?.());

  const verticalSlideRight = Gesture.Pan()
    .onUpdate(e => {
      const delta = calculatePanChangeDelta(e.absoluteY, e.startY, 200);
      onVerticalSlideRight?.(delta);
    })
    .onEnd(() => onVerticalSlideRightEnd?.());

  const composed = Gesture.Exclusive(
    Gesture.Race(doubleTapCenter, doubleTapLeft, doubleTapRight, verticalSlideLeft, verticalSlideRight),
    singleTap
  );

  return (
    <GestureDetector gesture={composed}>
      <View style={styles.container}>
        <View style={styles.part} />
        <View style={styles.part} />
        <View style={styles.part} />
      </View>
    </GestureDetector>
  );
};

const styles = StyleSheet.create({
  container: {
    position: 'absolute',
    flex: 1,
    width: '100%',
    height: '100%',
    flexDirection: 'row'
  },
  part: {
    flex: 1
  }
});
