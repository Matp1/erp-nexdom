package com.nexdom_matheus.erp_nexdom.repository;

import com.nexdom_matheus.erp_nexdom.model.Produto;
import com.nexdom_matheus.erp_nexdom.model.enums.TipoProduto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; 


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
   List<Produto> findByTipoProduto(TipoProduto tipoProduto);


    
}
