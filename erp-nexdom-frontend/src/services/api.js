import axios from "axios";

const apiClient = axios.create({
  baseURL: "/api", // Usa o caminho relativo, já que o proxy redireciona para o backend
  headers: {
    "Content-Type": "application/json",
  },
});

export default {
  getProdutos() {
    return apiClient.get("/produtos");
  },
  getProduto(codigo) {
    return apiClient.get(`/produtos/${codigo}`);
  },
  salvarProduto(produto) {
    return apiClient.post("/produtos", produto);
  },
  excluirProduto(codigo) {
    return apiClient.delete(`/produtos/${codigo}`);
  },
  atualizarProduto(codigo, produto) {
    return apiClient.put(`/produtos/${codigo}`, produto);
  },
  adicionarEstoque(codigo, quantidade) {
    return apiClient.put(`/produtos/${codigo}/entrada`, { quantidade });
  },
  reduzirEstoque(codigo, movimentacao) {
    return apiClient.put(`/produtos/${codigo}/saida`, movimentacao);
  },
  getProdutosPorTipo(tipoProduto) {
    return apiClient.get(`/produtos/tipo/${tipoProduto}`);
  },
  getLucroPorProduto(codigo) {
    return apiClient.get(`/produtos/lucro/${codigo}`);
  },
  getMovimentacoesPorProduto(codigo) {
    return apiClient.get(`/produtos/${codigo}/movimentacoes`);
  },
  getTotalEntradas() {
    return apiClient.get("/produtos/entrada/total");
  },

  getTotalSaidas() {
    return apiClient.get("/produtos/saida/total");
  },

  getLucroTotal() {
    return apiClient.get("/produtos/lucro/total");
  },

  listarMovimentacoes() {
    return apiClient.get("/produtos/todasmovimentacoes");
  },
};
