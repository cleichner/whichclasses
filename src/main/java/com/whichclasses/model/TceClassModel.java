package com.whichclasses.model;

import com.whichclasses.model.proto.TceClassProto;

/**
 * Wrapper class over the TceClassProto details.
 *
 * This is intended for more intelligent operations not supported by
 * the proto itself, such as calculating confidence intervals for a given
 * value.
 */
public class TceClassModel implements TceClass {

  private final TceClassProto mProto;

  public TceClassModel(TceClassProto proto) {
    mProto = proto;
  }

  @Override public TceClassModel getModel() {
    return this;
  }

  @Override public String toString() {
    return mProto.toString();
  }
}
