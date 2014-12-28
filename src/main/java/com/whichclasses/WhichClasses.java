package com.whichclasses;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.whichclasses.http.Frontend;
import com.whichclasses.http.WhichClassesServerModule;
import com.whichclasses.scraper.Scraper;
import com.whichclasses.scraper.ScraperModule;

public class WhichClasses {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(
        new ScraperModule(),
        new WhichClassesServerModule());
    injector.getInstance(Scraper.class).runScrape();
    injector.getInstance(Frontend.class).startServing();
  }

}
