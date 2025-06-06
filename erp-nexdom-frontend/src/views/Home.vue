<template>
  <div class="container mt-5">
    <h1>Bem-vindo ao ERP Nexdom</h1>
    <p>Gerencie seus produtos e estoque de forma eficiente.</p>
    <div class="mt-4">
      <router-link to="/produtos" class="btn btn-primary me-2"
        >Gerenciar Produtos</router-link
      >
      <router-link to="/produtos-por-tipo" class="btn btn-secondary me-2"
        >Produtos por Tipo</router-link
      >
      <router-link to="/lucro" class="btn btn-success me-2"
        >Consultar Lucro</router-link
      >
      <router-link to="/movimentacoes" class="btn btn-info"
        >Movimentações</router-link
      >
    </div>
  </div>
</template>

<script>
export default {
  name: "HomePage",
};
</script>
