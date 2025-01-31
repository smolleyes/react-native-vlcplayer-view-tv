import { useEffect, useState } from 'react';
import VolumeManager from 'react-native-volume-manager';

const useVolume = () => {
  const [volume, setVolume] = useState(0);

  useEffect(() => {
    VolumeManager.getVolume().then(setVolume);
    VolumeManager.addVolumeListener(event => setVolume(event.volume));
  }, []);

  useEffect(() => {
    VolumeManager.setVolume(volume);
  }, [volume]);

  return [volume, setVolume];
};

export default useVolume;
