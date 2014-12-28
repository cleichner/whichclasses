package com.whichclasses.frontend;

/**
 * An Http Frontend for serving content.
 */
public interface Frontend {
  /**
   * Begin serving HTTP content.
   * This will tie up the main thread until exiting.
   */
  public void startServing() throws Exception;
}
