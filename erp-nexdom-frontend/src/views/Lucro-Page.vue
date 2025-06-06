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
            <th>Descrição</th>
            <th>Categoria</th>
            <th>Estoque</th>
            <th>Valor Fornecedor</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in produtos" :key="produto.codigo">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.descricao }}</td>
            <td>{{ produto.tipoProduto }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
            <td>R$ {{ produto.valorFornecedor.toFixed(2) }}</td>
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
    produtos.value = response.data;
  } catch (error) {
    console.error("Erro ao carregar produtos:", error);
  }
};

onMounted(() => {
  carregarLucroTotal();
  carregarProdutos();
});
</script>
