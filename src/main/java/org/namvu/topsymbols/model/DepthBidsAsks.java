package org.namvu.topsymbols.model;

import java.util.List;

public class DepthBidsAsks {
  private long lastUpdateId;
  private String symbol;
  private List<double[]> bids;
  private List<double[]> asks;

  public long getLastUpdateId() {
    return lastUpdateId;
  }

  public void setLastUpdateId(long lastUpdateId) {
    this.lastUpdateId = lastUpdateId;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public List<double[]> getBids() {
    return bids;
  }

  public void setBids(List<double[]> bids) {
    this.bids = bids;
  }

  public List<double[]> getAsks() {
    return asks;
  }

  public void setAsks(List<double[]> asks) {
    this.asks = asks;
  }
}
