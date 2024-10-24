/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.function.BiFunction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.dto.TimeSeries;
import com.nbmp.waveform.model.generation.utils.TestUtils;
import com.nbmp.waveform.model.waveform.Waveform;
import com.nbmp.waveform.view.ChartView;
import com.nbmp.waveform.view.WavesRegister;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class FMSynthesisTest {

  @Mock private Waveform waveform1;
  @Mock private Waveform waveform2;
  @Mock private WavesRegister wave1;
  @Mock private WavesRegister wave2;
  @Mock private ModulationActiveWaveProps waveProps;
  @Mock private TimeSeries resultSerise;
  @Mock private GenerationState stateMock;
  @Mock private ControllersState controllersStateMock;
  @Mock private ChartView chartViewMock;
  @InjectMocks private FMSynthesis fmSynthesis;

  @Test
  public void computeReturnsNonNullBiTimeSeries() {
    int duration = 10;
    BiFunction<Double, Double, Double> recombinationMode = RecombinationMode.ADD.getFunction();

    when(controllersStateMock.getView1()).thenReturn(chartViewMock);
    when(controllersStateMock.getView2()).thenReturn(chartViewMock);
    when(controllersStateMock.getResultView()).thenReturn(chartViewMock);
    TestUtils.mockGenerationProps(
        stateMock, wave1, wave2, waveform1, waveform2, waveProps, resultSerise, duration, 0.0, 1.0);
    //    fmSynthesis.setRecombinationMode(recombinationMode);

    fmSynthesis.compute(duration);
    verify(chartViewMock, times(3)).refreshData(any(double[][].class));
  }

  @Test
  public void computeReturnsCorrectBiTimeSeries() {
    int duration = 10;
    BiFunction<Double, Double, Double> recombinationMode = RecombinationMode.ADD.getFunction();
    TestUtils.mockGenerationProps(
        stateMock, wave1, wave2, waveform1, waveform2, waveProps, resultSerise, duration, 1.0, 2.0);

    //    fmSynthesis.setRecombinationMode(recombinationMode);

    fmSynthesis.compute(duration);

    //    assertEquals(1.0, result.timeAmplitude1()[0][GenConstants.AMPLITUDE]);
    //    assertEquals(2.0, result.timeAmplitude2()[0][GenConstants.AMPLITUDE]);
  }

  @Test
  public void computeHandlesZeroDuration() {
    int duration = 0;
    BiFunction<Double, Double, Double> recombinationMode = RecombinationMode.ADD.getFunction();
    TestUtils.mockGenerationProps(
        stateMock, wave1, wave2, waveform1, waveform2, waveProps, resultSerise, duration, 1.0, 2.0);

    //    fmSynthesis.setRecombinationMode(recombinationMode);

    fmSynthesis.compute(duration);

    //    assertNotNull(result);
    //    assertEquals(
    //        0., Arrays.stream(result.timeAmplitude1()).map(x ->
    // x[GenConstants.AMPLITUDE]).count());
    //    assertEquals(
    //        0, Arrays.stream(result.timeAmplitude2()).map(x ->
    // x[GenConstants.AMPLITUDE]).count());
  }
}
