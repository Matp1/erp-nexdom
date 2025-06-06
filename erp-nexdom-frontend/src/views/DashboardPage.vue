<template>
  <div class="container">
    <h1>Dashboard</h1>

    <div class="metrics">
      <div class="card">
        <h3><i class="fas fa-box metric-icon"></i> Produtos Totais</h3>
        <p>{{ dashboard.totalProdutos }}</p>
      </div>
      <div class="card">
        <h3><i class="fas fa-arrow-up metric-icon"></i> Entradas</h3>
        <p>{{ dashboard.totalEntradas }}</p>
      </div>
      <div class="card">
        <h3><i class="fas fa-arrow-down metric-icon"></i> Saídas</h3>
        <p>{{ dashboard.totalSaidas }}</p>
      </div>
      <div class="card">
        <h3><i class="fas fa-dollar-sign metric-icon"></i> Lucro</h3>
        <p>R$ {{ dashboard.lucroTotal }}</p>
      </div>
    </div>

    <div class="card">
      <h2>Produtos</h2>
      <table>
        <thead>
          <tr>
            <th>Código</th>
            <th>Descrição</th>
            <th>Tipo</th>
            <th>Estoque</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in produtos" :key="produto.codigo">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.descricao }}</td>
            <td>{{ produto.tipoProduto }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/services/api";
import axios from "axios";

const produtos = ref([]);
const totalEntradas = ref(0);
const totalSaidas = ref(0);
const totalLucro = ref(0);
//const totalProdutos = ref(0);
//const lucroTotal = ref(0);
const dashboard = ref({
  totalProdutos: 0,
  totalEntradas: 0,
  totalSaidas: 0,
  lucroTotal: 0,
});

onMounted(async () => {
  try {
    const resProdutos = await api.getProdutos();
    produtos.value = resProdutos.data;

    // Simulando entrada e saída a partir das movimentações (Exemplo simplificado):
    let entradas = 0;
    let saidas = 0;
    let lucroTotal = 0;

    for (const produto of resProdutos.data) {
      const movimentacoesRes = await api.getMovimentacoesPorProduto(
        produto.codigo
      );
      const movimentacoes = movimentacoesRes.data;

      entradas += movimentacoes
        .filter((m) => m.tipoMovimentacao === "Entrada")
        .reduce((sum, m) => sum + m.quantidade, 0);

      saidas += movimentacoes
        .filter((m) => m.tipoMovimentacao === "Saída")
        .reduce((sum, m) => sum + m.quantidade, 0);

      const lucroRes = await api.getLucroPorProduto(produto.codigo);
      lucroTotal += lucroRes.data.lucro;
    }

    totalEntradas.value = entradas;
    totalSaidas.value = saidas;
    totalLucro.value = lucroTotal;
  } catch (error) {
    console.error("Erro ao carregar dados do dashboard:", error);
  }
  carregarProdutos();
  carregarDashboard();
  api.getTotalEntradas().then((response) => {
    dashboard.value.totalEntradas = response.data;
  });

  api.getTotalSaidas().then((response) => {
    dashboard.value.totalSaidas = response.data;
  });

  api.getLucroTotal().then((response) => {
    dashboard.value.lucroTotal = response.data;
  });
});

function carregarProdutos() {
  axios.get("http://localhost:8080/api/produtos").then((response) => {
    produtos.value = response.data;
    dashboard.value.totalProdutos = produtos.value.length;
  });
}

function carregarDashboard() {
  api.getTotalEntradas().then((response) => {
    dashboard.value.totalEntradas = response.data;
  });

  api.getTotalSaidas().then((response) => {
    dashboard.value.totalSaidas = response.data;
  });

  api.getLucroTotal().then((response) => {
    dashboard.value.lucroTotal = response.data;
  });
}
</script>

<style scoped>
.metrics {
  display: flex;
  gap: 1rem;
  justify-content: space-between;
  flex-wrap: wrap;
}

.metrics .card {
  flex: 1;
  min-width: 180px;
}
</style>
