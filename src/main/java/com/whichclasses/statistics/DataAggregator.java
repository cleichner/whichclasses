package com.whichclasses.statistics;

import java.util.List;

import com.whichclasses.model.Course;
import com.whichclasses.model.proto.TceRating.Question;

public interface DataAggregator {
  public List<Course> getRankedCourses(String deptId, Question question);
}
