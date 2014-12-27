package com.whichclasses.scraper.page;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.http.HttpUtils;
import com.whichclasses.model.Course;
import com.whichclasses.model.TceClass;
import com.whichclasses.scraper.page.TceClassPage.ClassPageFactory;

/**
 * Represents the TCE page dealing with a single course (e.g. ACCT 200), but
 * not a single class (e.g. ACCT in Fall 2012 taught by Prof. Johnson).
 */
public class CoursePage extends CacheableLazyLoadedPage implements Course {
  private static final String COURSE_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/"
      + "GenerateReport.aspx?Report=DEPTONECLASS&crssub=%s&crsnum=%s";
  private final ClassPageFactory classPageFactory;
  private final String id;
  private final String title;
  private final String coursePageUrl;

  public interface CoursePageFactory {
    CoursePage create(
        @Assisted("DepartmentIdentifier") String departmentId,
        @Assisted("CourseId") String id,
        @Assisted("CourseTitle") String title);
  }

  @Inject
  public CoursePage(
      ClassPageFactory classPageFactory,
      @Assisted("DepartmentIdentifier") String departmentId,
      @Assisted("CourseId") String id,
      @Assisted("CourseTitle") String title) {
    this.classPageFactory = classPageFactory;
    this.id = id;
    this.title = title;
    this.coursePageUrl = String.format(COURSE_PAGE_URL_BASE, departmentId, id);
  }

  @Override
  String getHtmlUrl() {
  	return coursePageUrl;
  }

  /**
   * @return map of unique class ids to ClassPage instances
   */
  @Override
  public Map<String, TceClass> getChildren() {
    Document document = getDocument();
    Elements courseLinks = document.select("#Tbl0 a[href]");
    Map<String, TceClass> classes = Maps.newHashMap();
    for (Element courseLink : courseLinks) {
      String crsId = HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "crsid");
      int trmCod = Integer.parseInt(
          HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "trmcod"));
      if (trmCod > 0 && !Strings.isNullOrEmpty(crsId)) {
        classes.put(crsId, classPageFactory.create(crsId, trmCod));
      }
    }

    return classes;
  }

  @Override public String toString() {
    return new StringBuilder(id).append(" - ").append(title).toString();
  }

}
