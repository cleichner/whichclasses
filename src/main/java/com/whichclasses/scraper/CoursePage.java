package com.whichclasses.scraper;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.http.HttpUtils;
import com.whichclasses.scraper.ClassPage.ClassPageFactory;

/**
 * Represents the TCE page dealing with a single course (e.g. ACCT 200), but
 * not a single class (e.g. ACCT in Fall 2012 taught by Prof. Johnson).
 */
public class CoursePage extends CacheableLazyLoadedPage implements ContainerPage<ClassPage> {
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
  public Map<String, ClassPage> getChildPages() {
    Document document = getDocument();
    Elements courseLinks = document.select("#Tbl0 a[href]");
    Map<String, ClassPage> classPages = Maps.newHashMap();
    for (Element courseLink : courseLinks) {
      String crsId = HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "crsid");
      int trmCod = Integer.parseInt(
          HttpUtils.getFirstQueryParameter(courseLink.attr("href"), "trmcod"));
      if (trmCod > 0 && !Strings.isNullOrEmpty(crsId)) {
        classPages.put(crsId, classPageFactory.create(crsId, trmCod));
      }
    }

    return classPages;
  }

  @Override public String toString() {
    return new StringBuilder(id).append(" - ").append(title).toString();
  }

}
