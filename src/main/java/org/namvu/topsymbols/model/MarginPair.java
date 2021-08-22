package org.namvu.topsymbols.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class MarginPair {
  @Id
  private String symbol;
  private String base;
  private String quote;
  private boolean isMarginTrade;
  private boolean isBuyAllowed;
  private boolean isSellAllowed;

  @OneToMany(mappedBy = "marginPair", fetch= FetchType.LAZY)
  private List<Ticker24hr> ticker24hrList;

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getBase() {
    return base;
  }

  public void setBase(String base) {
    this.base = base;
  }

  public String getQuote() {
    return quote;
  }

  public void setQuote(String quote) {
    this.quote = quote;
  }

  public boolean isMarginTrade() {
    return isMarginTrade;
  }

  public void setMarginTrade(boolean marginTrade) {
    isMarginTrade = marginTrade;
  }

  public boolean isBuyAllowed() {
    return isBuyAllowed;
  }

  public void setBuyAllowed(boolean buyAllowed) {
    isBuyAllowed = buyAllowed;
  }

  public boolean isSellAllowed() {
    return isSellAllowed;
  }

  public void setSellAllowed(boolean sellAllowed) {
    isSellAllowed = sellAllowed;
  }

  public List<Ticker24hr> getTicker24hrList() {
    return ticker24hrList;
  }

  public void setTicker24hrList(List<Ticker24hr> ticker24hrList) {
    this.ticker24hrList = ticker24hrList;
  }
}
