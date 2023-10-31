package com.example.pharmacy.direction.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.pharmacy.api.dto.DocumentDto;
import com.example.pharmacy.direction.entity.Direction;
import com.example.pharmacy.pharmacy.dto.PharmacyDto;
import com.example.pharmacy.pharmacy.service.PharmacySearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectionService {

	private static final int MAX_SEARCH_COUNT = 3;// 약국 최대 검색 갯수
	private static final double RADIUS_KM = 10.0;// 반경 10km

	private final PharmacySearchService pharmacySearchService;

	public List<Direction> buildDirectionList(DocumentDto documentDto) {

		if (Objects.isNull(documentDto)) {
			return Collections.emptyList();
		}

		// 약국 데이터 조회
		return pharmacySearchService.searchPharmacyDtoList()
			.stream()
			.map(pharmacyDto ->
				Direction.builder()
					.inputAddress(documentDto.getAddressName())
					.inputLatitude(documentDto.getLatitude())
					.inputLongitude(documentDto.getLongitude())
					.targetPharmacyName(pharmacyDto.getPharmacyName())
					.targetAddress(pharmacyDto.getPharmacyAddress())
					.targetLatitude(pharmacyDto.getLatitude())
					.targetLongitude(pharmacyDto.getLongitude())
					.distance(
						calculateDistance(documentDto.getLatitude(), documentDto.getLongitude(),
							pharmacyDto.getLatitude(), pharmacyDto.getLongitude()
							)
					)
					.build())
			.filter(direction -> direction.getDistance() <= RADIUS_KM)
			.sorted(Comparator.comparing(Direction::getDistance))
			.limit(MAX_SEARCH_COUNT)
			.collect(Collectors.toList());
	}



	// Haversine formula
	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		/*
		lat1 : 고객의 위도
		lon1 : 고객의 경도
		lat2 : 약국의 위도
		lon2 : 약국의 경도
		 */
		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);

		double earthRadius = 6371; //Kilometers
		return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
	}
}
