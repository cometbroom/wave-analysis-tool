/* (C)2024 */
package com.nbmp.waveform.model.dto;

import com.google.common.base.MoreObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class WaveProps {
  protected double frequency, amplitude, initialPhase;

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("frequency", frequency)
        .add("amplitude", amplitude)
        .add("initialPhase", initialPhase)
        .toString();
  }
}
