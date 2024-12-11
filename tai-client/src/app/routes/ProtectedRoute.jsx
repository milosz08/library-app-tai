import PropTypes from 'prop-types';
import { Navigate } from 'react-router-dom';
import SuspenseLoader from '~/components/common/SuspenseLoader';
import { useAuth } from '~/hooks/useAuth';

const ProtectedRoute = ({ children, allowedRoles }) => {
  const { isAuthenticated, isLoading, role } = useAuth();

  if (isLoading) {
    return <SuspenseLoader />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/logowanie" />;
  }

  if (allowedRoles && !allowedRoles.includes(role)) {
    return <Navigate to="/404" />;
  }

  return children;
};

export default ProtectedRoute;

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
  allowedRoles: PropTypes.arrayOf(PropTypes.string),
};
