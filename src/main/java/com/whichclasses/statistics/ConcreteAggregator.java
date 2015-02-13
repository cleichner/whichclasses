package com.whichclasses.statistics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.whichclasses.model.Course;
import com.whichclasses.model.DataSource;
import com.whichclasses.model.Department;
import com.whichclasses.model.proto.TceRating.Question;

public class ConcreteAggregator implements DataAggregator {
  
  DataSource dataSource;
  Map<String, Map<Question, Double>> courseScoreDataMap = Maps.newHashMap();
  
  @Inject
  public ConcreteAggregator(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public List<Course> getRankedCourses(String deptId, Question question) {
    Department dept = dataSource.getDepartmentList().getChildren().get(deptId);
    Collection<Course> courses = dept.getChildren().values();
    List<Course> courseList = new ArrayList<Course>(courses);
    Collections.sort(courseList, new SimpleCourseComparator(question));
    return courseList;
  }

}
