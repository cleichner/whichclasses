package com.whichclasses.statistics;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.whichclasses.model.proto.TceRating;
import com.whichclasses.model.proto.TceRating.Question;

public class RateableCollection implements Rateable {
  
  private Collection<? extends Rateable> children;
  private Map<Question, Double> questionToScore = Maps.newHashMap();
  private Map<Question, Double> questionToWilsonScore = Maps.newHashMap();
  private Map<Question, Integer> questionToNumRatings = Maps.newHashMap();
  
  public RateableCollection( Collection<? extends Rateable> children ) {
    this.children = children;
    calculateStatistics();
  }
  
  private void calculateStatistics() {
    for(Question question : Question.values()) {
      double qualityPoints = 0;
      int numRatings = 0;
      for(Rateable rateable : children) {
        qualityPoints += rateable.getAverageScore(question) * rateable.getRatingCount(question);
        numRatings += rateable.getRatingCount(question);
      }
      double averageScore = numRatings != 0 ? qualityPoints / numRatings : 0;
      double wilsonScore = calculateWilsonScore((averageScore - 1) / 4, numRatings) * 4 + 1;
      questionToScore.put(question, averageScore);
      questionToWilsonScore.put(question, wilsonScore);
      questionToNumRatings.put(question, numRatings);
    }
  }

  @Override
  public double getAverageScore(TceRating.Question question) {
    return questionToScore.get(question);
  }
  
  @Override
  public int getRatingCount(TceRating.Question question) {
    return questionToNumRatings.get(question);
  }
  
  @Override
  public double getWilsonScore(TceRating.Question question) {
    return questionToWilsonScore.get(question);
  }
  
  /**
   * Calculates the lower bound of the 95% confidence interval of a normalized data set of data
   * @param p - Normalized average of sample data set
   * @param n - Number of samples
   * @return The normalized Wilson score of a normalized data set
   */
  private double calculateWilsonScore(double p, int n) {
    if(n == 0) return 0;
    double z = 1.96;
    return (p + z*z/(2*n) - z*Math.sqrt(p*(1-p)/n + z*z/(4*n*n)))/(1 + z*z/n);
  }

}
