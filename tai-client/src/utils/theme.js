import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    custom: {
      50: '#fafafa',
      100: '#f5f5f5',
      200: '#e5e5e5',
      300: '#d4d4d4',
      400: '#a3a3a3',
      500: '#737373',
      600: '#525252',
      700: '#404040',
      800: '#262626',
      900: '#171717',
      950: '#0a0a0a',
    },
    customBlue: {
      50: '#e3e8f0',
      100: '#b0b9c8',
      200: '#7d8aa0',
      300: '#4a5b78',
      400: '#273754',
      500: '#1b2a45',
      600: '#142137',
      700: '#0e1829',
      800: '#080e1b',
      900: '#03050e',
    },
  },
});

export default theme;
