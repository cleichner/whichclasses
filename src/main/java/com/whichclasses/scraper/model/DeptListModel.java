package com.whichclasses.scraper.model;

import java.util.Map;

import com.whichclasses.model.Department;
import com.whichclasses.model.DeptList;

public class DeptListModel implements DeptList {

  private Map<String, Department> children;

  @Override
  public Map<String, Department> getChildren() {
    return children;
  }

  public static class Builder {
    private DeptListModel mModel = new DeptListModel();
    public DeptListModel build() { return mModel; }
    public Builder setChildren(Map<String, Department> children) {
      mModel.children = children;
      return this;
    }
  }

}
