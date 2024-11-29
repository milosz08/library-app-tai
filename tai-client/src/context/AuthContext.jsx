import { createContext, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { login as loginApi, logout as logoutApi } from '../api/authApi';
import { setLogoutFunction } from '../utils/authManager';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    setLogoutFunction(handleLogout);
  }, []);

  const handleLogin = async (email, password) => {
    const result = await loginApi(email, password);
    if (result.status === 204) {
      setIsAuthenticated(true);
      return { success: true };
    } else if (result.status === 200) {
      return {
        success: false,
        token: result.token,
      };
    }

    return {
      success: false,
      error: result.details || 'Nieprawidłowe dane logowania.',
    };
  };

  const handleLogout = async () => {
    const success = await logoutApi();
    if (success) {
      setIsAuthenticated(false);
      setUser(null);
    }
    return success;
  };

  return (
    <AuthContext.Provider
      value={{ user, handleLogin, handleLogout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
