package com.whichclasses.frontend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Serves the single frontend template. All page content is loaded via
 * Angular-based frontend.
 */
@SuppressWarnings("serial")
public class WebServlet extends HttpServlet {

  private static final String HOME_TEMPLATE = "home.tpl";

  private final Configuration mConfiguration;
  private Template mTemplate;

  @Inject public WebServlet(Configuration configuration) {
    mConfiguration = configuration;

    try {
      mTemplate = configuration.getTemplate(HOME_TEMPLATE);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("text/html");
    try {
      // TODO don't do this in prod. This is for rebuilding the template from disk.
      if (true) {
        mTemplate = mConfiguration.getTemplate(HOME_TEMPLATE);
      }

      mTemplate.process(null, response.getWriter());
    } catch (TemplateException e) {
      throw new ServletException(e);
    }
  }

}
