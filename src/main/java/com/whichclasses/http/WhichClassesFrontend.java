package com.whichclasses.http;

import org.eclipse.jetty.server.Server;

import com.google.inject.Inject;
import com.whichclasses.model.DataSource;

public class WhichClassesFrontend implements Frontend {

  private final DataSource mDataSource;

  @Inject public WhichClassesFrontend(DataSource dataSource) {
    mDataSource = dataSource;
  }

  @Override public void startServing() throws Exception {
    Server server = new Server(8080);
    server.start();
    server.join();
  }
}
