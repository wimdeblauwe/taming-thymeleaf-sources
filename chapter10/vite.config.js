import {defineConfig} from 'vite';
import tailwindcss from '@tailwindcss/vite';
import path from 'path';
import springBoot from '@wim.deblauwe/vite-plugin-spring-boot';

export default defineConfig({
    plugins: [
        tailwindcss(),
        springBoot()
    ],
    root: path.join(import.meta.dirname, './src/main/resources'),
    build: {
        manifest: true,
        rollupOptions: {
            input: [
                '/static/css/application.css'
            ]
        },
        outDir: path.join(import.meta.dirname, `./target/classes/static`),
        copyPublicDir: false,
        emptyOutDir: true
    },
    server: {
        proxy: {
            // Proxy all backend requests to Spring Boot except for static assets
            '^/(?!static|assets|@|.*\\.(js|css|png|svg|jpg|jpeg|gif|ico|woff|woff2)$)': {
                target: 'http://localhost:8080',  // Proxy to Spring Boot backend
                changeOrigin: true,
                secure: false
            }
        },
        watch: {
            ignored: ['target/**']
        }
    }
});
