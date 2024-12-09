let logoutFunction = null;

export const setLogoutFunction = fn => {
  logoutFunction = fn;
};

export const executeLogout = async () => {
  if (logoutFunction) {
    await logoutFunction();
  }
};
