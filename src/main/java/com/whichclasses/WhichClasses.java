package com.whichclasses;

import org.eclipse.jetty.server.Server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.whichclasses.scraper.Scraper;
import com.whichclasses.scraper.ScraperModule;

public class WhichClasses {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(new ScraperModule());
    injector.getInstance(Scraper.class).runScrape();

    // TODO: Guice this up
    Server server = new Server(8080);
    server.start();
    server.join();
  }

}
