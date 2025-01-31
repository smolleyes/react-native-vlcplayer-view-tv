import Brightness from 'react-native-brightness';
import { useState, useEffect } from 'react';

const useBrightness = () => {
  const [brightness, setBrightness] = useState(0);

  useEffect(() => {
    // Get initial brightness
    Brightness.getBrightness().then(setBrightness);
    
    // Note: react-native-brightness doesn't have a direct listener API
    // so we'll just handle manual changes
  }, []);

  useEffect(() => {
    Brightness.setBrightness(brightness);
  }, [brightness]);

  return [brightness, setBrightness];
};

export default useBrightness;
