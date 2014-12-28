package com.whichclasses.frontend;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.google.inject.Inject;

public class WhichClassesFrontend implements Frontend {

  private final AllServletsHandler mServletHandler;

  @Inject public WhichClassesFrontend(AllServletsHandler servletHandler) {
    mServletHandler = servletHandler;
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

    // Servlets for handling all API calls and default templates.
    mServletHandler.setContextPath("/");
    orderedHandlers.add(mServletHandler);

    // Last: Serve a 404.
    DefaultHandler defaultHandler = new DefaultHandler();
    defaultHandler.setServeIcon(false);
    orderedHandlers.add(defaultHandler);

    handlerList.setHandlers(orderedHandlers.toArray(new Handler[orderedHandlers.size()]));

    server.start();
    server.join();
  }
}
