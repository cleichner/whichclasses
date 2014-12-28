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
 * Returns the list of all departments as JSON.
 */
@SuppressWarnings("serial")
public class DeptListApiServlet extends HttpServlet {

  // This can be served as a constant value.
  private final String deptListJson;

  @Inject public DeptListApiServlet(DataSource dataSource, Gson gson) {
    deptListJson = gson.toJson(dataSource.getDepartmentList().getChildren().values());
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("text/html");
    response.getWriter().write(deptListJson);
  }
}
