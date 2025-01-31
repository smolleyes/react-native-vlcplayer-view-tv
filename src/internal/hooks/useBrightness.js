import * as Brightness from 'expo-brightness';
import { Dispatch, SetStateAction, useEffect, useState } from 'react';

const useBrightness = (): [number, Dispatch<SetStateAction<number>>] => {
  const [brightness, setBrightness] = useState(0);

  useEffect(() => {
    Brightness.getBrightnessAsync()
      .then(setBrightness)
      .then(() => Brightness.addBrightnessListener(event => setBrightness(event.brightness)));
  }, []);

  useEffect(() => {
    Brightness.setBrightnessAsync(brightness);
  }, [brightness]);

  return [brightness, setBrightness];
};

export default useBrightness;
