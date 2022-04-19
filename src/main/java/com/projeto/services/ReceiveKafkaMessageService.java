package com.projeto.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.projeto.domain.Product;
import com.projeto.dto.ShopDTO;
import com.projeto.dto.ShopItemDTO;
import com.projeto.repositories.ProductRepository;

@Service
public class ReceiveKafkaMessageService {

	private static final Logger LOG = LoggerFactory.getLogger(ReceiveKafkaMessageService.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private KafkaTemplate<String, ShopDTO> kafkaTemplate;

	private static final String SHOP_TOPIC_NAME = "SHOP_TOPIC";

	private static final String SHOP_TOPIC_EVENT_NAME = "SHOP_TOPIC_EVENT";

	@KafkaListener(topics = SHOP_TOPIC_NAME, groupId = "group")
	public void listenShopTopic(ShopDTO shopDTO) {
		try {
			LOG.info("Compra recebida no tópico: {}.", shopDTO.getIdentifier());

			boolean success = true;
			for (ShopItemDTO item : shopDTO.getItems()) {
				Optional<Product> product = productRepository.findByIdentifier(item.getProductIdentifier());
				
				

				if (!isValidShop(item, product)) {
					shopError(shopDTO);
					success = false;
					break;
				}
				
				LOG.info("ID do produto:" + product.get().getIdentifier());

			}

			if (success) {
				shopSuccess(shopDTO);
			}
		} catch (Exception e) {
			LOG.error("Erro no processamento da compra {}", shopDTO.getIdentifier());
		}

	}

	private boolean isValidShop(ShopItemDTO item, Optional<Product> product) {

		return product.isPresent() && product.get().getAmount() >= item.getAmount();
	}

	// Envia uma mensagem para o Kafka indicando erro na compra
	private void shopError(ShopDTO shopDTO) {
		LOG.info("Erro no processamento da compra {}.", shopDTO.getIdentifier());
		shopDTO.setStatus("ERROR");
		kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
	}

	// Envia uma mensagem para o Kafka indicando sucesso na compra
	private void shopSuccess(ShopDTO shopDTO) {
		LOG.info("Compra {} efetuada com sucesso.", shopDTO.getIdentifier());
		shopDTO.setStatus("SUCCESS");
		kafkaTemplate.send(SHOP_TOPIC_EVENT_NAME, shopDTO);
	}

}




