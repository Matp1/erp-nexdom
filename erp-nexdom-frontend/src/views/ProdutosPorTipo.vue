<template>
  <div class="container">
    <h1>Consulta de Produtos por Tipo</h1>

    <div class="card">
      <label for="tipo">Selecione o Tipo de Produto:</label>
      <select id="tipo" v-model="tipoSelecionado" @change="buscarProdutos">
        <option value="">Selecione...</option>
        <option v-for="tipo in tipos" :key="tipo" :value="tipo">
          {{ tipo }}
        </option>
      </select>
    </div>

    <div class="card" v-if="produtos.length">
      <h2>Produtos do Tipo: {{ tipoSelecionado }}</h2>
      <table>
        <thead>
          <tr>
            <th>Código</th>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Estoque</th>
            <th>Valor Fornecedor</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in produtos" :key="produto.codigo">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.name }}</td>
            <td>{{ produto.descricao }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
            <td>
              R$
              {{
                produto.valorFornecedor
                  ? produto.valorFornecedor.toFixed(2)
                  : "0.00"
              }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="card" v-else-if="tipoSelecionado">
      <p>Nenhum produto encontrado para este tipo.</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import api from "@/services/api";

const tipos = ["ELETRONICO", "ELETRODOMESTICO", "MOVEL"];

const tipoSelecionado = ref("");
const produtos = ref([]);

const buscarProdutos = async () => {
  if (!tipoSelecionado.value) {
    produtos.value = [];
    return;
  }

  try {
    const response = await api.getProdutosPorTipo(tipoSelecionado.value);
    produtos.value = response.data;
  } catch (error) {
    console.error("Erro ao buscar produtos:", error);
    produtos.value = [];
  }
};
</script>
