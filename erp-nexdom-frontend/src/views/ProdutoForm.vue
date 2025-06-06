<template>
  <div class="container">
    <h1>{{ isEdicao ? "Editar Produto" : "Cadastro de Produto" }}</h1>

    <div class="card">
      <label>Nome</label>
      <input v-model="produto.name" placeholder="Nome do produto" />

      <label>Descrição</label>
      <input v-model="produto.descricao" placeholder="Descrição do produto" />

      <div style="display: flex; gap: 1rem">
        <div style="flex: 1">
          <label>Preço de Fornecimento</label>
          <input
            type="number"
            v-model="produto.preco"
            placeholder="0.00"
            step="0.01"
          />
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
  name: "",
  descricao: "",
  preco: 0,
  quantidadeEstoque: 0,
  tipoProduto: "",
});

const isEdicao = ref(false);

onMounted(async () => {
  if (route.params.id) {
    try {
      isEdicao.value = true;
      const response = await api.getProduto(route.params.id);
      // Mapeia os dados do backend para o frontend
      produto.value = {
        name: response.data.name,
        descricao: response.data.descricao,
        preco: response.data.valorFornecedor, // Mapeia valorFornecedor para preco
        quantidadeEstoque: response.data.quantidadeEstoque,
        tipoProduto: response.data.tipoProduto,
      };
    } catch (error) {
      console.error("Erro ao carregar produto:", error);
      alert("Erro ao carregar produto.");
    }
  }
});

async function salvar() {
  try {
    // Cria objeto compatível com o backend (Produto)
    const produtoData = {
      name: produto.value.name,
      descricao: produto.value.descricao,
      valorFornecedor: produto.value.preco, // Mapeia preco para valorFornecedor
      quantidadeEstoque: produto.value.quantidadeEstoque,
      tipoProduto: produto.value.tipoProduto,
    };

    if (isEdicao.value) {
      await api.atualizarProduto(route.params.id, produtoData);
      alert("Produto atualizado com sucesso!");
    } else {
      await api.salvarProduto(produtoData);
      alert("Produto cadastrado com sucesso!");
    }
    router.push("/produtos");
  } catch (error) {
    console.error("Erro ao salvar produto:", error);
    // Exibe mensagem de erro específica do backend, se disponível
    const errorMessage = error.response?.data || "Erro ao salvar produto.";
    alert(errorMessage);
  }
}
</script>
