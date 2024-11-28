package com.ivanprokic.sportticketconsumer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.ivanprokic.sportticketconsumer.entity.Sport;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {}
