package org.adrianwalker.codepointdistance.distance;

import org.adrianwalker.codepointdistance.model.CodePoint;

public enum PythagorasDistance implements DistanceAlgorithm {

  INSTANCE;

  @Override
  public double calculate(final CodePoint from, final CodePoint to) {

    long deltaEastings = to.getEastings() - from.getEastings();
    long deltaNorthings = to.getNorthings() - from.getNorthings();
    double distance = Math.sqrt(Math.pow(deltaEastings, 2) + Math.pow(deltaNorthings, 2));

    return distance;
  }
}
