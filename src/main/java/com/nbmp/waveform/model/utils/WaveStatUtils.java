/* (C)2024 */
package com.nbmp.waveform.model.utils;

import org.apache.commons.math3.stat.StatUtils;

import com.github.psambit9791.jdsp.misc.UtilMethods;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaveStatUtils extends UtilMethods {

  public static double[] oneToOneNormalize(double[] amplitudeArray) {
    double min = StatUtils.min(amplitudeArray);
    double max = StatUtils.max(amplitudeArray);
    double maxAbs = Math.max(Math.abs(min), Math.abs(max));
    if (maxAbs == 0) {
      log.warn("Cannot normalize zero signal. Returning array as is.");
      return amplitudeArray;
    }
    double[] normalizedAmplitude = new double[amplitudeArray.length];
    for (int i = 0; i < amplitudeArray.length; i++) {
      normalizedAmplitude[i] = amplitudeArray[i] / maxAbs;
    }
    return normalizedAmplitude;
  }
}
