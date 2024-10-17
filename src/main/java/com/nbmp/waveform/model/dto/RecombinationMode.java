/* (C)2024 */
package com.nbmp.waveform.model.dto;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RecombinationMode {
  ADD((a, b) -> (a + b) / 2),
  SUBTRACT((a, b) -> (a - b) / 2),
  MULTIPLY((a, b) -> a * b),
  DIVIDE((a, b) -> b == 0 ? 0 : a / b);
  private final BiFunction<Double, Double, Double> function;

  public static List<String> getNames() {
    return Arrays.stream(RecombinationMode.class.getEnumConstants()).map(Enum::name).toList();
  }
}
