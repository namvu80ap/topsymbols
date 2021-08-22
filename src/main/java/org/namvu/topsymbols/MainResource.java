package org.namvu.topsymbols;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.search.MeterNotFoundException;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.jboss.logging.Logger;
import org.namvu.topsymbols.model.*;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;


@Path("/app")
public class MainResource {
  @Inject
  Logger log;

  @Inject
  MeterRegistry registry;

  @Inject
  TopSymbolServices topSymbolServices;

  private Vertx vertx;
  private WebClient clientTasks;

  @Inject
  public MainResource(Vertx vertx) {
    this.vertx = vertx;
    this.clientTasks = WebClient.create(vertx);
  }

  @GET
  @Path("/findAll")
  @Produces("application/json")
  public Uni<List<String>> getListMarginPair(){
    return Uni.createFrom().item(() -> topSymbolServices.findAllSymbol())
                    .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }

  @GET
  @Path("/topQuoteVolume")
  @Produces("application/json")
  public Uni<List<TopQuoteVolume>> findTopQuoteVolume(){
    return Uni.createFrom().item(() -> topSymbolServices.findTopQuoteVolume("BTC", 5))
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }

  @GET
  @Path("/topTradeCount")
  @Produces("application/json")
  public Uni<List<TopTradeCount>> findTopTradeCount(){
    return Uni.createFrom().item(() -> topSymbolServices.findTopTradeCount("USDT", 5))
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }

  @GET
  @Path("/topNotionalValue")
  @Produces("application/json")
  public Uni<List<TopNotionalValue>> getTopNotionalValue(){
    return Uni.createFrom().item(() -> topSymbolServices.getTopNotionalValue())
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }

  @GET
  @Path("/topTradePriceSpread")
  @Produces("application/json")
  public Uni<List<TopTradeCountPriceSpread>> getTopTradeCountPriceSpread(){
    return Uni.createFrom().item(() -> topSymbolServices.findTopTradeCountPriceSpread("USDT", 5))
      .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
  }


  private List<TopTradeCountPriceSpread> priceSpreadList = new ArrayList<>();

  @Scheduled(every = "{cron.expr.every_ten_seconds}")
  public void topSymbolMeter(){
      log.info("Scheduled topSymbolMeter run ... ");
      List<TopTradeCountPriceSpread> currentPriceSpreadList = topSymbolServices.findTopTradeCountPriceSpread("USDT", 5);
      if(priceSpreadList.size()==0) priceSpreadList = currentPriceSpreadList;

      for (int i = 0; i < currentPriceSpreadList.size(); i++) {
        priceSpreadList.get(i).priceSpread = currentPriceSpreadList.get(i).priceSpread;
      }

      priceSpreadList.forEach( priceSpread -> {
        try {
          registry.get("topsymbol.price.spread." + priceSpread.symbol).gauge();
        } catch ( MeterNotFoundException ex) {
          log.info("MeterNotFoundException : " + ex.getMessage());
          Gauge.builder("topsymbol.price.spread." + priceSpread.symbol, priceSpread,
              topPriceSpread -> Double.parseDouble(topPriceSpread.priceSpread))
            .register(registry);
        }
      });
  }

//  @GET
//  @Path("/{name}")
//  public String sayHello(@PathParam(value = "name") String name) {
//    registry.counter("greeting_counter", Tags.of("name", name)).increment();
//    return "Hello!";
//  }

}
