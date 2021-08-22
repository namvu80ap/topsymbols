package org.namvu.topsymbols.repos;

import org.namvu.topsymbols.model.Ticker24hr;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface Ticker24hrRepository extends CrudRepository<Ticker24hr, String> {

  @Query("SELECT t FROM Ticker24hr t inner join t.marginPair mg WHERE mg.quote = :quoteAsset ORDER BY t.quoteVolume DESC")
  List<Ticker24hr> findTopQuoteVolume(@Param("quoteAsset") String quoteAsset, Pageable pageable);

  @Query("SELECT t FROM Ticker24hr t inner join t.marginPair mg WHERE mg.quote = :quoteAsset ORDER BY t.count DESC")
  List<Ticker24hr> findTopTradeCount(@Param("quoteAsset") String quoteAsset, Pageable pageable);

}
