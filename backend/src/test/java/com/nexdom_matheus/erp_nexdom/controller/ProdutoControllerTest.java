package com.nexdom_matheus.erp_nexdom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexdom_matheus.dto.LucroDTO;
import com.nexdom_matheus.dto.MovimentacaoRequest;
import com.nexdom_matheus.dto.ProdutoPorTipoDTO;
import com.nexdom_matheus.erp_nexdom.model.MovimentoEstoque;
import com.nexdom_matheus.erp_nexdom.model.Produto;
import com.nexdom_matheus.erp_nexdom.model.enums.TipoProduto;
import com.nexdom_matheus.erp_nexdom.repository.MovimentoEstoqueRepository;
import com.nexdom_matheus.erp_nexdom.repository.ProdutoRepository;
import com.nexdom_matheus.erp_nexdom.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
                "spring.autoconfigure.exclude=" +
                                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
                                "org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration",
                "spring.jpa.hibernate.ddl-auto=none",
                "spring.datasource.url="
})
public class ProdutoControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private ProdutoService produtoService;

        @MockBean
        private MovimentoEstoqueRepository movimentoEstoqueRepository;

        @MockBean
        private ProdutoRepository produtoRepository; // Adicionado para garantir que o JPA não seja carregado

        @Autowired
        private ObjectMapper objectMapper;

        private Produto produto;

        @BeforeEach
        void setUp() {
                produto = new Produto();
                produto.setCodigo(1L);
                produto.setName("Smartphone XYZ");
                produto.setDescricao("Smartphone de última geração");
                produto.setTipoProduto(TipoProduto.ELETRONICO);
                produto.setValorFornecedor(1000.0);
                produto.setQuantidadeEstoque(50);
        }

        @Test
        void testListarProdutos() throws Exception {
                List<Produto> produtos = Arrays.asList(produto);
                when(produtoService.listarProdutos()).thenReturn(produtos);

                mockMvc.perform(get("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Smartphone XYZ"))
                                .andExpect(jsonPath("$[0].descricao").value("Smartphone de última geração"));

                verify(produtoService, times(1)).listarProdutos();
        }

        @Test
        void testBuscarProduto_Sucesso() throws Exception {
                when(produtoService.buscarProdutoPorCodigo(1L)).thenReturn(Optional.of(produto));

                mockMvc.perform(get("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Smartphone XYZ"))
                                .andExpect(jsonPath("$.descricao").value("Smartphone de última geração"));

                verify(produtoService, times(1)).buscarProdutoPorCodigo(1L);
        }

        @Test
        void testBuscarProduto_NaoEncontrado() throws Exception {
                when(produtoService.buscarProdutoPorCodigo(1L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(produtoService, times(1)).buscarProdutoPorCodigo(1L);
        }

        @Test
        void testSalvarProduto_Sucesso() throws Exception {
                when(produtoService.salvarProduto(any(Produto.class))).thenReturn(produto);

                mockMvc.perform(post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(produto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Smartphone XYZ"))
                                .andExpect(jsonPath("$.descricao").value("Smartphone de última geração"));

                verify(produtoService, times(1)).salvarProduto(any(Produto.class));
        }

        @Test
        void testSalvarProduto_TipoInvalido() throws Exception {
                when(produtoService.salvarProduto(any(Produto.class)))
                                .thenThrow(new IllegalArgumentException(
                                                "Tipo de produto inválido. Valores permitidos: [Eletrônico, Eletrodoméstico, Móvel]"));

                mockMvc.perform(post("/api/produtos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                    {
                                                        "name": "Produto Inválido",
                                                        "descricao": "Teste",
                                                        "preco": 100.0,
                                                        "quantidadeEstoque": 10,
                                                        "tipoProduto": "Invalido"
                                                    }
                                                """))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(
                                                org.hamcrest.Matchers.containsString("Tipo de produto inválido")));

                verify(produtoService, times(1)).salvarProduto(any(Produto.class));
        }

        @Test
        void testExcluirProduto_Sucesso() throws Exception {
                doNothing().when(produtoService).excluirProduto(1L);

                mockMvc.perform(delete("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());

                verify(produtoService, times(1)).excluirProduto(1L);
        }

        @Test
        void testExcluirProduto_NaoEncontrado() throws Exception {
                doThrow(new IllegalArgumentException("Produto não encontrado")).when(produtoService).excluirProduto(1L);

                mockMvc.perform(delete("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(produtoService, times(1)).excluirProduto(1L);
        }

        @Test
        void testAtualizarProduto_Sucesso() throws Exception {
                Produto produtoAtualizado = new Produto();
                produtoAtualizado.setCodigo(1L);
                produtoAtualizado.setName("Smartphone ABC");
                produtoAtualizado.setDescricao("Nova descrição");
                produto.setTipoProduto(TipoProduto.ELETRONICO);
                produtoAtualizado.setValorFornecedor(1200.0);
                produtoAtualizado.setQuantidadeEstoque(40);

                when(produtoService.atualizarProduto(eq(1L), any(Produto.class))).thenReturn(produtoAtualizado);

                mockMvc.perform(put("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Smartphone ABC"))
                                .andExpect(jsonPath("$.descricao").value("Nova descrição"));

                verify(produtoService, times(1)).atualizarProduto(eq(1L), any(Produto.class));
        }

        @Test
        void testAtualizarProduto_NaoEncontrado() throws Exception {
                Produto produtoAtualizado = new Produto();
                when(produtoService.atualizarProduto(eq(1L), any(Produto.class)))
                                .thenThrow(new IllegalArgumentException("Produto não encontrado"));

                mockMvc.perform(put("/api/produtos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(produtoAtualizado)))
                                .andExpect(status().isNotFound());

                verify(produtoService, times(1)).atualizarProduto(eq(1L), any(Produto.class));
        }

        @Test
        void testAdicionarEstoque_Sucesso() throws Exception {
                Produto produtoAtualizado = new Produto();
                produtoAtualizado.setCodigo(1L);
                produtoAtualizado.setName("Smartphone XYZ");
                produtoAtualizado.setDescricao("Smartphone de última geração");
                produto.setTipoProduto(TipoProduto.ELETRONICO);
                produtoAtualizado.setValorFornecedor(1000.0);
                produtoAtualizado.setQuantidadeEstoque(60);

                Map<String, Integer> request = new HashMap<>();
                request.put("quantidade", 10);

                when(produtoService.adicionarEstoque(1L, 10)).thenReturn(produtoAtualizado);

                mockMvc.perform(put("/api/produtos/1/entrada")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.quantidadeEstoque").value(60));

                verify(produtoService, times(1)).adicionarEstoque(1L, 10);
        }

        @Test
        void testAdicionarEstoque_QuantidadeInvalida() throws Exception {
                Map<String, Integer> request = new HashMap<>();
                request.put("quantidade", 0);

                mockMvc.perform(put("/api/produtos/1/entrada")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("A quantidade deve ser maior que zero"));

                verify(produtoService, never()).adicionarEstoque(anyLong(), anyInt());
        }

        @Test
        void testAdicionarEstoque_ProdutoNaoEncontrado() throws Exception {
                Map<String, Integer> request = new HashMap<>();
                request.put("quantidade", 10);

                when(produtoService.adicionarEstoque(1L, 10))
                                .thenThrow(new IllegalArgumentException("Produto não encontrado"));

                mockMvc.perform(put("/api/produtos/1/entrada")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Produto não encontrado"));

                verify(produtoService, times(1)).adicionarEstoque(1L, 10);
        }

        @Test
        void testReduzirEstoque_Sucesso() throws Exception {
                Produto produtoAtualizado = new Produto();
                produtoAtualizado.setCodigo(1L);
                produtoAtualizado.setName("Smartphone XYZ");
                produtoAtualizado.setDescricao("Smartphone de última geração");
                produto.setTipoProduto(TipoProduto.ELETRONICO);
                produtoAtualizado.setValorFornecedor(1000.0);
                produtoAtualizado.setQuantidadeEstoque(45);

                MovimentacaoRequest request = new MovimentacaoRequest(5, 1500.0);

                when(produtoService.reduzirEstoque(1L, 5, 1500.0)).thenReturn(produtoAtualizado);

                mockMvc.perform(put("/api/produtos/1/saida")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.quantidadeEstoque").value(45));

                verify(produtoService, times(1)).reduzirEstoque(1L, 5, 1500.0);
        }

        @Test
        void testReduzirEstoque_EstoqueInsuficiente() throws Exception {
                MovimentacaoRequest request = new MovimentacaoRequest(60, 1500.0);
                when(produtoService.reduzirEstoque(1L, 60, 1500.0))
                                .thenThrow(new IllegalArgumentException("Estoque insuficiente para a saída."));

                mockMvc.perform(put("/api/produtos/1/saida")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Estoque insuficiente para a saída."));

                verify(produtoService, times(1)).reduzirEstoque(1L, 60, 1500.0);
        }

        @Test
        void testReduzirEstoque_QuantidadeInvalida() throws Exception {
                MovimentacaoRequest request = new MovimentacaoRequest(0, 1500.0);

                mockMvc.perform(put("/api/produtos/1/saida")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("A quantidade deve ser maior que zero"));

                verify(produtoService, never()).reduzirEstoque(anyLong(), anyInt(), anyDouble());
        }

        @Test
        void testReduzirEstoque_ValorVendaInvalido() throws Exception {
                MovimentacaoRequest request = new MovimentacaoRequest(5, -100.0);

                mockMvc.perform(put("/api/produtos/1/saida")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("O valor de venda não pode ser negativo"));

                verify(produtoService, never()).reduzirEstoque(anyLong(), anyInt(), anyDouble());
        }

        @Test
        void testReduzirEstoque_ProdutoNaoEncontrado() throws Exception {
                MovimentacaoRequest request = new MovimentacaoRequest(5, 1500.0);
                when(produtoService.reduzirEstoque(1L, 5, 1500.0))
                                .thenThrow(new IllegalArgumentException("Produto não encontrado"));

                mockMvc.perform(put("/api/produtos/1/saida")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isNotFound())
                                .andExpect(content().string("Produto não encontrado"));

                verify(produtoService, times(1)).reduzirEstoque(1L, 5, 1500.0);
        }

        @Test
        void testGetProdutosPorTipo_Sucesso() throws Exception {
                ProdutoPorTipoDTO dto = new ProdutoPorTipoDTO(1L, "Smartphone XYZ", "Smartphone de última geração",
                                "Eletrônico", 1000.0, 50, 0);
                List<ProdutoPorTipoDTO> dtos = Arrays.asList(dto);
                when(produtoService.getProdutosPorTipo(TipoProduto.ELETRONICO));


                mockMvc.perform(get("/api/produtos/tipo/Eletrônico")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Smartphone XYZ"))
                                .andExpect(jsonPath("$[0].descricao").value("Smartphone de última geração"))
                                .andExpect(jsonPath("$[0].quantidadeDisponivel").value(50))
                                .andExpect(jsonPath("$[0].quantidadeSaida").value(0));

                verify(produtoService, times(1)).getProdutosPorTipo(TipoProduto.ELETRONICO);
        }

        @Test
        void testGetProdutosPorTipo_TipoInvalido() throws Exception {
                mockMvc.perform(get("/api/produtos/tipo/Invalido")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(
                                                "Tipo de produto inválido. Valores permitidos: [Eletrônico, Eletrodoméstico, Móvel]"));

                verify(produtoService, never()).getProdutosPorTipo(any(TipoProduto.class));

        }

        @Test
        void testCalcularLucroPorProduto_Sucesso() throws Exception {
                LucroDTO lucro = new LucroDTO(2500.0, 5);
                when(produtoService.calcularLucroPorProduto(1L)).thenReturn(lucro);

                mockMvc.perform(get("/api/produtos/lucro/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.lucroTotal").value(2500.0))
                                .andExpect(jsonPath("$.quantidadeTotalSaida").value(5));

                verify(produtoService, times(1)).calcularLucroPorProduto(1L);
        }

        @Test
        void testCalcularLucroPorProduto_NaoEncontrado() throws Exception {
                when(produtoService.calcularLucroPorProduto(1L))
                                .thenThrow(new IllegalArgumentException("Produto não encontrado"));

                mockMvc.perform(get("/api/produtos/lucro/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(produtoService, times(1)).calcularLucroPorProduto(1L);
        }

        @Test
        void testListarMovimentacoesPorProduto_Sucesso() throws Exception {
                when(produtoService.buscarProdutoPorCodigo(1L)).thenReturn(Optional.of(produto));
                MovimentoEstoque movimento = new MovimentoEstoque();
                movimento.setId(1L);
                movimento.setProdutoId(1L);
                movimento.setTipoMovimentacao("Saida");
                movimento.setQuantidadeMovimentada(5);
                movimento.setValorVenda(1500.0);
                movimento.setDataVenda(LocalDate.now());
                List<MovimentoEstoque> movimentacoes = Arrays.asList(movimento);
                when(movimentoEstoqueRepository.findByProdutoId(1L)).thenReturn(movimentacoes);

                mockMvc.perform(get("/api/produtos/1/movimentacoes")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].tipoMovimentacao").value("Saida"))
                                .andExpect(jsonPath("$[0].quantidadeMovimentada").value(5));

                verify(produtoService, times(1)).buscarProdutoPorCodigo(1L);
                verify(movimentoEstoqueRepository, times(1)).findByProdutoId(1L);
        }

        @Test
        void testListarMovimentacoesPorProduto_NaoEncontrado() throws Exception {
                when(produtoService.buscarProdutoPorCodigo(1L)).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/produtos/1/movimentacoes")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());

                verify(produtoService, times(1)).buscarProdutoPorCodigo(1L);
                verify(movimentoEstoqueRepository, never()).findByProdutoId(anyLong());
        }
}