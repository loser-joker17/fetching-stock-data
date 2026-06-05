package com.example.defineEdge.repository;

import com.example.defineEdge.entity.CandleEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CandleRepository extends CassandraRepository<CandleEntity, String> {
    List<CandleEntity> findBySymbolAndDatetimeBetween(String symbol, LocalDateTime start, LocalDateTime end);
}
