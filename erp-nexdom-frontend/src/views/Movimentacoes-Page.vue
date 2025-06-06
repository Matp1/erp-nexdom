<template>
  <div class="container">
    <h1>Movimentações</h1>

    <!-- Formulário de movimentação -->
    <div class="card">
      <h2>Registrar Movimentação</h2>
      <form @submit.prevent="registrarMovimentacao">
        <div class="form-group">
          <label>Produto</label>
          <select v-model="movimentacao.produtoId" required>
            <option disabled value="">Selecione um produto</option>
            <option
              v-for="produto in produtos"
              :key="produto.codigo"
              :value="produto.codigo"
            >
              {{ produto.descricao }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label>Tipo de Movimentação</label>
          <select v-model="movimentacao.tipoMovimentacao" required>
            <option disabled value="">Selecione o tipo</option>
            <option value="ENTRADA">Entrada</option>
            <option value="SAIDA">Saída</option>
          </select>
        </div>

        <div class="form-group">
          <label>Quantidade</label>
          <input
            type="number"
            v-model.number="movimentacao.quantidade"
            required
          />
        </div>

        <div
          class="form-group"
          v-if="movimentacao.tipoMovimentacao === 'SAIDA'"
        >
          <label>Valor de Venda</label>
          <input
            type="number"
            v-model.number="movimentacao.valorVenda"
            step="0.01"
          />
        </div>

        <button type="submit">Registrar</button>
      </form>
    </div>

    <!-- Tabela de movimentações -->
    <div class="card">
      <h2>Histórico de Movimentações</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Produto ID</th>
            <th>Tipo</th>
            <th>Quantidade</th>
            <th>Data</th>
            <th>Valor Venda</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mov in movimentacoes" :key="mov.id">
            <td>{{ mov.id }}</td>
            <td>{{ mov.produtoId }}</td>
            <td>{{ mov.tipoMovimentacao }}</td>
            <td>{{ mov.quantidadeMovimentada }}</td>
            <td>{{ mov.dataVenda }}</td>
            <td>{{ mov.valorVenda }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/services/api";

const movimentacoes = ref([]);
const produtos = ref([]);

const movimentacao = ref({
  produtoId: "",
  tipoMovimentacao: "",
  quantidade: null,
  valorVenda: null,
});

function carregarMovimentacoes() {
  api
    .listarMovimentacoes()
    .then((response) => {
      movimentacoes.value = response.data;
    })
    .catch((error) => {
      console.error("Erro ao carregar movimentações:", error);
    });
}

const carregarProdutos = async () => {
  try {
    const response = await api.getProdutos();
    produtos.value = response.data;
  } catch (error) {
    console.error("Erro ao carregar produtos:", error);
  }
};

async function registrarMovimentacao() {
  try {
    if (movimentacao.value.tipoMovimentacao === "ENTRADA") {
      await api.adicionarEstoque(
        movimentacao.value.produtoId,
        movimentacao.value.quantidade
      );
    } else if (movimentacao.value.tipoMovimentacao === "SAIDA") {
      await api.reduzirEstoque(movimentacao.value.produtoId, {
        quantidade: movimentacao.value.quantidade,
        valorVenda: movimentacao.value.valorVenda,
      });
    }
    alert("Movimentação registrada com sucesso!");
    carregarMovimentacoes();
  } catch (error) {
    console.error("Erro ao registrar movimentação:", error);
    alert("Erro ao registrar movimentação.");
  }
}

/*const formatarData = (dataISO) => {
  const data = new Date(dataISO);
  return data.toLocaleDateString();
};*/

onMounted(() => {
  carregarMovimentacoes();
  carregarProdutos();
});
</script>

<style scoped>
.container {
  padding: 20px;
}

.card {
  background-color: #004d4d;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
}

.form-group {
  margin-bottom: 12px;
}

input,
select {
  width: 100%;
  padding: 6px;
  border-radius: 4px;
  border: none;
}

button {
  width: 100%;
  padding: 8px;
  background-color: #00bfa5;
  border: none;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #009688;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 8px;
  border-bottom: 1px solid #ccc;
  text-align: left;
}

th {
  background-color: #00796b;
  color: white;
}
</style>
