/* (C)2024 */
package com.nbmp.waveform.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class WaveProps {
  protected double frequency, amplitude, initialPhase;

  public static WaveProps defaultWaveformProps() {
    return new WaveProps(5, 1, 0);
  }
}
