import { createContext, useState } from 'react';
import PropTypes from 'prop-types';
import SuspenseLoader from '~/components/common/SuspenseLoader';

export const LoaderContext = createContext();

export const LoaderProvider = ({ children }) => {
  const [isLoading, setIsLoading] = useState(false);

  return (
    <LoaderContext.Provider value={{ isLoading, setIsLoading }}>
      {isLoading && <SuspenseLoader />}
      {children}
    </LoaderContext.Provider>
  );
};

LoaderProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
