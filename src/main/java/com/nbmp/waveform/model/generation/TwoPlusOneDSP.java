/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.nbmp.waveform.model.dto.Signal;

public class TwoPlusOneDSP {
  private final Signal signal1;
  private final Signal signal2;
  private final Signal signal3;

  public TwoPlusOneDSP(Signal signal1, Signal signal2, Signal signal3) {
    this.signal1 = signal1;
    this.signal2 = signal2;
    this.signal3 = signal3;
  }

  public void applyEffect(Consumer<Signal> effectFuntcion) {
    effectFuntcion.accept(signal1);
    effectFuntcion.accept(signal2);
    effectFuntcion.accept(signal3);
  }

  public void applyEffect(Function<double[], double[]> effectFuntcion) {
    signal1.applyEffect(effectFuntcion);
    signal2.applyEffect(effectFuntcion);
    signal3.applyEffect(effectFuntcion);
  }

  public void applyEffect(BiFunction<double[], Double, double[]> effectFuntcion, double fxParam) {
    signal1.applyEffect(effectFuntcion, fxParam);
    signal2.applyEffect(effectFuntcion, fxParam);
    signal3.applyEffect(effectFuntcion, fxParam);
  }

}
