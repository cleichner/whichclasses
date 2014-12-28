package com.whichclasses.http;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.google.inject.Inject;
import com.whichclasses.model.DataSource;

public class WhichClassesFrontend implements Frontend {

  private final DataSource mDataSource;

  @Inject public WhichClassesFrontend(DataSource dataSource) {
    mDataSource = dataSource;
  }

  @Override public void startServing() throws Exception {
    Server server = new Server(8080);

    List<Handler> orderedHandlers = new ArrayList<>();

    HandlerList handlerList = new HandlerList();
    server.setHandler(handlerList);

    // Handle static resources first at /static.
    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(false);
    resourceHandler.setResourceBase("src/main/static/");
    ContextHandler resourceContextHandler = new ContextHandler();
    resourceContextHandler.setContextPath("/static");
    resourceContextHandler.setHandler(resourceHandler);
    orderedHandlers.add(resourceContextHandler);

    // Servlets for handling all API calls.
    ServletContextHandler servletHandler = new ServletContextHandler();
    servletHandler.setContextPath("/");
    servletHandler.addServlet(new ServletHolder(new DummyServlet()), "/dummy");
    orderedHandlers.add(servletHandler);

    // Last: Serve a 404.
    DefaultHandler defaultHandler = new DefaultHandler();
    defaultHandler.setServeIcon(false);
    orderedHandlers.add(defaultHandler);

    handlerList.setHandlers(orderedHandlers.toArray(new Handler[orderedHandlers.size()]));

    server.start();
    server.join();
  }
}
