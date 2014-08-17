package com.whichclasses.scraper;

import java.util.Map;
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
    Map<String, DepartmentPage> deptPages = deptListPage.getChildPages();
    DepartmentPage firstDepartment = deptPages.get("Accounting (ACCT)");
    System.out.println("Got department " + firstDepartment);
    Map<String, CoursePage> coursePages = firstDepartment.getChildPages();
    CoursePage firstCourse = coursePages.get(coursePages.keySet().iterator().next());
    System.out.println("Got course " + firstCourse);
    Map<String, ClassPage> classPages = firstCourse.getChildPages();
    ClassPage firstClass = classPages.get(classPages.keySet().iterator().next());
    System.out.println("Got class " + firstClass);
  }

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(
        new FactoryModuleBuilder()
            .implement(DepartmentPage.class, DepartmentPage.class)
            .build(DepartmentPage.DepartmentPageFactory.class),
        new FactoryModuleBuilder()
            .implement(CoursePage.class, CoursePage.class)
            .build(CoursePage.CoursePageFactory.class),
        new FactoryModuleBuilder()
            .implement(ClassPage.class, ClassPage.class)
            .build(ClassPage.ClassPageFactory.class));
    injector.getInstance(Scraper.class).runScrape();
  }
}
