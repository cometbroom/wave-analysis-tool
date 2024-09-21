/* (C)2024 */
package com.nbmp.waveform.generation;

import com.nbmp.waveform.models.SmartData;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class PhaseWaveGenerator {
  private SmartData<Double> maxValue = new SmartData<>(Double.NEGATIVE_INFINITY);
}
