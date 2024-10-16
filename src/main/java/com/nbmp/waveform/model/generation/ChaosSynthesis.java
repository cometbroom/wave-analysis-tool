/* (C)2024 */
package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.dto.BiTimeSeries;
import com.nbmp.waveform.model.dto.Signal;
import com.nbmp.waveform.model.filter.HighPassFilters;
import com.nbmp.waveform.model.filter.LowPassFilters;
import com.nbmp.waveform.model.utils.WaveStatUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChaosSynthesis implements Synthesis {
  // Modulation index
  private double k = 0.3;
  private final GenerationState state;
  private double lastAmplitude = 0.0;
  private int lastFrameIndex = 0;
  private int frameCounter = 0;
  private int zeroCrossingCounter = 1;

  @Override
  public BiTimeSeries compute(int duration) {
    int sampleCount = getSampleCount(duration);

    double timeStep = 1.0 / Generator.SAMPLE_RATE;
    double t = 0.0;

    var waveform1 = state.getWave1().getWaveform();
    var waveform2 = state.getWave2().getWaveform();

    var signal1 = new Signal();
    var signal2 = new Signal();
    var result = new Signal();

    signal1.addPoint(0.0, waveform1.compute(timeStep));
    signal2.addPoint(0.0, waveform2.compute(timeStep));

    var waveProps1 = waveform1.getProps();
    var waveProps2 = waveform2.getProps();
    // Basic coupling by using last computer value of the other wave
    waveProps2.setPhaseModulation((phi) -> phi + waveform1.getNormalizedPreviousAmplitude() * k);
    waveProps1.setPhaseModulation((phi) -> phi + waveform2.getNormalizedPreviousAmplitude() * k);

    for (int i = 1; i < sampleCount; i++) {
      signal1.addPoint(t, waveform1.compute(timeStep));
      signal2.addPoint(t, waveform2.compute(timeStep));
      result.addPoint(t, (signal1.getAmplitude(i) + signal2.getAmplitude(i)) / 2);
      t += timeStep;
    }
    var sinalProcessor = new TwoPlusOneDSP(signal1, signal2, result);

    sinalProcessor.applyEffect(HighPassFilters::removeDcOffsetMeanTechnique);
    sinalProcessor.applyEffect(HighPassFilters::removeDcOffset);
    sinalProcessor.applyEffect(LowPassFilters::applyButterWorth, 500);
    sinalProcessor.applyEffect(WaveStatUtils::oneToOneNormalize);

    resetWaveforms();
    state.getResultSeries().refreshData(result.getTimeAmplitude());
    return new BiTimeSeries(signal1.getTimeAmplitude(), signal2.getTimeAmplitude());
  }

  // Problematic method. fix get sample count 500. Which is not true.
  private int getSampleCount(int durationInMs) {
    double sampleCountDouble = Generator.SAMPLE_RATE * durationInMs / 1000.0;
    return Math.toIntExact(Math.round(sampleCountDouble));
  }

  private void monitorZeroCrossing(double[][] waveData, int i, double t) {
    if (frameCounter >= 255) {
      return;
    }
    if (isZeroCrossing(waveData[i][GenConstants.AMPLITUDE])) {
      if (zeroCrossingCounter % 2 == 0) {
        state.getFrameDurations()[frameCounter++] = i - lastFrameIndex;
        lastFrameIndex = i;
      }
      zeroCrossingCounter++;
    }
    lastAmplitude = waveData[i][GenConstants.AMPLITUDE];
  }

  private double getZeroCrossingAngle(double signalValue) {
    return Math.acos(lastAmplitude * signalValue);
  }

  private void resetWaveforms() {
    state.getWave1().getWaveform().getProps().resetModulations();
    state.getWave2().getWaveform().getProps().resetModulations();
    state.getWave1().getWaveform().setCumulativePhaseRadians(0);
    state.getWave2().getWaveform().setCumulativePhaseRadians(0);
  }

  private boolean isZeroCrossing(double signalValue) {
    return lastAmplitude * signalValue < 0;
  }
}
