package com.whichclasses.model;

public interface Department extends Container<Course> {
  public String getShortName();
  public String getFullName();
}
