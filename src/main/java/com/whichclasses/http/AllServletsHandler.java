package com.whichclasses.http;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Inject;

public class AllServletsHandler extends ServletContextHandler {

  @Inject public AllServletsHandler(
      HomeServlet homeServlet,
      DummyServlet dummyServlet) {
    addServlet(new ServletHolder(dummyServlet), "/dummy");
    addServlet(new ServletHolder(homeServlet), "/");
  }
}
