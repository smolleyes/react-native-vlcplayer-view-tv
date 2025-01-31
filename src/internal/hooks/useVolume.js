import { Dispatch, SetStateAction, useEffect, useState } from 'react';
import { VolumeManager } from 'react-native-volume-manager';

const useVolume = (): [number, Dispatch<SetStateAction<number>>] => {
  const [volume, setVolume] = useState(1);

  useEffect(() => {
    VolumeManager.getVolume()
      .then(event => setVolume(event.volume))
      .then(() => VolumeManager.addVolumeListener(event => setVolume(event.volume)));
  }, []);

  useEffect(() => {
    volume !== undefined && VolumeManager.setVolume(volume);
  }, [volume]);

  return [volume, setVolume];
};

export default useVolume;
