<template>
  <header class="topbar">
    <div class="logo">
      <img src="@/assets/nexdom-logo.png" alt="Nexdom" />
    </div>
    <nav class="nav-links">
      <router-link to="/">Dashboard</router-link>
      <router-link to="/produtos">Produtos</router-link>
      <router-link to="/movimentacoes">Movimentações</router-link>
      <router-link to="/lucro">Lucro</router-link>
      <router-link to="/consulta">Consulta por Tipo</router-link>
    </nav>
  </header>
</template>

<script setup>
// Nenhum script necessário, funcional apenas com o template e o CSS
</script>

<style scoped>
.topbar {
  background-color: #021f1e;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.8rem 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo img {
  height: 32px;
}

.logo span {
  color: #0fd1b4;
  font-weight: bold;
  font-size: 1.2rem;
}

.nav-links {
  display: flex;
  gap: 1.5rem;
}

.nav-links a {
  color: #f0f0f0;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-links a:hover {
  color: #0fd1b4;
}

.router-link-active {
  color: #0fd1b4;
  font-weight: bold;
}
</style>
