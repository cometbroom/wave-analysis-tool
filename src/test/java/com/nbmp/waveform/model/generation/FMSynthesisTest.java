/* (C)2024 */
package com.nbmp.waveform.model.generation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.utils.TestUtils;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FMSynthesisTest extends SynthesisTest {
  @InjectMocks private FMSynthesis fmSynthesis;

  @Test
  public void computeReturnsNonNullBiTimeSeries() {
    fmSynthesis.compute(DURATION);
    verifyStreamReactorAddOutputs(streamReactor, times(SAMPLE_COUNT));
  }

  @Test
  public void computeReturnsCorrectBiTimeSeries() {
    int duration = 1;
    TestUtils.mockGenerationProps(
        stateMock, waveform1, waveform2, waveProps, resultSerise, MOD_INDEX, 1.0, 2.0);
    fmSynthesis.compute(duration);
    verify(streamReactor, times(1)).addOutputs(eq(0), eq(1.0), eq(2.0), eq(1.0));
  }

  @Test
  public void computeHandlesZeroDuration() throws Exception {
    int duration = 0;

    openedMocks.close();
    streamReactor = Mockito.spy(new StreamReactor(duration));
    openedMocks = MockitoAnnotations.openMocks(this);
    TestUtils.mockGenerationProps(
        stateMock, waveform1, waveform2, waveProps, resultSerise, MOD_INDEX, 0.0, 1.0);
    TestUtils.mockReactor(stateMock, streamReactor);

    try (MockedStatic<AppConstants> appConstants = mockStatic(AppConstants.class)) {
      appConstants.when(AppConstants::getSampleCount).thenReturn(duration);
      fmSynthesis.compute(duration);
      verify(streamReactor, never()).addOutputs(anyInt(), anyDouble(), anyDouble(), anyDouble());
    }
  }
}
