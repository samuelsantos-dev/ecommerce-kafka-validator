package com.validator.repositories;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.validator.domain.Product;

public interface ProductRepository extends JpaRepositoryImplementation<Product, Integer> {

}
