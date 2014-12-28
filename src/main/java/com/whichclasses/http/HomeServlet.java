package com.whichclasses.http;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@SuppressWarnings("serial")
public class HomeServlet extends HttpServlet {

  private static final String HOME_TEMPLATE = "home.tpl";

  private Template template;

  @Inject public HomeServlet(Configuration configuration) {
    try {
      template = configuration.getTemplate(HOME_TEMPLATE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // TODO: extract to common TemplateServlet base class.
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("text/html");
    try {
      template.process(null, response.getWriter());
    } catch (TemplateException e) {
      throw new ServletException(e);
    }
  }

}
