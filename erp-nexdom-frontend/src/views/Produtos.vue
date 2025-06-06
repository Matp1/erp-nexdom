<template>
  <div class="container mt-5">
    <h2>Gerenciar Produtos</h2>
    <div class="mb-4">
      <h4>{{ isEditing ? "Editar Produto" : "Adicionar Novo Produto" }}</h4>
      <produto-form
        :produto-inicial="produtoSelecionado"
        :is-editing="isEditing"
        @produto-salvo="fetchProdutos"
      />
    </div>
    <produto-list
      :produtos="produtos"
      @editar-produto="editarProduto"
      @excluir-produto="excluirProduto"
      @adicionar-estoque="adicionarEstoque"
      @reduzir-estoque="reduzirEstoque"
    />
  </div>
</template>

<script>
import ProdutoForm from "@/components/ProdutoForm.vue";
import ProdutoList from "@/components/ProdutoList.vue";
import api from "@/services/api";
import { mapGetters, mapActions } from "vuex";

export default {
  name: "ProdutosPage",
  components: {
    ProdutoForm,
    ProdutoList,
  },
  data() {
    return {
      produtoSelecionado: {
        codigo: null,
        name: "",
        descricao: "",
        tipoProduto: "Eletrônico",
        valorFornecedor: 0,
        quantidadeEstoque: 0,
      },
      isEditing: false,
    };
  },
  computed: {
    ...mapGetters(["produtos"]),
  },
  methods: {
    ...mapActions(["fetchProdutos"]),
    editarProduto(produto) {
      this.produtoSelecionado = { ...produto };
      this.isEditing = true;
    },
    async excluirProduto(codigo) {
      if (confirm("Tem certeza que deseja excluir este produto?")) {
        try {
          await api.excluirProduto(codigo);
          this.fetchProdutos();
        } catch (error) {
          alert("Erro ao excluir o produto: " + error.message);
        }
      }
    },
    async adicionarEstoque(codigo) {
      const quantidade = prompt("Digite a quantidade a ser adicionada:");
      if (quantidade && !isNaN(quantidade) && parseInt(quantidade) > 0) {
        try {
          await api.adicionarEstoque(codigo, parseInt(quantidade));
          this.fetchProdutos();
        } catch (error) {
          alert("Erro ao adicionar estoque: " + error.message);
        }
      } else {
        alert("Por favor, insira uma quantidade válida.");
      }
    },
    async reduzirEstoque(codigo) {
      const quantidade = prompt("Digite a quantidade a ser reduzida:");
      const valorVenda = prompt("Digite o valor de venda:");
      if (
        quantidade &&
        !isNaN(quantidade) &&
        parseInt(quantidade) > 0 &&
        valorVenda &&
        !isNaN(valorVenda) &&
        parseFloat(valorVenda) >= 0
      ) {
        try {
          await api.reduzirEstoque(codigo, {
            quantidade: parseInt(quantidade),
            valorVenda: parseFloat(valorVenda),
          });
          this.fetchProdutos();
        } catch (error) {
          alert("Erro ao reduzir estoque: " + error.message);
        }
      } else {
        alert("Por favor, insira valores válidos.");
      }
    },
  },
  mounted() {
    this.fetchProdutos();
  },
};
</script>
