/* (C)2024 */
package com.nbmp.waveform.extras;

import com.nbmp.waveform.models.SliderTarget;

public interface Sliderable {
  void updateValue(SliderTarget target, double value);
}
