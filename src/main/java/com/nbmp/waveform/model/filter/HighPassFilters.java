/* (C)2024 */
package com.nbmp.waveform.model.filter;

import com.github.psambit9791.jdsp.filter.Butterworth;
import com.nbmp.waveform.application.AppConfig;
import com.nbmp.waveform.model.dto.Signal;

public class HighPassFilters {

  public static double[] removeDcOffset(double[] signal) {
    double samplingFrequency = AppConfig.SAMPLE_RATE; //
    double cutoff = 2; // hz

    int filterOrder = 2;
    Butterworth butterworth = new Butterworth(samplingFrequency);

    return butterworth.highPassFilter(signal, filterOrder, cutoff);
  }

  public static Signal removeDcOffsetMeanTechnique(Signal signal) {
    double mean = signal.recalculateMean();
    for (int i = 0; i < signal.getSampleCount(); i++) {
      signal.changePoint(i, signal.getTime(i), signal.getAmplitude(i) - mean);
    }
    return signal;
  }
}