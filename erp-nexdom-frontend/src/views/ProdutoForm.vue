<template>
  <div class="container">
    <h1>Cadastro de Produto</h1>

    <div class="card">
      <label>Nome</label>
      <input v-model="produto.name" placeholder="Nome do produto" />

      <label>Descrição</label>
      <input v-model="produto.descricao" placeholder="Descrição do produto" />

      <div style="display: flex; gap: 1rem">
        <div style="flex: 1">
          <label>Preço</label>
          <input type="number" v-model="produto.preco" placeholder="0.00" />
        </div>
        <div style="flex: 1">
          <label>Categoria</label>
          <select v-model="produto.tipoProduto" required>
            <option value="ELETRONICO">Eletrônico</option>
            <option value="ELETRODOMESTICO">Eletrodoméstico</option>
            <option value="MOVEL">Móvel</option>
          </select>
        </div>
      </div>

      <label>Estoque</label>
      <input
        type="number"
        v-model="produto.quantidadeEstoque"
        placeholder="0"
      />

      <button @click="salvar">Salvar</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import api from "@/services/api";
import { useRouter, useRoute } from "vue-router";

const route = useRoute();
const router = useRouter();

const produto = ref({
  nome: "",
  descricao: "",
  preco: 0,
  valorFornecedor: 0,
  quantidadeEstoque: 0,
  tipoProduto: "",
});

const isEdicao = ref(false);

onMounted(async () => {
  if (route.params.id) {
    isEdicao.value = true;
    const response = await api.getProduto(route.params.id);
    produto.value = response.data;
  }
});

async function salvar() {
  try {
    if (isEdicao.value) {
      await api.atualizarProduto(route.params.id, produto.value);
      alert("Produto atualizado com sucesso!");
    } else {
      await api.salvarProduto(produto.value);
      alert("Produto cadastrado com sucesso!");
    }
    router.push("/produtos");
  } catch (error) {
    console.error("Erro ao salvar produto:", error);
    alert("Erro ao salvar produto.");
  }
}
</script>
