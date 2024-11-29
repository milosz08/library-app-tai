import { createContext, useState } from 'react';
import { Alert, Snackbar } from '@mui/material';
import PropTypes from 'prop-types';

export const AlertContext = createContext();

export const AlertProvider = ({ children }) => {
  const [alerts, setAlerts] = useState([]);

  const addAlert = (message, severity = 'info') => {
    const id = new Date().getTime();
    setAlerts(prev => [...prev, { id, message, severity }]);
  };

  const removeAlert = id => {
    setAlerts(prev => prev.filter(alert => alert.id !== id));
  };

  return (
    <AlertContext.Provider value={{ addAlert }}>
      {children}
      {alerts.map(alert => (
        <Snackbar
          key={alert.id}
          open
          autoHideDuration={10000}
          onClose={() => removeAlert(alert.id)}
          anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
          <Alert
            onClose={() => removeAlert(alert.id)}
            severity={alert.severity}
            variant="filled">
            {alert.message}
          </Alert>
        </Snackbar>
      ))}
    </AlertContext.Provider>
  );
};

AlertProvider.propTypes = {
  children: PropTypes.node.isRequired,
};
