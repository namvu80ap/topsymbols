package org.namvu.topsymbols;

import io.quarkus.scheduler.Scheduled;
import io.vertx.core.json.JsonArray;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.namvu.topsymbols.model.MarginPair;
import org.namvu.topsymbols.model.Ticker24hr;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PeriodicTasks {

  @Inject
  Logger log;

  private WebClient clientTasks;

  @Inject
  public PeriodicTasks(Vertx vertx) {
    this.clientTasks = WebClient.create(vertx);
  }

  @Inject
  TopSymbolServices topSymbolServices;

  @ConfigProperty(name = "binance.secret.apikey")
  String apiKey;

  @ConfigProperty(name = "binance.api.header.key")
  String headerKey;

  @ConfigProperty(name = "binance.api.margin.allpair")
  String apiMarginAllPair;

  @ConfigProperty(name = "binance.api.ticker.24hr")
  String apiTicker24hr;

  @Scheduled(every="{cron.expr.every_five_seconds}")
  void allMarginPair(){
    log.debug("PeriodicTask allMarginPair running ...");
    List<MarginPair> marginPairList = clientTasks.getAbs(apiMarginAllPair)
      .putHeader(headerKey,apiKey)
      .as(BodyCodec.jsonArray())
      .send()
      .subscribeAsCompletionStage()
      .thenApply( body -> {
        ArrayList<MarginPair> list = new ArrayList<MarginPair>();
        JsonArray jsonArray = body.body();
        log.debugf("AllPair jsonArray size: %d", jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
          MarginPair marginPair = jsonArray.getJsonObject(i).mapTo(MarginPair.class);
          if( "BTC".equalsIgnoreCase(marginPair.getQuote()) ||
              "USDT".equalsIgnoreCase(marginPair.getQuote())) {
            list.add(marginPair);
          }
        }
        return list;
      }).join();
    log.debugf("AllPair list size: %d", marginPairList.size());
    topSymbolServices.upsertAllMarginPair(marginPairList);
  }

  @Scheduled(every="{cron.expr.every_five_seconds}")
  void price24Hour(){
    log.debug("PeriodicTask price24Hour running ...");
    List<String> symbols = topSymbolServices.findAllSymbol();
    List<Ticker24hr> ticker24hrList = clientTasks.getAbs(apiTicker24hr)
      .putHeader(headerKey,apiKey)
      .as(BodyCodec.jsonArray())
      .send()
      .subscribeAsCompletionStage()
      .thenApply( body -> {
        ArrayList<Ticker24hr> list = new ArrayList<Ticker24hr>();
        JsonArray jsonArray = body.body();
        for (int i = 0; i < jsonArray.size(); i++) {
          Ticker24hr ticker24hr = jsonArray.getJsonObject(i).mapTo(Ticker24hr.class);
          if( symbols.contains(ticker24hr.getSymbol()) )
            list.add(ticker24hr);
        }
        return list;
      }).join(); //TODO - Handler Exception
    log.debugf("Ticker24hr list size: %d", ticker24hrList.size());
    topSymbolServices.upsertTicker24hr(ticker24hrList);
  }

}
