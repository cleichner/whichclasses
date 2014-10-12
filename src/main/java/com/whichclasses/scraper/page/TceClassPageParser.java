package com.whichclasses.scraper.page;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.whichclasses.model.TceClassModel;
import com.whichclasses.model.TceCourseIdentifier;

public final class TceClassPageParser {

  @SuppressWarnings("serial")
  private static class ParserException extends RuntimeException {
    public ParserException(String message) { super(message); }
  }
  
  public static TceClassModel parseTceClassPage(Document page) {
    return new TceClassPageParser(page).buildModel();
  }

  private static final String METADATA_TABLE_ID = "tbl1";
  private static final String QUESTIONS_TABLE_ID = "main";

  // Note: space might be 0x20 or 0xa0
  private static final Pattern COURSE_ROW_PATTERN = Pattern.compile(
      "Course:" +
      "[\\u00a0\\s]+" +
      // Department ID
      "([A-Z]+)" +
      "[\\u00a0\\s]" +
      // Course ID
      "([a-zA-Z0-9]+)" +
      ".*?" +
      "Title:" +
      "[\\u00a0\\s]+" +
      "(.*?)" +
      // 2+ spaces signal end of title
      "[\\u00a0\\s][\\u00a0\\s]+" +
      ".*");
  private final Document page;
  private final Element metadataTable;
  private final Element questionsTable;
  
  private TceClassPageParser(Document page) {
    this.page = page;
    this.metadataTable = assertElement(page.getElementById(METADATA_TABLE_ID));
    this.questionsTable = assertElement(page.getElementById(QUESTIONS_TABLE_ID));
  }
  
  private TceClassModel buildModel() {
    // The entire course row is a jumble.
    String courseRowText = getElementTagNameContainingText(metadataTable, "tr", "Course:").text();
    Matcher courseRowMatcher = COURSE_ROW_PATTERN.matcher(courseRowText);
    if (!courseRowMatcher.matches()) {
      throw new ParserException("Could not match regex to text: \"" + courseRowText + "\"");
    }

    String department = courseRowMatcher.group(1);
    String courseNumber = courseRowMatcher.group(2);
    String courseTitle = courseRowMatcher.group(3);

    Element instructorRow = getElementTagNameContainingText(metadataTable, "tr", "Instructor:");
    String instructor = instructorRow.child(1).text().trim();

    Element termRow = getElementTagNameContainingText(metadataTable, "tr", "Term:");
    String termString = termRow.child(1).text().trim();

    // TODO: question parsing.

    return TceClassModel.newBuilder()
        .setIdentifier(TceCourseIdentifier.newBuilder()
            .setDepartment(department)
            .setCourseNumber(courseNumber))
        .setInstructor(instructor)
        .setTermCode(buildTermCode(termString))
        .setTitle(courseTitle)
        .build();
  }

  /**
   * "Spring-2009" --> 091
   * @param termString
   * @return
   */
  private String buildTermCode(String termString) {
    // TODO: parse this
    return termString;
  }

  private Element getElementTagNameContainingText(
      Element root, final String tagName, String text) {
    return assertOne(Collections2.filter(
        root.getElementsContainingText(text),
        new Predicate<Element>() {
          @Override
          public boolean apply(Element element) {
            return element.tagName().equals(tagName);
          }
        }));
  }

  private Element assertElement(Element element) {
    if (element == null) {
      throw new ParserException("Element not found.");
    }
    return element;
  }

  private Element assertOne(Collection<Element> elements) {
    if (elements.size() != 1) {
      throw new ParserException("Expected one element, found " + elements.size());
    }
    return elements.iterator().next();
  }

}
