package com.nexdom_matheus.erp_nexdom.service;

import com.nexdom_matheus.dto.ProdutoPorTipoDTO;
import com.nexdom_matheus.dto.LucroDTO;
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

    /**
     * Lista todos os produtos cadastrados.
     * @return Lista de produtos.
     */
    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    /**
     * Busca um produto pelo código.
     * @param codigo Código do produto.
     * @return Optional contendo o produto, ou vazio se não encontrado.
     */
    public Optional<Produto> buscarProdutoPorCodigo(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    /**
     * Salva um novo produto no banco de dados.
     * @param produto Produto a ser salvo.
     * @return Produto salvo.
     * @throws IllegalArgumentException Se o tipo de produto for inválido.
     */
    public Produto salvarProduto(Produto produto) {
        List<String> tiposPermitidos = Arrays.asList("Eletrônico", "Eletrodoméstico", "Móvel");
        if (!tiposPermitidos.contains(produto.getTipoProduto())) {
            throw new IllegalArgumentException("Tipo de produto inválido. Valores permitidos: " + tiposPermitidos);
        }
        return produtoRepository.save(produto);
    }

    /**
     * Exclui um produto pelo código.
     * @param codigo Código do produto.
     * @throws IllegalArgumentException Se o produto não for encontrado.
     */
    public void excluirProduto(Long codigo) {
        if (!produtoRepository.existsById(codigo)) {
            throw new IllegalArgumentException("Produto não encontrado");
        }
        produtoRepository.deleteById(codigo);
    }

    /**
     * Atualiza um produto existente.
     * @param codigo Código do produto a ser atualizado.
     * @param produtoAtualizado Dados atualizados do produto.
     * @return Produto atualizado.
     * @throws IllegalArgumentException Se o produto não for encontrado.
     */
    public Produto atualizarProduto(Long codigo, Produto produtoAtualizado) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produto.setName(produtoAtualizado.getName());
        produto.setDescricao(produtoAtualizado.getDescricao());
        produto.setTipoProduto(produtoAtualizado.getTipoProduto());
        produto.setValorFornecedor(produtoAtualizado.getValorFornecedor());
        produto.setQuantidadeEstoque(produtoAtualizado.getQuantidadeEstoque());
        return produtoRepository.save(produto);
    }

    /**
     * Adiciona uma quantidade ao estoque de um produto e registra uma movimentação de entrada.
     * @param codigo Código do produto.
     * @param quantidade Quantidade a ser adicionada.
     * @return Produto atualizado.
     * @throws IllegalArgumentException Se o produto não for encontrado.
     */
    @Transactional
    public Produto adicionarEstoque(Long codigo, int quantidade) {
        Produto produto = produtoRepository.findById(codigo)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + quantidade);

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setProdutoId(codigo);
        movimento.setTipoMovimentacao("Entrada");
        movimento.setQuantidadeMovimentada(quantidade);
        movimento.setDataVenda(LocalDate.now());
        movimento.setValorVenda(0.0);
        movimentoEstoqueRepository.save(movimento);

        return produtoRepository.save(produto);
    }

    /**
     * Reduz uma quantidade do estoque de um produto e registra uma movimentação de saída.
     * @param codigo Código do produto.
     * @param quantidade Quantidade a ser reduzida.
     * @param valorVenda Valor de venda da saída.
     * @return Produto atualizado.
     * @throws IllegalArgumentException Se o produto não for encontrado ou se o estoque for insuficiente.
     */
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

    /**
     * Consulta produtos por tipo, retornando quantidade disponível e de saída.
     * @param tipoProduto Tipo do produto (ex.: Eletrônico, Eletrodoméstico, Móvel).
     * @return Lista de produtos com informações adicionais.
     */
    public List<ProdutoPorTipoDTO> getProdutosPorTipo(TipoProduto tipoProduto) {
        List<Produto> produtos = produtoRepository.findByTipoProduto(tipoProduto);
        return produtos.stream().map(produto -> {
            List<MovimentoEstoque> saidas = movimentoEstoqueRepository.findByProdutoIdAndTipoMovimentacao(produto.getCodigo(), "Saida");
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
                    quantidadeSaida
            );
        }).collect(Collectors.toList());
    }

    /**
     * Calcula o lucro total e a quantidade total de saída de um produto.
     * @param codigo Código do produto.
     * @return DTO contendo o lucro total e a quantidade total de saída.
     * @throws IllegalArgumentException Se o produto não for encontrado.
     */
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
}