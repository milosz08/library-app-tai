import { useContext } from 'react';
import { LoaderContext } from '~/context/LoaderContext';

export const useLoader = () => useContext(LoaderContext);
