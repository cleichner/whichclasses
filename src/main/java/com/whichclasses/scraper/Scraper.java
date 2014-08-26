package com.whichclasses.scraper;

import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.whichclasses.scraper.page.DeptListPage;

public class Scraper {

  private final DeptList deptList;

  @Inject
  public Scraper(DeptListPage deptListPage) {
    this.deptList = deptListPage;
  }

  public void runScrape() {
    // For now: scrape one thing and build models for each.
    Map<String, Department> depts = deptList.getChildren();
    Department firstDepartment = depts.get("Accounting (ACCT)");
    System.out.println("Got department " + firstDepartment);
    Map<String, Course> courses = firstDepartment.getChildren();
    Course firstCourse = courses.get(courses.keySet().iterator().next());
    System.out.println("Got course " + firstCourse);
    Map<String, TceClass> classes = firstCourse.getChildren();
    TceClass firstClass = classes.get(classes.keySet().iterator().next());
    System.out.println("Got class " + firstClass);
  }

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(new ScraperModule());
    injector.getInstance(Scraper.class).runScrape();
  }
}
