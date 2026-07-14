import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api/process-definitions': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/api/v1/camunda8': {
        target: 'http://localhost:8090',
        changeOrigin: true,
      },
      '/api/monitoring': {
        target: 'http://localhost:8094',
        changeOrigin: true,
      },
    },
  },
});
