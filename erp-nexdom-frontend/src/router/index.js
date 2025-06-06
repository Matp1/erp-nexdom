import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "../views/DashboardPage.vue";
import ProdutoList from "../views/ProdutoList.vue";
import ProdutoForm from "../views/ProdutoForm.vue";
import Movimentacoes from "../views/Movimentacoes-Page.vue";
import Lucro from "../views/Lucro-Page.vue";
import ConsultaPorTipo from "../views/ProdutosPorTipo.vue";

const routes = [
  { path: "/", name: "Dashboard", component: Dashboard },
  { path: "/produtos", name: "Produtos", component: ProdutoList },
  { path: "/produto/novo", name: "NovoProduto", component: ProdutoForm },
  { path: "/movimentacoes", name: "Movimentacoes", component: Movimentacoes },
  { path: "/lucro", name: "Lucro", component: Lucro },
  { path: "/consulta", name: "ConsultaPorTipo", component: ConsultaPorTipo },
  {
    path: "/produto/editar/:id",
    name: "EditarProduto",
    component: ProdutoForm,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
