package com.whichclasses.scraper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class Scraper {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(
        new FactoryModuleBuilder()
            .implement(DepartmentPage.class, DepartmentPage.class)
            .build(DepartmentPage.DepartmentPageFactory.class));
    DeptListPage page = injector.getInstance(DeptListPage.class);
    System.out.println(page.getDepartmentPages());
  }

}
