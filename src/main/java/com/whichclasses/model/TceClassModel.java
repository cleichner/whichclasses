package com.whichclasses.model;

import com.whichclasses.model.proto.TceClassProto;

public class TceClassModel implements TceClass {

  private final TceClassProto mProto;

  public TceClassModel(TceClassProto proto) {
    mProto = proto;
  }
  
  @Override
  public TceClassProto getProto() {
    return mProto;
  }

  @Override
  public TceClassModel getModel() {
    return this;
  }

}
