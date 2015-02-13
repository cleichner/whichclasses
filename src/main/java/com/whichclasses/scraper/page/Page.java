package com.whichclasses.scraper.page;

import org.jsoup.nodes.Document;

public interface Page {
  public String getHtmlUrl();
  public void setHtmlUrl(String htmlUrl);
  public Document getDocument();
}
