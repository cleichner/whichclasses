package com.whichclasses.statistics;

import java.util.Comparator;

import com.whichclasses.model.Course;
import com.whichclasses.model.proto.TceRating.Question;

public class SimpleCourseComparator implements Comparator<Course> {
  
  private Question question;

  public SimpleCourseComparator(Question question) {
    this.question = question;
  }

  @Override
  public int compare(Course o1, Course o2) {
    double diff = o2.getAverageScore(question) - o1.getAverageScore(question);
    return (int) (diff < 0 ? Math.floor(diff) : Math.ceil(diff));
  }
  
  
}
