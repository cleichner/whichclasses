package com.whichclasses.scraper.page;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.model.Course;
import com.whichclasses.model.Department;
import com.whichclasses.scraper.http.HttpUtils;
import com.whichclasses.scraper.page.CoursePage.CoursePageFactory;

public class DepartmentPage implements Department, Page {
  private static final String DEPARTMENT_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/GenerateReport.aspx?"
      + "Report=DEPTCOURSE&crssub=%s";
  private final CoursePageFactory coursePageFactory;
  private final Page page;
  private final String identifier;
  private final String name;

  public interface DepartmentPageFactory {
    DepartmentPage create(
        @Assisted("DepartmentIdentifier") String identifier,
        @Assisted("DepartmentName") String name);
  }

  @Inject
  public DepartmentPage(
      CoursePageFactory coursePageFactory,
      @Assisted("DepartmentIdentifier") String identifier,
      @Assisted("DepartmentName") String name,
      Page page) {
    this.coursePageFactory = coursePageFactory;
    this.identifier = identifier;
    this.name = name;
    this.page = page;
    this.page.setHtmlUrl(String.format(DEPARTMENT_PAGE_URL_BASE, identifier));
  }

  @Override public String getShortName() { return identifier; }
  @Override public String getFullName() { return name; }

  /**
   * @return map of course titles (e.g. "ACCT 200A") to CoursePage instances
   */
  @Override public Map<String, Course> getChildren() {
    Document document = getDocument();
    Elements courseLinks = document.select("#GV1 a[href]");
    Map<String, Course> courses = Maps.newHashMap();
    for (Element courseLink : courseLinks) {
      String courseId = HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "crsnum");
      if (courseId != null && courseId.length() > 0) {
        String courseTitle = courseLink.text();
        courses.put(courseId, coursePageFactory.create(identifier, courseId, courseTitle));
      }
    }

    return courses;
  }

  @Override public String toString() {
    return new StringBuilder()
        .append("[")
        .append(identifier)
        .append("] - ")
        .append(name)
        .toString();
  }
  
  @Override
  public String getHtmlUrl() {
    return page.getHtmlUrl();
  }

  @Override
  public void setHtmlUrl(String htmlUrl) {
    page.setHtmlUrl(htmlUrl);
  }

  @Override
  public Document getDocument() {
    return page.getDocument();
  }
}
