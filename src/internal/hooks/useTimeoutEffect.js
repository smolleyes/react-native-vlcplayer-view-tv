import { useEffect, useState } from 'react';

type Dispatch<A> = (value: A, propagate?: boolean) => void;
type SetStateAction<S> = S | ((prevState: S, propagate?: boolean) => S);

export const useTimeoutEffect = <T>(
  initialValue: T,
  handler: (value: T) => void,
  delay: number = 500
): [T, Dispatch<SetStateAction<T>>] => {
  const [value, setValue] = useState<T>(initialValue);
  const [propage, setPropagate] = useState(true);

  const [timeoutId, setTimeoutId] = useState<number | null>(null);

  useEffect(() => {
    if (!propage) return;
    if (timeoutId) {
      clearTimeout(timeoutId);
    }
    const id = setTimeout(() => {
      handler(value);
    }, delay);
    setTimeoutId(id);
  }, [value]);

  const setValueWrapper = (value: SetStateAction<T>, propagate: boolean = true) => {
    setPropagate(propagate);
    setValue(value);
  };

  return [value, setValueWrapper];
};
