/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import com.nbmp.waveform.model.utils.RealtimeMethods;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RecombinationMode {
  ADD((a, b) -> RealtimeMethods.clip(a + b, -1, 1)),
  SUBTRACT((a, b) -> RealtimeMethods.clip(a - b, -1, 1)),
  MULTIPLY((a, b) -> RealtimeMethods.clip(a * b, -1, 1)),
  DIVIDE((a, b) -> b == 0 ? 0 : RealtimeMethods.clip(a / b, -1, 1));
  private final BiFunction<Double, Double, Double> function;

  public static List<String> getNames() {
    return Arrays.stream(RecombinationMode.class.getEnumConstants()).map(Enum::name).toList();
  }
}
