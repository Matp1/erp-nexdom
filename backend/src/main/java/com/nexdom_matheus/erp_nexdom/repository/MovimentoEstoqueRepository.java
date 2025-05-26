package com.nexdom_matheus.erp_nexdom.repository;

import com.nexdom_matheus.erp_nexdom.model.MovimentoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {
    List<MovimentoEstoque> findByProdutoIdAndTipoMovimentacao(Long produtoId, String tipoMovimentacao);
    List<MovimentoEstoque> findByProdutoId(Long produtoId);
}
