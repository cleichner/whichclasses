package com.whichclasses.model;

import com.whichclasses.model.proto.TceClassProto;

public interface TceClass {
  public TceClassProto getProto();
  public TceClassModel getModel();
}
