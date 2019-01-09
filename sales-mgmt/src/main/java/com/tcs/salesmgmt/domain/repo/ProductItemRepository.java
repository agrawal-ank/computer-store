package com.tcs.salesmgmt.domain.repo;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import com.tcs.salesmgmt.domain.model.ProductType;

@Transactional
public interface ProductItemRepository extends CrudRepository<ProductType, Integer> {
	public ProductType findByProductType(String productType);
}
