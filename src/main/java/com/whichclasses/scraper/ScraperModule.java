package com.whichclasses.scraper;

import java.nio.file.FileSystems;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * Guice module for "production mode".
 */
public class ScraperModule extends AbstractModule {

  // TCE disk cache location.
  private static final String DISK_LOCATION = "/Users/jesse/data/whichclasses";

  @Override
  protected void configure() {
    install(new FactoryModuleBuilder()
        .implement(DepartmentPage.class, DepartmentPage.class)
        .build(DepartmentPage.DepartmentPageFactory.class));
    install(new FactoryModuleBuilder()
        .implement(CoursePage.class, CoursePage.class)
        .build(CoursePage.CoursePageFactory.class));
    install(new FactoryModuleBuilder()
        .implement(ClassPage.class, ClassPage.class)
        .build(ClassPage.ClassPageFactory.class));
  }

  @Provides
  PageCache providesDiskCache() {
    return new PermanentHtmlDiskCache(
        FileSystems.getDefault().getPath(DISK_LOCATION));
  }

}
