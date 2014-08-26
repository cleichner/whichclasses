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
public abstract class CacheableLazyLoadedPage {

  // Being lazy by injecting these here. Oh well.
  @Inject private AuthenticatedClient httpClient;
  @Inject private PageCache pageHtmlCache;

  /**
   * @return the fully-qualified URL required to load the given page.
   */
  abstract String getHtmlUrl();

  Document getDocument() {
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
