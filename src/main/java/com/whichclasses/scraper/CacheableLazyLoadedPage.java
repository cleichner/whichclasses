package com.whichclasses.scraper;

import org.jsoup.nodes.Document;

import com.google.inject.Inject;

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

  // Being lazy by injecting these here. Oh well.
  @Inject private AuthenticatedClient httpClient;
  @Inject private PageCache pageHtmlCache;

  /**
   * @return the fully-qualified URL required to load the given page.
   */
  abstract String getHtmlUrl();

  Document getDocument() {
    // TODO(gunsch): this is probably really memory-excessive to hold onto
    // once model data is loaded. Consider a delegated Model class for each subclass.

    // Return in-memory instance if available.
    if (document != null)
      return document;

    String htmlUrl = getHtmlUrl();
    Document documentFromCache = pageHtmlCache.get(htmlUrl);
    if (documentFromCache != null) {
      document = documentFromCache;
      return document;
    }

    document = httpClient.getPage(getHtmlUrl());
    pageHtmlCache.store(htmlUrl, document);
    return document;
  }
}
