package com.whichclasses.scraper;

import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.whichclasses.scraper.page.ClassPage;
import com.whichclasses.scraper.page.CoursePage;
import com.whichclasses.scraper.page.DepartmentPage;
import com.whichclasses.scraper.page.DeptListPage;

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
    Injector injector = Guice.createInjector(new ScraperModule());
    injector.getInstance(Scraper.class).runScrape();
  }
}
