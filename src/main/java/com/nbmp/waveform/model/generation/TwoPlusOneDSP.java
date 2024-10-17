/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.StatUtils;

import com.nbmp.waveform.model.dto.Signal;

import lombok.Getter;

@Getter
public class TwoPlusOneDSP {
  private final List<Signal> signalStream;

  public TwoPlusOneDSP(Signal signal1, Signal signal2, Signal signal3) {
    // Terminating stream as array is not large enough
    signalStream = Stream.of(signal1, signal2, signal3).filter(this::validSignal).toList();
  }

  public TwoPlusOneDSP getSignalStream() {
    return this;
  }

  public void applyEffect(Consumer<Signal> effectFuntcion) {
    signalStream.forEach(effectFuntcion);
  }

  public void applyEffect(Function<double[], double[]> effectFuntcion) {
    signalStream.forEach(signal -> signal.applyEffect(effectFuntcion));
  }

  public void applyEffect(BiFunction<double[], Double, double[]> effectFuntcion, double fxParam) {
    signalStream.forEach(signal -> signal.applyEffect(effectFuntcion, fxParam));
  }

  private boolean validSignal(Signal signal) {
    double min = StatUtils.min(signal.getAmplitudeArray());
    double max = StatUtils.max(signal.getAmplitudeArray());
    double maxAbs = Math.max(Math.abs(min), Math.abs(max));

    return maxAbs != 0
        && signal.getAmplitudeArray().length > 0
        && signal.getTimeArray().length > 0
        && Arrays.stream(signal.getAmplitudeArray()).noneMatch(Double::isNaN)
        && Arrays.stream(signal.getTimeArray()).noneMatch(Double::isNaN);
  }
}
