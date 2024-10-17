/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.function.BiFunction;
import java.util.function.Function;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.generation.GenConstants;
import com.nbmp.waveform.model.generation.Generator;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a signal consisting of time and amplitude data.
 */
@Getter
@Setter
public class Signal {
  private double sumAmplitude;
  private double mean;
  private double[] timeArray;
  private double[] amplitudeArray;
  private int indexCounter;
  private int sampleCount;

  /**
   * Default constructor for Signal.
   */
  public Signal() {
    sumAmplitude = 0.0;
    mean = 0.0;
    indexCounter = 0;
    sampleCount = Generator.SAMPLE_RATE * AppConstants.duration.getValue() / 1000;
    timeArray = new double[sampleCount];
    amplitudeArray = new double[sampleCount];
  }

  /**
   * Adds a point to the signal.
   *
   * @param t the time value
   * @param amplitude the amplitude value
   */
  public void addPoint(double t, double amplitude) {
    if (indexCounter >= sampleCount) {
      return;
    }
    this.timeArray[indexCounter] = t;
    this.amplitudeArray[indexCounter] = amplitude;
    sumAmplitude += amplitude;
    indexCounter++;
  }

  /**
   * Changes a point in the signal.
   *
   * @param index the index of the point to be changed
   * @param t the new time value
   * @param amplitude the new amplitude value
   */
  public void changePoint(int index, double t, double amplitude) {
    if (index >= sampleCount) {
      return;
    }
    sumAmplitude -= amplitudeArray[index];
    this.timeArray[index] = t;
    this.amplitudeArray[index] = amplitude;
    sumAmplitude += amplitude;
  }

  /**
   * Recalculates and returns the mean amplitude of the signal.
   *
   * @return the recalculated mean amplitude
   */
  public double recalculateMean() {
    mean = sumAmplitude / sampleCount;
    return mean;
  }

  /**
   * Applies an effect function to the amplitude array.
   *
   * @param effectFunction the effect function to be applied
   */
  public void applyEffect(Function<double[], double[]> effectFunction) {
    amplitudeArray = effectFunction.apply(amplitudeArray);
  }

  /**
   * Applies an effect function to the amplitude array with an additional parameter.
   *
   * @param effectFunction the effect function to be applied
   * @param fxParam the additional parameter for the effect function
   */
  public void applyEffect(BiFunction<double[], Double, double[]> effectFunction, double fxParam) {
    amplitudeArray = effectFunction.apply(amplitudeArray, fxParam);
  }

  /**
   * Gets the time-amplitude data as a 2D array.
   *
   * @return a 2D array containing the time and amplitude data
   */
  public double[][] getTimeAmplitude() {
    double[][] timeAmplitude = new double[sampleCount][2];
    for (int i = 0; i < sampleCount; i++) {
      timeAmplitude[i][GenConstants.TIME] = timeArray[i];
      timeAmplitude[i][GenConstants.AMPLITUDE] = amplitudeArray[i];
    }
    return timeAmplitude;
  }

  /**
   * Gets the time value at a specific index.
   *
   * @param index the index of the time value
   * @return the time value at the specified index
   */
  public double getTime(int index) {
    return timeArray[index];
  }

  /**
   * Gets the amplitude value at a specific index.
   *
   * @param index the index of the amplitude value
   * @return the amplitude value at the specified index
   */
  public double getAmplitude(int index) {
    return amplitudeArray[index];
  }
}