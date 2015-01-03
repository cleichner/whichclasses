package com.whichclasses;

import com.google.inject.ImplementedBy;

/**
 * Simple handler for getting environmental config values.
 */
@ImplementedBy(SystemPropertyConfigManager.class)
public interface ConfigManager {
  public final String DISK_CACHE_LOCATION = "diskCacheLocation";

  public final String WEBAUTH_USERNAME = "webauthUsername";
  public final String WEBAUTH_PASSWORD = "webauthPassword";

  /**
   * Gets a config value by key.
   * @param key
   * @return
   * @throws IllegalArgumentException If value for key is not found.
   */
  public String getConfigValue(String key);

  /**
   * Gets a config value by key, returning <pre>defaultValue</pre> if not found.
   * @param key
   * @return
   */
  public String getConfigValue(String key, String defaultValue);
}
