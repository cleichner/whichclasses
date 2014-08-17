package com.whichclasses.scraper;

import org.jsoup.nodes.Document;

/**
 * Parent class for a page with the following behavior:
 * <ul>
 * <li>Not all data may be present after construction time. It may require more loading.</li>
 * <li>Backing HTML can be cached in a predetermined manner to avoid loading.</li>
 * </ul>
 * 
 * These are combined into a single class to maximize shared code. Multiple inheritance
 * in another language might be considered appropriate here otherwise.
 */
public abstract class CacheableLazyLoadedPage {

  private Document document;
  private AuthenticatedClient httpClient;
	
  CacheableLazyLoadedPage(AuthenticatedClient httpClient) {
    this.httpClient = httpClient;
  }

  /**
   * @return the fully-qualified URL required to load the given page.
   */
  abstract String getHtmlUrl();

  Document getDocument() {
    if (document == null) {
      document = httpClient.getPage(getHtmlUrl());
    }
    return document;
  }
}
