package org.namvu.topsymbols;

import io.vertx.core.json.Json;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import io.vertx.mutiny.ext.web.codec.BodyCodec;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.namvu.topsymbols.model.*;
import org.namvu.topsymbols.repos.MarginPairRepository;
import org.namvu.topsymbols.repos.Ticker24hrRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class TopSymbolServices {

    @Inject
    Logger log;

    @Inject
    Vertx vertx;

    @Inject
    MarginPairRepository marginPairRepository;

    @Inject
    Ticker24hrRepository ticker24hrRepository;

    @ConfigProperty(name = "binance.secret.apikey")
    String apiKey;

    @ConfigProperty(name = "binance.api.header.key")
    String headerKey;

    @ConfigProperty(name = "binance.api.depth")
    String apiDepth;

    @Transactional
    public void upsertAllMarginPair( List<MarginPair> marginPairList ){
      marginPairRepository.saveAll(marginPairList);
    }

    @Transactional
    public void upsertTicker24hr( List<Ticker24hr> ticker24hrList ){
      for ( Ticker24hr ticker24hr: ticker24hrList) {
        ticker24hrRepository.save(ticker24hr);
      }
    }

    public List<TopQuoteVolume> findTopQuoteVolume( String quoteAsset, int limit ){
      Pageable pageable = PageRequest.of(0,limit);
      List<Ticker24hr> list = ticker24hrRepository.findTopQuoteVolume(quoteAsset, pageable);
      List<TopQuoteVolume> topQuoteVolumeList = new ArrayList<TopQuoteVolume>();
      for (Ticker24hr ticker24hr : list) {
        TopQuoteVolume topQuoteVolum = new TopQuoteVolume();
        topQuoteVolum.symbol = ticker24hr.getSymbol();
        topQuoteVolum.quoteVolume = String.format("%.8f",ticker24hr.getQuoteVolume());
        topQuoteVolumeList.add(topQuoteVolum);
      }
      return topQuoteVolumeList;
    }

    public List<TopTradeCount> findTopTradeCount(String quoteAsset, int limit ){
      Pageable pageable = PageRequest.of(0,limit);
      List<Ticker24hr> list = ticker24hrRepository.findTopTradeCount(quoteAsset, pageable);
      List<TopTradeCount> topTradeCountList = new ArrayList<TopTradeCount>();
      for (Ticker24hr ticker24hr : list) {
        TopTradeCount topTradeCount = new TopTradeCount();
        topTradeCount.symbol = ticker24hr.getSymbol();
        topTradeCount.tradeNumber = String.format("%d",ticker24hr.getCount());
        topTradeCountList.add(topTradeCount);
      }
      return topTradeCountList;
    }

    public List<TopTradeCountPriceSpread> findTopTradeCountPriceSpread(String quoteAsset, int limit ){
      Pageable pageable = PageRequest.of(0,limit);
      List<Ticker24hr> list = ticker24hrRepository.findTopTradeCount(quoteAsset, pageable);
      List<TopTradeCountPriceSpread> topTradeCountPriceSpreads = new ArrayList<TopTradeCountPriceSpread>();
      for (Ticker24hr ticker24hr : list) {
        TopTradeCountPriceSpread topTradeCountPriceSpread = new TopTradeCountPriceSpread();
        topTradeCountPriceSpread.symbol = ticker24hr.getSymbol();
        topTradeCountPriceSpread.priceSpread = String.format("%.8f",ticker24hr.getPriceChange());
        topTradeCountPriceSpreads.add(topTradeCountPriceSpread);
      }
      return topTradeCountPriceSpreads;
    }

    public List<String> findAllSymbol(){
      return marginPairRepository.findAllSymbol();
    }

    public List<TopNotionalValue> getTopNotionalValue(){
      List<DepthBidsAsks> listDepth200 = getTop200Depth();
      List<TopNotionalValue> topNotionalValueList = new ArrayList<>();
      for (DepthBidsAsks bidsAsks: listDepth200) {

        double[] notionalValueBids = new double[200];
        double[] notionalValueAsks = new double[200];

        for (int i = 0; i < bidsAsks.getBids().size(); i++) {
          //We already know bids[0] is price and bids[1] is qty
          notionalValueBids[i] = bidsAsks.getBids().get(i)[0] * bidsAsks.getBids().get(i)[1] ;
          notionalValueAsks[i] = bidsAsks.getAsks().get(i)[0] * bidsAsks.getAsks().get(i)[1] ;
        }
        TopNotionalValue topNotionalValue = new TopNotionalValue();
        topNotionalValue.symbol = bidsAsks.getSymbol();
        topNotionalValue.notionalValueBids = String.format("%.8f",Arrays.stream(notionalValueBids).sum());
        topNotionalValue.notionalValueAsks = String.format("%.8f",Arrays.stream(notionalValueAsks).sum());
        topNotionalValueList.add(topNotionalValue);
      }
      return topNotionalValueList;
    }

    private List<DepthBidsAsks> getTop200Depth(){
      log.debug("GetTop200Depth running ...");
      WebClient webClient = WebClient.create(vertx);
      List<TopQuoteVolume> topQuoteVolumeList = findTopQuoteVolume("BTC",5);
      List<DepthBidsAsks> depthBidsAsksList = new ArrayList<>();
      for( TopQuoteVolume topSymbol : topQuoteVolumeList){
        CompletableFuture<DepthBidsAsks> depthBidsAsksCompletableFuture = webClient.getAbs(apiDepth)
          .putHeader(headerKey, apiKey)
          .addQueryParam("symbol", topSymbol.symbol)
          .addQueryParam("limit", "500") //TODO - Remove hardcode
          .as(BodyCodec.json(DepthBidsAsks.class))
          .send()
          .subscribeAsCompletionStage()
          .thenApply(body -> {
            if(body.statusCode() == Response.Status.OK.getStatusCode()){
              DepthBidsAsks item = body.body();
              item.getBids().subList(200, 500).clear();
              item.getAsks().subList(200, 500).clear();
              item.setSymbol(topSymbol.symbol);
              log.debugf("Bids size: %d  Asks size: %d", item.getBids().size(), item.getAsks().size());
              return item;
            } else {
              log.errorf("External API error: %d Message: %s", body.statusCode(), body.statusMessage());
              return new DepthBidsAsks();
            }
          });
        CompletableFuture.allOf(depthBidsAsksCompletableFuture).join();
        depthBidsAsksList.add(depthBidsAsksCompletableFuture.join());
      }
      return depthBidsAsksList;
    }

}
