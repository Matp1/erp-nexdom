<template>
  <nav class="navbar">
    <div class="logo">NEXDOM</div>
    <ul>
      <li><router-link to="/">Dashboard</router-link></li>
      <li><router-link to="/produtos">Produtos</router-link></li>
      <li><router-link to="/movimentacoes">Movimentações</router-link></li>
      <li><router-link to="/lucro">Lucro</router-link></li>
      <li><router-link to="/produtos-por-tipo">Por Tipo</router-link></li>
    </ul>
  </nav>
</template>

<style scoped>
.navbar {
  background-color: #014d4d;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  color: white;
}
.navbar .logo {
  font-weight: bold;
  font-size: 1.5rem;
}
.navbar ul {
  list-style: none;
  display: flex;
  gap: 2rem;
}
.navbar a {
  color: white;
  text-decoration: none;
}
.navbar a:hover {
  opacity: 0.7;
}
</style>
