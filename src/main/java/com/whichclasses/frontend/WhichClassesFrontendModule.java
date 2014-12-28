package com.whichclasses.frontend;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

public class WhichClassesFrontendModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Frontend.class).to(WhichClassesFrontend.class);
  }

  @Provides
  private Configuration provideFreemarkerConfiguration() throws Exception {
    Configuration cfg = new Configuration();
    cfg.setDirectoryForTemplateLoading(new java.io.File("src/main/template/"));
    cfg.setDefaultEncoding("UTF-8");
    // TODO: don't set this in production.
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
    return cfg;
  }
}
