package com.whichclasses.scraper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Represents the TCE page dealing with a single instance of a course (e.g.
 * CSC 335 taught in Spring 2012 by Mercer).
 */
public class ClassPage {

  private static final String CLASS_PAGE_URL_BASE =
      "https://tce.oirps.arizona.edu/TCE_Student_Reports_CSS/GenerateReport.aspx?Report=ASUARep";
  private final AuthenticatedClient client;
  // TODO extract all of this to a separate model class.
  private final String crsId;
  private final int trmCod;

  public interface ClassPageFactory {
    ClassPage create(
        @Assisted("ClassId") String crsId,
        @Assisted("Semester") int trmCod);
  }

  @Inject
  public ClassPage(
      AuthenticatedClient client,
      @Assisted("ClassId") String crsId,
      @Assisted("Semester") int trmCod) {
    this.client = client;
    this.crsId = crsId;
    this.trmCod = trmCod;
  }

  @Override public String toString() {
    return "Class instance: " + crsId + ", taught in " + trmCod;
  }
}
