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
              {{ produto.name }}
              <!-- Alterado de descricao para name -->
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
            required
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
            <th>Produto</th>
            <!-- Alterado de Produto ID para Produto -->
            <th>Tipo</th>
            <th>Quantidade</th>
            <th>Data</th>
            <th>Valor Venda</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="mov in movimentacoes" :key="mov.id">
            <td>{{ mov.id }}</td>
            <td>
              {{ getProdutoName(mov.produtoId) }}
              <!-- Exibe o name do produto -->
            </td>
            <td>{{ mov.tipoMovimentacao }}</td>
            <td>{{ mov.quantidadeMovimentada }}</td>
            <td>{{ formatarData(mov.dataVenda) }}</td>
            <!-- Usando função formatarData -->
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

// Função para obter o name do produto pelo produtoId
function getProdutoName(produtoId) {
  const produto = produtos.value.find((p) => p.codigo === produtoId);
  return produto ? produto.name : "Produto não encontrado";
}

// Função para formatar a data (descomentada e ajustada)
function formatarData(dataISO) {
  if (!dataISO) return "-";
  const data = new Date(dataISO);
  return data.toLocaleDateString("pt-BR");
}

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
        valorVenda: movimentacao.value.valorVenda || 0,
      });
    }
    alert("Movimentação registrada com sucesso!");
    // Reseta o formulário
    movimentacao.value = {
      produtoId: "",
      tipoMovimentacao: "",
      quantidade: null,
      valorVenda: null,
    };
    carregarMovimentacoes();
  } catch (error) {
    console.error("Erro ao registrar movimentação:", error);
    // Exibe a mensagem de erro específica do backend
    const mensagemErro =
      error.response?.data || "Erro ao registrar movimentação.";
    alert(mensagemErro);
  }
}

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
