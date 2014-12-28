package com.whichclasses.http;

import com.google.inject.AbstractModule;

public class WhichClassesServerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Frontend.class).to(WhichClassesFrontend.class);
  }

}
