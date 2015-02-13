package com.whichclasses.model;

import java.util.List;

import com.whichclasses.model.proto.TceClassProto;
import com.whichclasses.model.proto.TceRating;
import com.whichclasses.model.proto.TceRating.ScoreCount;

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

  @Override
  public double getAverageScore(TceRating.Question question) {
    List<TceRating> ratings = mProto.getRatingList();
    for(TceRating rating : ratings) {
      if(rating.getQuestion() == question) {
        double qualityPoints = 0;
        int ratingCount = 0;
        for(ScoreCount count : rating.getRatingList()) {
          qualityPoints += (5 - count.getValueIndex()) * count.getCount();
          ratingCount += count.getCount();
        }
        return ratingCount != 0 ? qualityPoints / ratingCount : 0;
      }
    }
    return 0;
  }
  
  @Override
  public int getRatingCount(TceRating.Question question) {
    List<TceRating> ratings = mProto.getRatingList();
    for(TceRating rating : ratings) {
      if(rating.getQuestion() == question) {
        int ratingCount = 0;
        for(ScoreCount count : rating.getRatingList()) {
          ratingCount += count.getCount();
        }
        return ratingCount;
      }
    }
    return 0;
  }
  
  @Override
  public double getWilsonScore(TceRating.Question question) {
    double p = (getAverageScore(question) - 1) / 4;
    double n = getRatingCount(question);
    double z = 1.96;
    double wScoreNormalized = (p + z*z/(2*n) - z*Math.sqrt(p*(1-p)/n + z*z/(4*n*n)))/(1 + z*z/n);
    return wScoreNormalized * 4 + 1;
  }
}
