import PropTypes from 'prop-types';
import { Navigate } from 'react-router-dom';
import SuspenseLoader from '../../components/common/SuspenseLoader';
import { useAuth } from '../../hooks/useAuth';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return <SuspenseLoader />;
  }

  return isAuthenticated ? children : <Navigate to="/logowanie" />;
};

export default ProtectedRoute;

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
};
