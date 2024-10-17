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

}
