/* (C)2024 */
package com.nbmp.waveform.model.utils;

import org.apache.commons.math3.stat.StatUtils;

import com.github.psambit9791.jdsp.misc.UtilMethods;

public class WaveStatUtils extends UtilMethods {
  public static double[] zScoreNormalize(double[] amplitudeArray) {
    double[] normalizedAmplitude = new double[amplitudeArray.length];
    double mean = StatUtils.mean(amplitudeArray);
    double stdDev = findStandardDeviation(amplitudeArray);
    for (int i = 0; i < amplitudeArray.length; i++) {
      normalizedAmplitude[i] = (amplitudeArray[i] - mean) / stdDev;
    }
    return normalizedAmplitude;
  }

  public static double[] oneToOneNormalize(double[] amplitudeArray) {
    double[] normalizedAmplitude = new double[amplitudeArray.length];
    double min = StatUtils.min(amplitudeArray);
    double max = StatUtils.max(amplitudeArray);
    double maxAbs = Math.max(Math.abs(min), Math.abs(max));
    for (int i = 0; i < amplitudeArray.length; i++) {
      normalizedAmplitude[i] = amplitudeArray[i] / maxAbs;
    }
    return normalizedAmplitude;
  }

  public static double findStandardDeviation(double[] amplitudeArray) {
    double mean = StatUtils.mean(amplitudeArray);
    double sum = 0;
    for (double amp : amplitudeArray) {
      sum += Math.pow(amp - mean, 2);
    }
    return Math.sqrt(sum / (amplitudeArray.length - 1));
  }
}
