package com.example.pharmacy.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentDto {

	@JsonProperty("place_name")
	private String placeName;

	@JsonProperty("address_name")
	private String addressName;

	@JsonProperty("y")
	private double latitude;

	@JsonProperty("X")
	private double longitude;

	@JsonProperty("distance")
	private double distance;
}
