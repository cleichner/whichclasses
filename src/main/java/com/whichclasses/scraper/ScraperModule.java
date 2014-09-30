package com.whichclasses.scraper;

import java.nio.file.FileSystems;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.whichclasses.scraper.cache.PageCache;
import com.whichclasses.scraper.cache.PermanentHtmlDiskCache;
import com.whichclasses.scraper.page.TceClassPage;
import com.whichclasses.scraper.page.CoursePage;
import com.whichclasses.scraper.page.DepartmentPage;

/**
 * Guice module for "production mode".
 */
public class ScraperModule extends AbstractModule {

  // TCE disk cache location.
  // TODO(gunsch): allow specifying by system property
  private static final String DISK_LOCATION = "/Users/gunsch/data/whichclasses";

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
  }

  @Provides
  PageCache providesDiskCache() {
    return new PermanentHtmlDiskCache(
        FileSystems.getDefault().getPath(DISK_LOCATION));
  }

}
