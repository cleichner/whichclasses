package com.whichclasses.model;

/**
 * A source-agnostic method for returning TCE data.
 */
public interface DataSource {

  /**
   * @return All TCE departments.
   */
  public DeptList getDepartmentList();
}
