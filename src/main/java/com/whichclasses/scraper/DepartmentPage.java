package com.whichclasses.scraper;

import org.jsoup.nodes.Document;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class DepartmentPage {
  private final AuthenticatedClient client;
  private final String identifier;
  private final String name;
  private Document document;

  public interface DepartmentPageFactory {
    DepartmentPage create(
        @Assisted("DepartmentIdentifier") String identifier,
        @Assisted("DepartmentName") String name);
  }

  @Inject
  public DepartmentPage(AuthenticatedClient client,
      @Assisted("DepartmentIdentifier") String identifier,
      @Assisted("DepartmentName") String name) {
    this.client = client;
    this.identifier = identifier;
    this.name = name;
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
