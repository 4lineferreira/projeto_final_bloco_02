package com.ecommercefarmacia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ecommercefarmacia.model.Produto;

public interface ProdutoRespository extends JpaRepository <Produto, Long>{
	
	public List<Produto> findAllByNomeContainingIgnoreCase(@Param("nome")String nome);

}
