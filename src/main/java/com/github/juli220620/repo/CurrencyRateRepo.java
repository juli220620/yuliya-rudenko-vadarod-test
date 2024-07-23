package com.github.juli220620.repo;

import com.github.juli220620.model.CurrencyRateEntity;
import com.github.juli220620.model.id.CurrencyRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRateRepo extends JpaRepository<CurrencyRateEntity, CurrencyRateId> {
}
