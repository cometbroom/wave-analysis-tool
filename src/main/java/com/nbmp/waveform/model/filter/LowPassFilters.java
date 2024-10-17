/* (C)2024 */
package com.nbmp.waveform.model.filter;

import com.github.psambit9791.jdsp.filter.Butterworth;
import com.nbmp.waveform.application.AppConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LowPassFilters {
  public static double[] applyButterWorth(double[] signal, double cutoff) {
    double samplingFrequency = AppConstants.SAMPLE_RATE; //

    int filterOrder = 2; // Second-order filter
    Butterworth butterworth = new Butterworth(samplingFrequency);

    return butterworth.lowPassFilter(signal, filterOrder, cutoff);
  }
}
