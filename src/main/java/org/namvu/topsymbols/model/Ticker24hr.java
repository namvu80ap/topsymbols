package org.namvu.topsymbols.model;

import javax.persistence.*;

@Entity
public class Ticker24hr {

  @Id
  private String symbol;
  private double priceChange;
  private double priceChangePercent;
  private double weightedAvgPrice;
  private double prevClosePrice;
  private double lastPrice;
  private double lastQty;
  private double bidPrice;
  private double askPrice;
  private double openPrice;
  private double highPrice;
  private double lowPrice;
  private double volume;
  private double quoteVolume;
  private long openTime;
  private long closeTime;
  private long firstId;
  private long lastId;
  private int count;

  @ManyToOne(fetch= FetchType.LAZY)
  @JoinColumn(name="symbol", insertable = false, updatable = false)
  private MarginPair marginPair;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public double getPriceChange() {
    return priceChange;
  }

  public void setPriceChange(double priceChange) {
    this.priceChange = priceChange;
  }

  public double getPriceChangePercent() {
    return priceChangePercent;
  }

  public void setPriceChangePercent(double priceChangePercent) {
    this.priceChangePercent = priceChangePercent;
  }

  public double getWeightedAvgPrice() {
    return weightedAvgPrice;
  }

  public void setWeightedAvgPrice(double weightedAvgPrice) {
    this.weightedAvgPrice = weightedAvgPrice;
  }

  public double getPrevClosePrice() {
    return prevClosePrice;
  }

  public void setPrevClosePrice(double prevClosePrice) {
    this.prevClosePrice = prevClosePrice;
  }

  public double getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(double lastPrice) {
    this.lastPrice = lastPrice;
  }

  public double getLastQty() {
    return lastQty;
  }

  public void setLastQty(double lastQty) {
    this.lastQty = lastQty;
  }

  public double getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(double bidPrice) {
    this.bidPrice = bidPrice;
  }

  public double getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(double askPrice) {
    this.askPrice = askPrice;
  }

  public double getOpenPrice() {
    return openPrice;
  }

  public void setOpenPrice(double openPrice) {
    this.openPrice = openPrice;
  }

  public double getHighPrice() {
    return highPrice;
  }

  public void setHighPrice(double highPrice) {
    this.highPrice = highPrice;
  }

  public double getLowPrice() {
    return lowPrice;
  }

  public void setLowPrice(double lowPrice) {
    this.lowPrice = lowPrice;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public double getQuoteVolume() {
    return quoteVolume;
  }

  public void setQuoteVolume(double quoteVolume) {
    this.quoteVolume = quoteVolume;
  }

  public long getOpenTime() {
    return openTime;
  }

  public void setOpenTime(long openTime) {
    this.openTime = openTime;
  }

  public long getCloseTime() {
    return closeTime;
  }

  public void setCloseTime(long closeTime) {
    this.closeTime = closeTime;
  }

  public long getFirstId() {
    return firstId;
  }

  public void setFirstId(long firstId) {
    this.firstId = firstId;
  }

  public long getLastId() {
    return lastId;
  }

  public void setLastId(long lastId) {
    this.lastId = lastId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public MarginPair getMarginPair() {
    return marginPair;
  }

  public void setMarginPair(MarginPair marginPair) {
    this.marginPair = marginPair;
  }
}
