package com.example.pharmacy.direction.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pharmacy.direction.entity.Direction;

public interface DirectionRepository extends JpaRepository<Direction, Long> {
}
