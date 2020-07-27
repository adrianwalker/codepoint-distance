package org.adrianwalker.codepointdistance.distance;

import org.adrianwalker.codepointdistance.model.CodePoint;

public interface DistanceAlgorithm {
  
  double calculate(CodePoint from, CodePoint to);
}
