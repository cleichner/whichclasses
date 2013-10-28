package com.whichclasses.scraper;

import java.util.Map;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.http.HttpUtils;
import com.whichclasses.scraper.CoursePage.CoursePageFactory;

public class DepartmentPage {
  private static final String DEPARTMENT_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/GenerateReport.aspx?Report=DEPTCOURSE";
  private final AuthenticatedClient client;
  private final CoursePageFactory coursePageFactory;
  private final String identifier;
  private final String name;
  private final String departmentPageUrl;
  private Document document;

  public interface DepartmentPageFactory {
    DepartmentPage create(
        @Assisted("DepartmentIdentifier") String identifier,
        @Assisted("DepartmentName") String name);
  }

  @Inject
  public DepartmentPage(AuthenticatedClient client,
      CoursePageFactory coursePageFactory,
      @Assisted("DepartmentIdentifier") String identifier,
      @Assisted("DepartmentName") String name) {
    this.client = client;
    this.coursePageFactory = coursePageFactory;
    this.identifier = identifier;
    this.name = name;
    this.departmentPageUrl = DEPARTMENT_PAGE_URL_BASE + "&crssub=" + identifier;
  }

  private Document getDocument() {
    if (document == null) {
      document = client.getPage(departmentPageUrl);
    }
    return document;
  }

  public Map<String, CoursePage> getCoursePages() {
    // TODO avoid dupes. Maybe change this to Map.<CourseId, CoursePage>.
    Document document = getDocument();
    Elements courseLinks = document.select("#GV1 a[href]");
    Map<String, CoursePage> coursePages = Maps.newHashMap();
    for (Element courseLink : courseLinks) {
      String courseId = HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "crssub");
      if (courseId != null && courseId.length() > 0) {
        String courseTitle = courseLink.text();
        coursePages.put(courseId, coursePageFactory.create(identifier, courseId, courseTitle));
      }
    }

    return coursePages;
  }

  @Override public String toString() {
    return new StringBuilder()
        .append("[")
        .append(identifier)
        .append("] - ")
        .append(name)
        .toString();
  }
}
