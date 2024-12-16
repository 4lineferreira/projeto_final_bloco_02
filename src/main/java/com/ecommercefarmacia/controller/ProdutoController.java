package com.ecommercefarmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommercefarmacia.model.Produto;
import com.ecommercefarmacia.repository.CategoriaRepository;
import com.ecommercefarmacia.repository.ProdutoRespository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

	@Autowired
	private ProdutoRespository produtoRespository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRespository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) {
		return produtoRespository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRespository.findAllByNomeContainingIgnoreCase(nome));
	}

	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		if (categoriaRepository.existsById(produto.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRespository.save(produto));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Essa categoria não existe", null);
	}

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
		if (produtoRespository.existsById(produto.getId())) {

			if (categoriaRepository.existsById(produto.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK).body(produtoRespository.save(produto));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Essa categoria não existe", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Produto> produto = produtoRespository.findById(id);
		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		produtoRespository.deleteById(id);
		
	}
}
