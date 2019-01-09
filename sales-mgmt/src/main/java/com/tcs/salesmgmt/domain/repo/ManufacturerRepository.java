package com.tcs.salesmgmt.domain.repo;

import org.springframework.data.repository.CrudRepository;
import com.tcs.salesmgmt.domain.model.ManufactureDetails;

public interface ManufacturerRepository extends CrudRepository<ManufactureDetails, Integer> {
	public ManufactureDetails findByManufacturerName(String manufacturer);
}
