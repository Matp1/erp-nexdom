<template>
  <div class="container">
    <h1>Lista de Produtos</h1>

    <div class="card">
      <button class="add-button" @click="adicionarProduto">
        Adicionar Produto
      </button>

      <table>
        <thead>
          <tr>
            <th>Código</th>
            <th>Nome</th>
            <th>Descrição</th>
            <th>Valor Fornecedor</th>
            <th>Quantidade</th>
            <th>Tipo</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="produto in produtos" :key="produto.codigo">
            <td>{{ produto.codigo }}</td>
            <td>{{ produto.name }}</td>
            <td>{{ produto.descricao }}</td>
            <td>R$ {{ Number(produto.valorFornecedor).toFixed(2) }}</td>
            <td>{{ produto.quantidadeEstoque }}</td>
            <td>{{ produto.tipoProduto }}</td>
            <td>
              <button @click="editarProduto(produto.codigo)">Editar</button>

              <button
                class="delete-button"
                @click="removerProduto(produto.codigo)"
              >
                Remover
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/services/api";
import { useRouter } from "vue-router";

const router = useRouter();
const produtos = ref([]);

onMounted(() => {
  carregarProdutos();
});

function carregarProdutos() {
  api
    .getProdutos()
    .then((response) => {
      produtos.value = response.data;
    })
    .catch((error) => {
      console.error("Erro ao buscar produtos:", error);
    });
}

function adicionarProduto() {
  router.push({ name: "NovoProduto" });
}

function editarProduto(id) {
  router.push({ name: "EditarProduto", params: { id } });
}
function removerProduto(codigo) {
  if (confirm("Tem certeza que deseja remover este produto?")) {
    api
      .excluirProduto(codigo)
      .then(() => {
        produtos.value = produtos.value.filter(
          (produto) => produto.codigo !== codigo
        );
      })
      .catch((error) => {
        console.error("Erro ao remover produto:", error);
      });
  }
}
</script>

<style scoped>
.container {
  padding: 2rem;
}

h1 {
  margin-bottom: 1rem;
}

.card {
  background-color: #004d4d;
  padding: 1.5rem;
  border-radius: 10px;
}

.add-button {
  background-color: #1abc9c;
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 5px;
  margin-bottom: 1rem;
  cursor: pointer;
}

.add-button:hover {
  background-color: #16a085;
}

table {
  width: 100%;
  border-collapse: collapse;
  color: white;
}

th,
td {
  padding: 0.8rem;
  text-align: left;
  border-bottom: 1px solid #1abc9c;
}

th {
  background-color: #006666;
}

tr:hover {
  background-color: #005555;
}

.edit-button {
  background-color: #3498db;
  color: white;
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 5px;
  margin-right: 0.5rem;
  cursor: pointer;
}

.edit-button:hover {
  background-color: #2980b9;
}

.delete-button {
  background-color: #e74c3c;
  color: white;
  border: none;
  padding: 0.4rem 0.8rem;
  border-radius: 5px;
  cursor: pointer;
}

.delete-button:hover {
  background-color: #c0392b;
}
</style>
