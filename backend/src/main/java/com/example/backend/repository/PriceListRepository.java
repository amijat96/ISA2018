package com.example.backend.repository;

import com.example.backend.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Integer> {
}
