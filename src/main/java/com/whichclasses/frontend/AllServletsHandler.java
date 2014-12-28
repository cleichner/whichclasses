package com.whichclasses.frontend;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Inject;

public class AllServletsHandler extends ServletContextHandler {

  @Inject public AllServletsHandler(
      DeptListApiServlet deptListApiServlet,
      HomeServlet homeServlet) {
    addServlet(new ServletHolder(deptListApiServlet), "/api/departments");
    addServlet(new ServletHolder(homeServlet), "/");
  }
}
