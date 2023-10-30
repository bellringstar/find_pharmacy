package com.example.pharmacy.pharmacy.service;

import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.pharmacy.pharmacy.entity.Pharmacy;
import com.example.pharmacy.pharmacy.repository.PharmacyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyRepositoryService {
	private final PharmacyRepository pharmacyRepository;

	@Transactional
	public void updateAddress(Long id, String address) {
		Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

		if (Objects.isNull(entity)) {
			log.error("[PharmacyRepositoryService updateAddress] not found id : {}", id);
			return;
		}

		entity.changePharmacyAddress(address);
	}

	//dirty checking test
	public void updateAddressWithoutTransaction(Long id, String address) {
		Pharmacy entity = pharmacyRepository.findById(id).orElse(null);

		if (Objects.isNull(entity)) {
			log.error("[PharmacyRepositoryService updateAddress] not found id : {}", id);
			return;
		}

		entity.changePharmacyAddress(address);
	}

}
