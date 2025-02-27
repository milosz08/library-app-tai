import react from '@vitejs/plugin-react';
import path from 'path';
import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const { VITE_PROXY_API_URL } = loadEnv(mode, process.cwd());
  return {
    plugins: [react()],
    build: {
      outDir: 'target/dist',
    },
    server: {
      proxy:
        mode === 'development'
          ? {
              '/api': {
                target: VITE_PROXY_API_URL,
              },
            }
          : {},
    },
    resolve: {
      alias: {
        '~': path.resolve(__dirname, 'src'),
      },
    },
  };
});
