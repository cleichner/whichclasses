package com.whichclasses.scraper.page;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.whichclasses.model.proto.TceClassProto;
import com.whichclasses.model.proto.TceCourseIdentifier;
import com.whichclasses.model.proto.TceRating;
import com.whichclasses.model.proto.TceRating.Question;
import com.whichclasses.model.proto.TceRating.ScoreCount;

public final class TceClassPageParser {

  @SuppressWarnings("serial")
  private static class ParserException extends RuntimeException {
    public ParserException(String message) { super(message); }
    public ParserException(String message, Throwable e) { super(message, e); }
  }
  
  public static TceClassProto parseTceClassPage(Document page) {
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
  private final Element metadataTable;
  private final Element questionsTable;
  
  private TceClassPageParser(Document page) {
    this.metadataTable = assertElement(page.getElementById(METADATA_TABLE_ID));
    this.questionsTable = assertElement(page.getElementById(QUESTIONS_TABLE_ID));
  }

  private enum QuestionParsingState {
    STATE_INITIAL,
    STATE_EXPECTING_QUESTION,
    STATE_READING_RESULTS,
  }
  
  private TceClassProto buildModel() {
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
    String instructor = strip(instructorRow.child(1).text());

    Element termRow = getElementTagNameContainingText(metadataTable, "tr", "Term:");
    String termString = strip(termRow.child(1).text());

    // table --> tbody --> [tr]
    Elements questionRows = questionsTable.children().first().children();
    List<TceRating> questionResults = Lists.newArrayListWithCapacity(questionRows.size());

    QuestionParsingState state = QuestionParsingState.STATE_INITIAL;
    TceRating.Builder builder = null;
    int valueIndex = 0;
    for (Element questionRow : questionRows) {
      switch (state) {
        case STATE_INITIAL: {
          // Skip the first row (header).
          state = QuestionParsingState.STATE_EXPECTING_QUESTION;
          break;
        }

        case STATE_READING_RESULTS: {
          if (questionRow.children().size() == 1) {
            // This is a new question. Intentional fall-through to below.
          } else {
            if (questionRow.text().contains("omits")) {
              assertSize(2, questionRow.children());
              builder.setOmits(Integer.parseInt(strip(questionRow.child(1).text()), 10));
              state = QuestionParsingState.STATE_EXPECTING_QUESTION;
              break;
            }
            
            // Expect consistent format for results.
            assertSize(6, questionRow.children());

            // Fifth column should be a parseable number.
            try {
              builder.addRating(ScoreCount.newBuilder()
                  .setValueIndex(valueIndex)
                  .setCount(Integer.parseInt(strip(questionRow.child(4).text()), 10)));
              valueIndex++;
            } catch (NumberFormatException e) {
              throw new ParserException("Could not parse frequency", e);
            }

            break;
          }
        }

        case STATE_EXPECTING_QUESTION: {
          if (builder != null) {
            questionResults.add(builder.build());
            builder = null;
            state = QuestionParsingState.STATE_EXPECTING_QUESTION;
          }

          String questionText = questionRow.text();
          builder = TceRating.newBuilder()
              // TODO: which question is this? Different wordings map to
              // the same concept here. Translate from questionText, throw
              // if unrecognized.
              .setQuestion(Question.OVERALL_COURSE_RATING);
          state = QuestionParsingState.STATE_READING_RESULTS;
          valueIndex = 0;

          break;
        }

        default: {
          throw new IllegalStateException();
        }
      }
    }
    if (builder != null) {
      questionResults.add(builder.build());
    }

    return TceClassProto.newBuilder()
        .setIdentifier(TceCourseIdentifier.newBuilder()
            .setDepartment(department)
            .setCourseNumber(courseNumber))
        .setInstructor(instructor)
        .setTermCode(buildTermCode(termString))
        .setTitle(courseTitle)
        .addAllRating(questionResults)
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

  private void assertSize(int expected, Collection<Element> elements) {
    if (elements.size() != expected) {
      throw new ParserException("Expected exactly six cells per rating row, got "
          + elements.size());
    }
  }

  /**
   * Like trim, but removes all nbsp.
   * @param original
   * @return
   */
  private String strip(String original) {
    return original.replaceAll("[\\u00a0\\s]", " ").trim();
  }
}
