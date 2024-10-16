/* (C)2024 */
package com.nbmp.waveform.model.filter;

import com.github.psambit9791.jdsp.filter.Butterworth;
import com.github.psambit9791.jdsp.filter.FIRWin2;
import com.nbmp.waveform.application.AppConfig;
import com.nbmp.waveform.model.generation.GenConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LowPassFilters {
  public static double[][] movingAverage(double[][] signal, int windowSize) {
    int dataSize = signal.length;
    double[][] filteredData = new double[dataSize][2];

    int halfWindow = windowSize / 2;

    for (int i = 0; i < dataSize; i++) {
      double sum = 0;
      int count = 0;

      for (int j = i - halfWindow; j <= i + halfWindow; j++) {
        if (j >= 0 && j < dataSize) {
          sum += signal[j][GenConstants.AMPLITUDE];
          count++;
        }
      }
      double average = sum / count;

      filteredData[i][GenConstants.TIME] = signal[i][GenConstants.TIME];
      filteredData[i][GenConstants.AMPLITUDE] = average;
    }

    return filteredData;
  }

  public static double[] applyButterWorth(double[] signal, double cutoff) {
    double samplingFrequency = AppConfig.SAMPLE_RATE; //

    int filterOrder = 2; // Second-order filter
    Butterworth butterworth = new Butterworth(samplingFrequency);

    return butterworth.lowPassFilter(signal, filterOrder, cutoff);
  }

  public static double[][] applyFIRFilter(double[][] data) {
    return applyFIRFilter(data, computeCoefficientsFIR());
  }

  public static double[][] applyFIRFilter(double[][] data, double[] coefficients) {
    int dataSize = data.length;
    int filterOrder = coefficients.length;
    double[][] filteredSignal = new double[dataSize][2];

    for (int i = 0; i < dataSize; i++) {
      double filteredValue = 0;

      // Apply the filter coefficients
      for (int j = 0; j < filterOrder; j++) {
        int index = i - j;
        if (index >= 0) {
          filteredValue += coefficients[j] * data[index][1];
        }
      }

      filteredSignal[i][GenConstants.TIME] = data[i][GenConstants.TIME];
      filteredSignal[i][GenConstants.AMPLITUDE] = filteredValue;
    }

    return filteredSignal;
  }

  public static double[] computeCoefficientsFIR() {
    double samplingRate = 100;
    double[] freqs = {0.0, 25, 50};
    double[] gains = {0.0, 1.0, 1.0};
    int taps = 10;
    FIRWin2 fw2 = new FIRWin2(taps, samplingRate);
    return fw2.computeCoefficients(freqs, gains);
  }
}
