package org.namvu.topsymbols.repos;

import org.namvu.topsymbols.model.MarginPair;
import org.namvu.topsymbols.model.Ticker24hr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

//TODO - Change me to use reactive
public interface MarginPairRepository extends CrudRepository<MarginPair, Long> {
  @Query("SELECT t.symbol FROM MarginPair t")
  List<String> findAllSymbol();
}
