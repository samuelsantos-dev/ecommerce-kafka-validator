package com.projeto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.projeto.domain.Product;

public interface ProductRepository extends JpaRepositoryImplementation<Product, Integer> {

	Optional<Product> findByIdentifier(String identifier);

}
