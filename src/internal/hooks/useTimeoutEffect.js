import { useEffect, useState } from 'react';

export const useTimeoutEffect = (initialValue, callback, timeout = 500) => {
  const [value, setValue] = useState(initialValue);
  const [timeoutId, setTimeoutId] = useState(null);

  useEffect(() => {
    if (timeoutId) {
      clearTimeout(timeoutId);
    }
    const id = setTimeout(() => callback(value), timeout);
    setTimeoutId(id);

    return () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }
    };
  }, [value]);

  return [value, setValue];
};
