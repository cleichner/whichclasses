package com.whichclasses.statistics;

import com.whichclasses.model.proto.TceRating;

public interface Rateable {
  public int getRatingCount(TceRating.Question question);
  public double getAverageScore(TceRating.Question question);
  public double getWilsonScore(TceRating.Question question);
}
