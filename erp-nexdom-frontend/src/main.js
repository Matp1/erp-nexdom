import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "bootstrap/dist/css/bootstrap.min.css"; // Importa o CSS do Bootstrap
import "bootstrap"; // Importa o JavaScript do Bootstrap
import "./assets/styles.css";
import "@fortawesome/fontawesome-free/css/all.css";

const app = createApp(App);
app.use(router);
app.use(store);
app.mount("#app");
