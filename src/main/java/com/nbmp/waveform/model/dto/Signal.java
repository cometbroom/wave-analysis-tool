/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.generation.GenConstants;
import com.nbmp.waveform.model.generation.Generator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Signal {
  private double sumAmplitude;
  private double mean;
  private double[] timeArray;
  private double[] amplitudeArray;
  private int indexCounter;
  private int sampleCount;

  public Signal() {
    sumAmplitude = 0.0;
    mean = 0.0;
    indexCounter = 0;
    sampleCount = Generator.SAMPLE_RATE * AppConstants.duration.getValue() / 1000;
    timeArray = new double[sampleCount];
    amplitudeArray = new double[sampleCount];
  }

  public void addPoint(double t, double amplitude) {
    if (indexCounter >= sampleCount) {
      return;
    }
    this.timeArray[indexCounter] = t;
    this.amplitudeArray[indexCounter] = amplitude;
    sumAmplitude += amplitude;
    indexCounter++;
  }

  public void changePoint(int index, double t, double amplitude) {
    if (index >= sampleCount) {
      return;
    }
    sumAmplitude -= amplitudeArray[index];
    this.timeArray[index] = t;
    this.amplitudeArray[index] = amplitude;
    sumAmplitude += amplitude;
  }

  public double recalculateMean() {
    mean = sumAmplitude / sampleCount;
    return mean;
  }

  public void applyEffect(Function<double[], double[]> effectFuntcion) {
    amplitudeArray = effectFuntcion.apply(amplitudeArray);
  }

  public void applyEffect(BiFunction<double[], Double, double[]> effectFuntcion, double fxParam) {
    amplitudeArray = effectFuntcion.apply(amplitudeArray, fxParam);
  }

  public double[][] getTimeAmplitude() {
    double[][] timeAmplitude = new double[sampleCount][2];
    for (int i = 0; i < sampleCount; i++) {
      timeAmplitude[i][GenConstants.TIME] = timeArray[i];
      timeAmplitude[i][GenConstants.AMPLITUDE] = amplitudeArray[i];
    }
    return timeAmplitude;
  }

  public double getTime(int index) {
    return timeArray[index];
  }

  public double getAmplitude(int index) {
    return amplitudeArray[index];
  }
}
