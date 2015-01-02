package com.whichclasses.frontend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.whichclasses.model.DataSource;

/**
 * Handles ranking searches.
 */
@SuppressWarnings("serial")
public class SearchApiServlet extends HttpServlet {
  private final DataSource mDataSource;
  private final Gson mGson;

  @Inject public SearchApiServlet(DataSource dataSource, Gson gson) {
    this.mDataSource = dataSource;
    this.mGson = gson;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    // TODO: actually implement this
    response.getWriter().write("{\"results\":[{\"identifier\":{\"department\":\"ECE\",\"course_number\":\"351A\"},\"title\":\"All The Circuits\"}]}");
  }

}
