package com.whichclasses.scraper;

import org.jsoup.nodes.Document;

public class DepartmentPage {
  private final AuthenticatedClient client;
  private final String identifier;
  private final String name;
  private Document document;

  // Assisted injection plz?
  DepartmentPage(AuthenticatedClient client, String identifier, String name) {
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
