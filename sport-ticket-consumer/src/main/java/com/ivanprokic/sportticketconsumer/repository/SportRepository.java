package com.ivanprokic.sportticketconsumer.repository;

import com.ivanprokic.sportticketconsumer.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {}
