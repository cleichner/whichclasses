package com.whichclasses.scraper.cache;

import org.jsoup.nodes.Document;

/**
 * Generic key/value page cache for use by the scraper.
 */
public interface PageCache {
  public void store(String k, Document v);
  public Document get(String k);
}
