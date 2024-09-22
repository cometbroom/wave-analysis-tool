/* (C)2024 */
package com.nbmp.waveform.guides;

import com.nbmp.waveform.models.SliderTarget;

import java.util.function.Consumer;

public interface Guide {
  Double compute(Double t, Double timeStep);
}
