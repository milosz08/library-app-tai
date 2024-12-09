import { useContext } from 'react';
import { LoaderContext } from '../context/LoaderContenxt';

export const useLoader = () => useContext(LoaderContext);
