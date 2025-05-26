package com.nexdom_matheus.erp_nexdom.controller;

import com.nexdom_matheus.dto.LucroDTO;
import com.nexdom_matheus.erp_nexdom.model.enums.TipoProduto;
import com.nexdom_matheus.dto.ProdutoRequestDTO;
import com.nexdom_matheus.dto.MovimentacaoRequest;
import com.nexdom_matheus.dto.ProdutoPorTipoDTO;
import com.nexdom_matheus.erp_nexdom.model.MovimentoEstoque;
import com.nexdom_matheus.erp_nexdom.model.Produto;
import com.nexdom_matheus.erp_nexdom.repository.MovimentoEstoqueRepository;
import com.nexdom_matheus.erp_nexdom.service.ProdutoService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "http://localhost:8081")
public class ProdutoController {

    @Autowired
    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Autowired
    private MovimentoEstoqueRepository movimentoEstoqueRepository;

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable Long codigo) {
        Optional<Produto> produto = produtoService.buscarProdutoPorCodigo(codigo);
        return produto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public Produto salvarProduto(@RequestBody @Valid ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setName(dto.getName());
        produto.setDescricao(dto.getDescricao());
        produto.setValorFornecedor(dto.getPreco());
        produto.setQuantidadeEstoque(dto.getQuantidadeEstoque());

        produto.setTipoProduto(TipoProduto.fromString(dto.getTipoProduto()));

        return produtoService.salvarProduto(produto);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long codigo) {
        try {
            produtoService.excluirProduto(codigo);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Long codigo, @RequestBody Produto produto) {
        try {
            Produto updatedProduto = produtoService.atualizarProduto(codigo, produto);
            return ResponseEntity.ok(updatedProduto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{codigo}/entrada")
    public ResponseEntity<?> adicionarEstoque(@PathVariable Long codigo, @RequestBody Map<String, Integer> request) {
        Integer quantidade = request.get("quantidade");
        if (quantidade == null || quantidade <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A quantidade deve ser maior que zero");
        }
        try {
            Produto produtoAtualizado = produtoService.adicionarEstoque(codigo, quantidade);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{codigo}/saida")
    public ResponseEntity<?> reduzirEstoque(@PathVariable Long codigo, @RequestBody MovimentacaoRequest request) {
        if (request.getQuantidade() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A quantidade deve ser maior que zero");
        }
        if (request.getValorVenda() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O valor de venda não pode ser negativo");
        }
        try {
            Produto produtoAtualizado = produtoService.reduzirEstoque(codigo, request.getQuantidade(),
                    request.getValorVenda());
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().equals("Produto não encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/tipo/{tipoProduto}")
    public ResponseEntity<?> getProdutosPorTipo(@PathVariable String tipoProduto) {
        try {
            TipoProduto tipo = TipoProduto.fromString(tipoProduto);
            List<ProdutoPorTipoDTO> produtos = produtoService.getProdutosPorTipo(tipo);
            return ResponseEntity.ok(produtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/lucro/{codigo}")
    public ResponseEntity<?> calcularLucroPorProduto(@PathVariable Long codigo) {
        try {
            LucroDTO lucro = produtoService.calcularLucroPorProduto(codigo);
            return ResponseEntity.ok(lucro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{codigo}/movimentacoes")
    public ResponseEntity<List<MovimentoEstoque>> listarMovimentacoesPorProduto(@PathVariable Long codigo) {
        Optional<Produto> produto = produtoService.buscarProdutoPorCodigo(codigo);
        if (produto.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<MovimentoEstoque> movimentacoes = movimentoEstoqueRepository.findByProdutoId(codigo);
        return ResponseEntity.ok(movimentacoes);
    }

    // Retorna o total de entradas
    @GetMapping("/entrada/total")
    public int getTotalEntradas() {
        return produtoService.getTotalEntradas();
    }

    // Retorna o total de saídas
    @GetMapping("/saida/total")
    public int getTotalSaidas() {
        return produtoService.getTotalSaidas();
    }

    // Retorna o lucro total
    @GetMapping("/lucro/total")
    public double getLucroTotal() {
        return produtoService.getLucroTotal();
    }

    // Endpoint para listar todas as movimentações
    @GetMapping("/todasmovimentacoes")
    public List<MovimentoEstoque> listarMovimentacoes() {
        return movimentoEstoqueRepository.findAll();
    }

}