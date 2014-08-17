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

public class DepartmentPage extends CacheableLazyLoadedPage implements ContainerPage<CoursePage> {
  private static final String DEPARTMENT_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/GenerateReport.aspx?Report=DEPTCOURSE";
  private final CoursePageFactory coursePageFactory;
  private final String identifier;
  private final String name;

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
    super(client);
    this.coursePageFactory = coursePageFactory;
    this.identifier = identifier;
    this.name = name;
  }

  @Override
  String getHtmlUrl() {
    return DEPARTMENT_PAGE_URL_BASE + "&crssub=" + identifier;
  }

  /**
   * @return map of course titles (e.g. "ACCT 200A") to CoursePage instances
   */
  public Map<String, CoursePage> getChildPages() {
    Document document = getDocument();
    Elements courseLinks = document.select("#GV1 a[href]");
    Map<String, CoursePage> coursePages = Maps.newHashMap();
    for (Element courseLink : courseLinks) {
      String courseId = HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "crsnum");
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
