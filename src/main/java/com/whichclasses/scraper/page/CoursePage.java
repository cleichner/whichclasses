package com.whichclasses.scraper.page;

import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.model.Course;
import com.whichclasses.model.TceClass;
import com.whichclasses.model.proto.TceRating.Question;
import com.whichclasses.scraper.http.HttpUtils;
import com.whichclasses.scraper.page.TceClassPage.ClassPageFactory;
import com.whichclasses.statistics.RateableCollection;

/**
 * Represents the TCE page dealing with a single course (e.g. ACCT 200), but
 * not a single class (e.g. ACCT in Fall 2012 taught by Prof. Johnson).
 */
public class CoursePage implements Course, Page {
  private static final String COURSE_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/"
      + "GenerateReport.aspx?Report=DEPTONECLASS&crssub=%s&crsnum=%s";
  private final ClassPageFactory classPageFactory;
  private final Page page;
  private final String id;
  private final String title;
  private RateableCollection rateableCollection;
  private Map<String, TceClass> children;

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
      @Assisted("CourseTitle") String title,
      Page page) {
    this.classPageFactory = classPageFactory;
    this.id = id;
    this.title = title;
    this.page = page;
    this.page.setHtmlUrl(String.format(COURSE_PAGE_URL_BASE, departmentId.replaceAll("[\\s_]", ""), id));
    buildChildren();
    this.rateableCollection = new RateableCollection(this.getChildren().values());
  }

  private void buildChildren() {
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
    this.children = classes;
  }

  /**
   * @return map of unique class ids to ClassPage instances
   */
  @Override
  public Map<String, TceClass> getChildren() {
    return children;
  }

  @Override public String toString() {
    return new StringBuilder(id).append(" - ").append(title).toString();
  }

  @Override
  public int getRatingCount(Question question) {
    return rateableCollection.getRatingCount(question);
  }

  @Override
  public double getAverageScore(Question question) {
    return rateableCollection.getAverageScore(question);
  }

  @Override
  public double getWilsonScore(Question question) {
    return rateableCollection.getWilsonScore(question);
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
