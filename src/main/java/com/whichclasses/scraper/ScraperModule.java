package com.whichclasses.scraper;

import java.nio.file.FileSystems;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.whichclasses.ConfigManager;
import com.whichclasses.model.DataSource;
import com.whichclasses.scraper.cache.PageCache;
import com.whichclasses.scraper.cache.PermanentHtmlDiskCache;
import com.whichclasses.scraper.page.CacheableLazyLoadedPage;
import com.whichclasses.scraper.page.Page;
import com.whichclasses.scraper.page.TceClassPage;
import com.whichclasses.scraper.page.CoursePage;
import com.whichclasses.scraper.page.DepartmentPage;
import com.whichclasses.statistics.ConcreteAggregator;
import com.whichclasses.statistics.DataAggregator;

/**
 * Guice module for "production mode".
 */
public class ScraperModule extends AbstractModule {

  private static final String DEFAULT_DISK_CACHE_LOCATION = "/tmp/whichclasses";

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder()
        .implement(DepartmentPage.class, DepartmentPage.class)
        .build(DepartmentPage.DepartmentPageFactory.class));
    install(new FactoryModuleBuilder()
        .implement(CoursePage.class, CoursePage.class)
        .build(CoursePage.CoursePageFactory.class));
    install(new FactoryModuleBuilder()
        .implement(TceClassPage.class, TceClassPage.class)
        .build(TceClassPage.ClassPageFactory.class));

    // When using Scraper, it acts as a generic DataSource for TCE data.
    // This may need to be separated into its own optional module if there's ever a conflict.
    bind(Page.class).to(CacheableLazyLoadedPage.class);
    bind(DataSource.class).to(Scraper.class);
    bind(DataAggregator.class).to(ConcreteAggregator.class);
  }

  @Provides
  PageCache providesDiskCache(ConfigManager configManager) {
    return new PermanentHtmlDiskCache(FileSystems.getDefault().getPath(
        configManager.getConfigValue(
            ConfigManager.DISK_CACHE_LOCATION, DEFAULT_DISK_CACHE_LOCATION )));
  }

}
