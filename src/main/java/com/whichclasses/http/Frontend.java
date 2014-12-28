package com.whichclasses.http;

/**
 * An Http Frontend for serving content.
 */
public interface Frontend {
  /**
   * Begin serving HTTP content.
   */
  public void startServing() throws Exception;
}
