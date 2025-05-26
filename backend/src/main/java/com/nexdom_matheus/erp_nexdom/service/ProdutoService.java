package com.nexdom_matheus.erp_nexdom.service;

import com.nexdom_matheus.dto.LucroDTO;
import com.nexdom_matheus.dto.ProdutoPorTipoDTO;
import com.nexdom_matheus.erp_nexdom.model.MovimentoEstoque;
import com.nexdom_matheus.erp_nexdom.model.Produto;
import com.nexdom_matheus.erp_nexdom.model.enums.TipoProduto;
import com.nexdom_matheus.erp_nexdom.repository.ProdutoRepository;
import com.nexdom_matheus.erp_nexdom.repository.MovimentoEstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private MovimentoEstoqueRepository movimentoEstoqueRepository;

    // Método para buscar todos os produtos
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    // Método para buscar um produto pelo ID
    public Optional<Produto> buscarProdutoPorCodigo(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    // Método para criar um produto
    public Produto salvarProduto(Produto produto) {
        if (produto.getTipoProduto() == null) {
            throw new IllegalArgumentException(
                    "Tipo de produto inválido. Valores permitidos: " + Arrays.toString(TipoProduto.values()));
        }
        return produtoRepository.save(produto);
    }

    // Método para excluir um produto existente
    public void excluirProduto(Long codigo) {
        produtoRepository.deleteById(codigo);
    }

    // Método para atualizar um produto existente
    public Produto atualizarProduto(Long codigo, Produto produtoAtualizado) {
        Optional<Produto> produtoExistente = produtoRepository.findById(codigo);
        if (produtoExistente.isPresent()) {
            Produto produto = produtoExistente.get();
            produto.setName(produtoAtualizado.getName());
            produto.setDescricao(produtoAtualizado.getDescricao());
            produto.setTipoProduto(produtoAtualizado.getTipoProduto());
            produto.setValorFornecedor(produtoAtualizado.getValorFornecedor());
            produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
            return produtoRepository.save(produto);
        }
        return null;
    }

    // Entrada de produto (adicionar no estoque)
    @Transactional
    public Produto adicionarEstoque(Long codigo, int quantidade) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);

        // Registrar a movimentação de entrada
        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setProdutoId(codigo);
        movimento.setTipoMovimentacao("Entrada");
        movimento.setQuantidadeMovimentada(quantidade);
        movimento.setDataVenda(LocalDate.now());
        movimento.setValorVenda(0.0);
        movimentoEstoqueRepository.save(movimento);

        return produtoRepository.save(produto);
    }

    // Saída de produto (reduzir do estoque)
    @Transactional
    public Produto reduzirEstoque(Long codigo, int quantidade, double valorVenda) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        if (produto.getQuantidadeEstoque() < quantidade) {
            throw new IllegalArgumentException("Estoque insuficiente para a saída.");
        }
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - quantidade);

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setProdutoId(codigo);
        movimento.setTipoMovimentacao("Saida");
        movimento.setQuantidadeMovimentada(quantidade);
        movimento.setDataVenda(LocalDate.now());
        movimento.setValorVenda(valorVenda);
        movimentoEstoqueRepository.save(movimento);

        return produtoRepository.save(produto);
    }

    // Consulta de produtos por tipo
    public List<ProdutoPorTipoDTO> getProdutosPorTipo(TipoProduto tipo) {
        List<Produto> produtos = produtoRepository.findByTipoProduto(tipo);
        return produtos.stream().map(produto -> {
            List<MovimentoEstoque> saidas = movimentoEstoqueRepository
                    .findByProdutoIdAndTipoMovimentacao(produto.getCodigo(), "Saida");
            int quantidadeSaida = saidas.stream()
                    .mapToInt(MovimentoEstoque::getQuantidadeMovimentada)
                    .sum();
            return new ProdutoPorTipoDTO(
                    produto.getCodigo(),
                    produto.getName(),
                    produto.getDescricao(),
                    produto.getTipoProduto().name(),
                    produto.getValorFornecedor(),
                    produto.getQuantidadeEstoque(),
                    quantidadeSaida);
        }).collect(Collectors.toList());
    }

    // Calcular lucro por produto
    public LucroDTO calcularLucroPorProduto(Long codigo) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        List<MovimentoEstoque> saidas = movimentoEstoqueRepository.findByProdutoIdAndTipoMovimentacao(codigo, "Saida");
        double lucroTotal = saidas.stream()
                .mapToDouble(m -> (m.getValorVenda() - produto.getValorFornecedor()) * m.getQuantidadeMovimentada())
                .sum();
        int quantidadeTotalSaida = saidas.stream()
                .mapToInt(MovimentoEstoque::getQuantidadeMovimentada)
                .sum();
        return new LucroDTO(lucroTotal, quantidadeTotalSaida);
    }

   public int getTotalEntradas() {
    return movimentoEstoqueRepository.findAll().stream()
            .filter(m -> m.getTipoMovimentacao().equalsIgnoreCase("ENTRADA"))
            .mapToInt(MovimentoEstoque::getQuantidadeMovimentada)
            .sum();
}

public int getTotalSaidas() {
    return movimentoEstoqueRepository.findAll().stream()
            .filter(m -> m.getTipoMovimentacao().equalsIgnoreCase("SAIDA"))
            .mapToInt(MovimentoEstoque::getQuantidadeMovimentada)
            .sum();
}

public double getLucroTotal() {
    return movimentoEstoqueRepository.findAll().stream()
            .filter(m -> m.getTipoMovimentacao().equalsIgnoreCase("SAIDA"))
            .mapToDouble(m -> {
                Produto produto = produtoRepository.findById(m.getProdutoId())
                        .orElseThrow(() -> new RuntimeException("Produto não encontrado para ID " + m.getProdutoId()));

                return (m.getValorVenda() - produto.getValorFornecedor()) * m.getQuantidadeMovimentada();
            })
            .sum();
}



}