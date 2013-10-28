package com.whichclasses.scraper;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class CoursePage {

  //private final CourseSectionPageFactory courseSectionPageFactory;
  private final AuthenticatedClient client;
  private final String departmentId;
  private final String id;
  private final String title;

  public interface CoursePageFactory {
    CoursePage create(
        @Assisted("DepartmentIdentifier") String departmentId,
        @Assisted("CourseId") String id,
        @Assisted("CourseTitle") String title);
  }

  @Inject
  public CoursePage(
      AuthenticatedClient client,
      @Assisted("DepartmentIdentifier") String departmentId,
      @Assisted("CourseId") String id,
      @Assisted("CourseTitle") String title) {
    this.client = client;
    this.departmentId = departmentId;
    this.id = id;
    this.title = title;
  }

  @Override public String toString() {
    return new StringBuilder(id).append(" - ").append(title).toString();
  }
}
