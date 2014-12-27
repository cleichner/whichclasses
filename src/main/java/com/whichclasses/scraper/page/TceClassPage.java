package com.whichclasses.scraper.page;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.whichclasses.model.TceClass;
import com.whichclasses.model.TceClassModel;
import com.whichclasses.model.proto.TceClassProto;

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

  private TceClassProto model;
  
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

  @Override public TceClassProto getProto() {
    buildModelIfNecessary();
    return model;
  }

  @Override public TceClassModel getModel() {
    return null;
  }

  private void buildModelIfNecessary() {
    if (model != null) return;
    model = TceClassPageParser.parseTceClassPage(getDocument());
  }
}
