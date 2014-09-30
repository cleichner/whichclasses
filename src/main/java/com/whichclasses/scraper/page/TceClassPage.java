package com.whichclasses.scraper.page;

import org.jsoup.nodes.Document;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.model.TceClassModel;
import com.whichclasses.scraper.TceClass;

/**
 * Represents the TCE page dealing with a single instance of a course (e.g.
 * CSC 335 taught in Spring 2012 by Mercer).
 */
public class TceClassPage extends CacheableLazyLoadedPage implements TceClass {

  private static final String CLASS_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/GenerateReport.aspx?Report=ASUARep&"
      + "crsid=%s&trmcod=%s";

  private final String crsId;
  private final int trmCod;

  private TceClassModel model;
  
  public interface ClassPageFactory {
    TceClassPage create(
        @Assisted("ClassId") String crsId,
        @Assisted("Semester") int trmCod);
  }

  @Inject
  public TceClassPage(
      @Assisted("ClassId") String crsId,
      @Assisted("Semester") int trmCod) {
    this.crsId = crsId;
    this.trmCod = trmCod;
  }

  @Override String getHtmlUrl() {
    return String.format(CLASS_PAGE_URL_BASE, crsId, trmCod);
  }

  @Override public String toString() {
    return "Class instance: " + crsId + ", taught in " + trmCod;
  }

  @Override public String getTitle() {
    buildModelIfNecessary();
    return model.getTitle();
  }

  private void buildModelIfNecessary() {
    if (model != null) return;

    // TODO: parse page here
    Document page = getDocument();
    System.out.println(page.toString());
    
    model = TceClassModel.newBuilder()
        .setCourseId(crsId)
        .setTermCode(trmCod)
        .setTitle("Hello!")
        .build();
  }
}
