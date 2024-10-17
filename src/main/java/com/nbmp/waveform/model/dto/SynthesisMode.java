/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

@Getter
public enum SynthesisMode {
  INDEPENDENT,
  CHAOS_TWO_WAY_FM(0.3),
  CHAOS_INDEPENDENT_SELF_MOD_FM(0.2),
  FM_WAVE1MOD_WAVE2CARRIER(2);
  private double modIndex = 0.0;
  private String title;

  SynthesisMode() {
    this(0.0);
  }

  SynthesisMode(double modIndex) {
    this.modIndex = modIndex;
    this.title = this.name();
  }

  SynthesisMode(String title, double modIndex) {
    this.modIndex = modIndex;
    this.title = title;
  }

  public static List<String> getNames() {
    return Arrays.stream(SynthesisMode.class.getEnumConstants()).map(Enum::name).toList();
  }
}
