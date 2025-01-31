import { useEffect } from 'react';
import { BackHandler } from 'react-native';

export default function useBackHandler(handler) {
  useEffect(() => {
    const subscription = BackHandler.addEventListener('hardwareBackPress', () => {
      const result = handler();
      return result;
    });

    return () => subscription.remove();
  }, [handler]);
}
