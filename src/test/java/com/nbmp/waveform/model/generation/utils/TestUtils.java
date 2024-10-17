/* (C)2024 */
package com.nbmp.waveform.model.generation.utils;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.model.generation.GenerationState;
import com.nbmp.waveform.model.waveform.Waveform;
import com.nbmp.waveform.view.WavesRegister;

import static org.mockito.Mockito.when;

public class TestUtils {

  /**
   * Mocks the generation properties for testing.
   */
  public static void mockGenerationProps(
      GenerationState stateMock,
      WavesRegister wave1,
      WavesRegister wave2,
      Waveform waveform1,
      Waveform waveform2,
      ModulationActiveWaveProps waveProps,
      TimeSeries resultSerise,
      int duration,
      double amplitudeWave1,
      double amplitudeWave2) {
    when(wave1.getWaveform()).thenReturn(waveform1);
    when(wave2.getWaveform()).thenReturn(waveform2);
    when(waveform1.getProps()).thenReturn(waveProps);
    when(waveform2.getProps()).thenReturn(waveProps);
    when(stateMock.getWave1()).thenReturn(wave1);
    when(stateMock.getWave2()).thenReturn(wave2);
    when(stateMock.getResultSeries()).thenReturn(resultSerise);
    when(waveform1.compute(1.0 / AppConstants.SAMPLE_RATE)).thenReturn(amplitudeWave1);
    when(waveform2.compute(1.0 / AppConstants.SAMPLE_RATE)).thenReturn(amplitudeWave2);
  }
}
