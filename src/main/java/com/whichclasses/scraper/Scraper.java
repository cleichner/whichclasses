package com.whichclasses.scraper;

import java.util.List;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Scraper {

  private final DeptListPage deptListPage;

  @Inject
  public Scraper(DeptListPage deptListPage) {
    this.deptListPage = deptListPage;
  }

  public void runScrape() {
    // For now: scrape one thing and build models for each. 
    List<DepartmentPage> deptPages = deptListPage.getDepartmentPages();
    DepartmentPage firstDepartment = deptPages.get(0);
  }

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(
        new FactoryModuleBuilder()
            .implement(DepartmentPage.class, DepartmentPage.class)
            .build(DepartmentPage.DepartmentPageFactory.class));
    injector.getInstance(Scraper.class).runScrape();
  }
}
