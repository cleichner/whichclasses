package com.whichclasses;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SystemPropertyConfigManager implements ConfigManager {
  private static final String PROPERTIES_FILENAME = "config.properties";
  private final Properties mProperties;

  @Inject public SystemPropertyConfigManager() throws IOException {
    mProperties = new Properties();
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
    if (inputStream == null) {
      throw new FileNotFoundException(
          "Properties file 'config.properties' not found. "
          + "Create one from config.properties.EXAMPLE in src/main/resources.");
    }
    mProperties.load(inputStream);
  }

  @Override public String getConfigValue(String key, String defaultValue) {
    try {
      return getConfigValue(key);
    } catch (IllegalArgumentException e) {
      return defaultValue;
    }
  }

  @Override public String getConfigValue(String key) {
    String value = mProperties.getProperty(key);
    if (value == null) {
      throw new IllegalArgumentException(
          "Could not find value in config.properties for key \"" + key + "\"");
    }
    return value;
  }

}
