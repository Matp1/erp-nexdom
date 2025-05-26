# 🚀 ERP Nexdom

Um sistema de gestão de estoque com funcionalidades completas para cadastro de produtos, controle de entradas e saídas, visualização de lucro e consultas detalhadas por tipo de produto.

![Badge](https://img.shields.io/badge/Status-Em%20Desenvolvimento-blue)
![GitHub license](https://img.shields.io/github/license/Matp1/erp-nexdom)
![GitHub repo size](https://img.shields.io/github/repo-size/Matp1/erp-nexdom)

---

## 🗂️ Tecnologias Utilizadas

### 🔗 **Frontend**
- Vue.js 3 + Vite
- JavaScript
- Axios
- CSS3

### 🔗 **Backend**
- Java 17+
- Spring Boot
- Spring Data JPA
- H2 Database (para desenvolvimento)

---

## 📜 Funcionalidades

- ✅ Cadastro de Produtos
- ✅ Edição e exclusão de produtos
- ✅ Controle de estoque (entradas e saídas)
- ✅ Registro de movimentações com histórico completo
- ✅ Cálculo de lucro total e por produto
- ✅ Consulta de produtos por tipo (Eletrônico, Eletrodoméstico e Móvel)
- ✅ Dashboard com visão geral de estoque, entradas, saídas e lucro
- ✅ Testes Unitários no backend

---

## 📦 Como rodar o projeto

### 🔧 Backend (Java + Spring)

```bash
# Clone o projeto
git clone https://github.com/Matp1/erp-nexdom.git

# Acesse a pasta do backend
cd backend

# Rode a aplicação
./mvnw spring-boot:run

# Acesse a pasta do frontend
cd frontend

# Instale as dependências
npm install

# Rode a aplicação
npm run dev

# Acesse a pasta do frontend
cd frontend

# Instale as dependências
npm install

# Rode a aplicação
npm run dev


### 🔗 Rotas da API

| Método | Endpoint                           | Descrição                           |
| ------ | ---------------------------------- | ----------------------------------- |
| GET    | `/api/produtos`                    | Lista todos os produtos             |
| POST   | `/api/produtos`                    | Cria um novo produto                |
| PUT    | `/api/produtos/{id}`               | Atualiza um produto                 |
| DELETE | `/api/produtos/{id}`               | Remove um produto                   |
| PUT    | `/api/produtos/{id}/entrada`       | Adiciona estoque                    |
| PUT    | `/api/produtos/{id}/saida`         | Reduz estoque com registro de venda |
| GET    | `/api/produtos/entrada/total`      | Total de entradas                   |
| GET    | `/api/produtos/saida/total`        | Total de saídas                     |
| GET    | `/api/produtos/lucro/total`        | Lucro total                         |
| GET    | `/api/produtos/lucro/{id}`         | Lucro de um produto específico      |
| GET    | `/api/produtos/tipo/{tipoProduto}` | Lista produtos por tipo             |
| GET    | `/api/produtos/{id}/movimentacoes` | Movimentações de um produto         |
| GET    | `/api/produtos/todasmovimentacoes` | Histórico geral de movimentações    |


###🎯 Layout
✔️ Interface simples, intuitiva e responsiva.

✔️ Foco na usabilidade e clareza das informações.

###🧑‍💻 Desenvolvido por
@Matp1 💙

###⭐ Se curtir, deixe uma ⭐ no repositório!


