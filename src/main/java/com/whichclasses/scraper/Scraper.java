package com.whichclasses.scraper;

import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.whichclasses.model.Course;
import com.whichclasses.model.Department;
import com.whichclasses.model.DeptList;
import com.whichclasses.model.TceClass;
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

    for (Course oneCourse : courses.values()) {
      System.out.println("Got course " + oneCourse);
      Map<String, TceClass> classes = oneCourse.getChildren();
      for (TceClass oneClass : classes.values()) {
        System.out.println("Got class " + oneClass);
        System.out.println("Class: " + oneClass.getModel().toString());
      }
    }

  }

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(new ScraperModule());
    injector.getInstance(Scraper.class).runScrape();
  }
}
