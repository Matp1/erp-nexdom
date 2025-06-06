import { createStore } from "vuex";
import axios from "axios";

export default createStore({
  state: {
    produtos: [],
    produtosPorTipo: [], // Inicializa como array vazio
    lucro: null,
    movimentacoes: [],
  },
  mutations: {
    SET_PRODUTOS(state, produtos) {
      state.produtos = produtos;
    },
    SET_PRODUTOS_POR_TIPO(state, produtos) {
      state.produtosPorTipo = produtos || []; // Garante que seja um array, mesmo que a resposta seja vazia
    },
    SET_LUCRO(state, lucro) {
      state.lucro = lucro;
    },
    SET_MOVIMENTACOES(state, movimentacoes) {
      state.movimentacoes = movimentacoes;
    },
  },
  actions: {
    async fetchProdutos({ commit }) {
      try {
        const response = await axios.get("/api/produtos");
        commit("SET_PRODUTOS", response.data);
      } catch (error) {
        commit("SET_PRODUTOS", []); // Em caso de erro, define como array vazio
        throw error; // Re-lança o erro para ser tratado pelo componente
      }
    },
    async fetchProdutosPorTipo({ commit }, tipoProduto) {
      try {
        const response = await axios.get(`/api/produtos/tipo/${tipoProduto}`);
        commit("SET_PRODUTOS_POR_TIPO", response.data);
      } catch (error) {
        commit("SET_PRODUTOS_POR_TIPO", []); // Em caso de erro, define como array vazio
        throw error; // Re-lança o erro para ser tratado pelo componente
      }
    },
    async fetchLucro({ commit }, codigo) {
      try {
        const response = await axios.get(`/api/produtos/lucro/${codigo}`);
        commit("SET_LUCRO", response.data);
      } catch (error) {
        commit("SET_LUCRO", null);
        throw error;
      }
    },
    async fetchMovimentacoes({ commit }, codigo) {
      try {
        const response = await axios.get(
          `/api/produtos/${codigo}/movimentacoes`
        );
        commit("SET_MOVIMENTACOES", response.data);
      } catch (error) {
        commit("SET_MOVIMENTACOES", []);
        throw error;
      }
    },
  },
  getters: {
    produtos: (state) => state.produtos,
    produtosPorTipo: (state) => state.produtosPorTipo,
    lucro: (state) => state.lucro,
    movimentacoes: (state) => state.movimentacoes,
  },
});
