package com.projeto.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto.domain.Product;
import com.projeto.repositories.ProductRepository;

@Service
public class DBService {

	@Autowired
	private ProductRepository rep;

	public void initDatabase() {
		Product product = new Product(null, "123456789", 100);
		Product product2 = new Product(null, "987654321", 200);

		rep.saveAll(Arrays.asList(product, product2));
		
		

	}
}
