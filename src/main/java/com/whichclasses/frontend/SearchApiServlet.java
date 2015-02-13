package com.whichclasses.frontend;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.whichclasses.model.Course;
import com.whichclasses.model.DataSource;
import com.whichclasses.model.Department;
import com.whichclasses.model.TceClass;
import com.whichclasses.model.proto.TceRating.Question;
import com.whichclasses.statistics.DataAggregator;

/**
 * Handles ranking searches.
 */
@SuppressWarnings("serial")
public class SearchApiServlet extends HttpServlet {
  private final DataAggregator mDataAggregator;
  private final Gson mGson;

  @Inject public SearchApiServlet(DataAggregator dataAggregator, Gson gson) {
    this.mDataAggregator = dataAggregator;
    this.mGson = gson;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    String quality = request.getParameter("quality");
    String entityType = request.getParameter("entityType");
    String department = request.getParameter("department");
    
    Question question = Question.OVERALL_COURSE_RATING;
    switch(quality) {
    case "best":
      question = Question.OVERALL_COURSE_RATING;
      break;
    case "hardest":
      question = Question.COURSE_DIFFICULTY;
      break;
    }
    
    String json = "{\"results\":[";
    List<Course> courses = mDataAggregator.getRankedCourses(department, question);
    for(Course c : courses) {
    	String[] split = c.toString().split(" - ", 2);
    	String number = split[0];
    	String title = split[1];
    	// It works.
    	json += String.format("{\"identifier\":{\"department\":\"%s\",\"course_number\":\"%s\"},\"title\":\"%s\"},", department, number, title);
    }
    json = json.substring(0, json.length()-1) + "]}";
    response.getWriter().write(json);
  }

}
