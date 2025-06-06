const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081, // Define a nova porta para o frontend
    open: true, // Abre o navegador automaticamente ao iniciar o servidor
    proxy: {
      "/api": {
        target: "http://localhost:8080", // Proxy para o backend
        changeOrigin: true,
      },
    },
  },
});
