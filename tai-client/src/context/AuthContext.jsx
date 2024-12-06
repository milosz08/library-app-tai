import { createContext, useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import {
  login as loginApi,
  logout as logoutApi,
  sessionRevalidate,
} from '../api/authApi';
import { setLogoutFunction } from '../utils/authManager';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [role, setRole] = useState(null);
  const [roleName, setRoleName] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    const revalidateSession = async () => {
      const result = await sessionRevalidate();
      if (result.success) {
        setRole(result.role);
        setRoleName(result.roleName);
        setIsAuthenticated(true);
      } else {
        setIsAuthenticated(false);
      }
    };

    if (!isAuthenticated) {
      revalidateSession();
    }
    setLogoutFunction(handleLogout);
  }, [isAuthenticated]);

  const handleLogin = async (email, password) => {
    const result = await loginApi(email, password);
    if (result.status === 200) {
      if (result.token) {
        return {
          success: false,
          token: result.token,
        };
      } else if (result.role && result.roleName) {
        setRole(result.role);
        setRoleName(result.roleName);
        setIsAuthenticated(true);
        return {
          success: true,
          role: result.role,
          roleName: result.roleName,
        };
      }
    }
    return {
      success: false,
      error: result.details || 'Nieprawidłowe dane logowania.',
    };
  };

  const handleLogout = async () => {
    await logoutApi();
    setIsAuthenticated(false);
    setRole(null);
    setRoleName(null);
  };

  return (
    <AuthContext.Provider
      value={{ handleLogin, handleLogout, isAuthenticated, role, roleName }}>
      {children}
    </AuthContext.Provider>
  );
};

AuthProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
