package com.whichclasses.scraper.page;

import org.jsoup.nodes.Document;

import com.google.inject.Inject;
import com.whichclasses.scraper.cache.PageCache;
import com.whichclasses.scraper.http.AuthenticatedClient;

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
public class CacheableLazyLoadedPage implements Page {

  // Field injection is the work of the devil.
  private AuthenticatedClient httpClient;
  private PageCache pageHtmlCache;
  private String htmlUrl;
  
  @Inject
  public CacheableLazyLoadedPage(AuthenticatedClient httpClient, PageCache pageHtmlCache) {
    this.httpClient = httpClient;
    this.pageHtmlCache = pageHtmlCache;
  }

  /**
   * @return the fully-qualified URL required to load the given page.
   */
  public String getHtmlUrl() {
    return this.htmlUrl;
  }
  
  public void setHtmlUrl(String htmlUrl) {
    this.htmlUrl = htmlUrl;
  }

  public Document getDocument() {
    String htmlUrl = getHtmlUrl();
    Document documentFromCache = pageHtmlCache.get(htmlUrl);
    if (documentFromCache != null) {
      return documentFromCache;
    }

    Document documentFromHttp = httpClient.getPage(getHtmlUrl());
    pageHtmlCache.store(htmlUrl, documentFromHttp);
    return documentFromHttp;
  }
}
