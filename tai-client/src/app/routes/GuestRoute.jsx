import PropTypes from 'prop-types';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';

const GuestRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return !isAuthenticated ? children : <Navigate to="/" />;
};

export default GuestRoute;

GuestRoute.propTypes = {
  children: PropTypes.node.isRequired,
};
