<template>
  <div class="container">
    <h1>Lucro</h1>

    <div class="card">
      <h2>Lucro Total</h2>
      <p>
        <strong>R$ {{ lucroTotal.toFixed(2) }}</strong>
      </p>
    </div>

    <div class="card">
      <h2>Lucro por Produto</h2>
      <table>
        <thead>
          <tr>
            <th>Código</th>
            <th>Nome</th>
            <!-- Mantido: Alterado de Descrição para Nome -->
            <th>Categoria</th>
            <th>Estoque</th>
            <th>Valor Fornecedor</th>
            <th>Quantidade Vendida</th>
            <th>Lucro</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in produtos" :key="produto.codigo">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.name }}</td>
            <!-- Mantido: Alterado de descricao para name -->
            <td>{{ produto.tipoProduto }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
            <td>R$ {{ produto.valorFornecedor.toFixed(2) }}</td>
            <td>{{ produto.lucro?.quantidadeTotalSaida || 0 }}</td>
            <td>R$ {{ produto.lucro?.lucroTotal.toFixed(2) || "0.00" }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/services/api";

const produtos = ref([]);
const lucroTotal = ref(0);

const carregarLucroTotal = async () => {
  try {
    const response = await api.getLucroTotal();
    lucroTotal.value = response.data;
  } catch (error) {
    console.error("Erro ao carregar lucro total:", error);
  }
};

const carregarProdutos = async () => {
  try {
    const response = await api.getProdutos();
    // Para cada produto, carrega apenas os dados de lucro
    produtos.value = await Promise.all(
      response.data.map(async (produto) => {
        try {
          // Carrega lucro do produto
          const lucroResponse = await api.getLucroPorProduto(produto.codigo);
          const lucro = lucroResponse.data;
          return {
            ...produto,
            lucro, // Adiciona lucro (lucroTotal, quantidadeTotalSaida)
          };
        } catch (error) {
          console.error(
            `Erro ao carregar lucro do produto ${produto.codigo}:`,
            error
          );
          return {
            ...produto,
            lucro: { lucroTotal: 0, quantidadeTotalSaida: 0 },
          };
        }
      })
    );
  } catch (error) {
    console.error("Erro ao carregar produtos:", error);
  }
};

onMounted(() => {
  carregarLucroTotal();
  carregarProdutos();
});
</script>
