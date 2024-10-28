/* (C)2024 */
package com.nbmp.waveform.model.utils;

import java.util.function.BiFunction;

import org.mockito.Mockito;
import org.mockito.ThrowingConsumer;
import org.springframework.beans.factory.ObjectFactory;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.controller.ControllersState;
import com.nbmp.waveform.controller.SmartObservable;
import com.nbmp.waveform.model.dto.ModulationActiveWaveProps;
import com.nbmp.waveform.model.dto.RecombinationMode;
import com.nbmp.waveform.model.generation.GenerationState;
import com.nbmp.waveform.model.pipeline.GenerationListeners;
import com.nbmp.waveform.model.pipeline.OutStream;
import com.nbmp.waveform.model.pipeline.StreamReactor;
import com.nbmp.waveform.model.waveform.Waveform;

import static org.mockito.Mockito.when;

public class TestUtils {

  public static <T extends Number> ThrowingConsumer<T> isBetween(
      double minInclusive, double maxExclusive) {
    return (value) -> {
      if (value.doubleValue() >= minInclusive || value.doubleValue() < maxExclusive) {
        throw new AssertionError("Value is not between " + minInclusive + " and " + maxExclusive);
      }
    };
  }

  /**
   * Mocks the generation properties for testing.
   */
  public static void mockGenerationProps(
      GenerationState stateMock,
      Waveform waveform1,
      Waveform waveform2,
      ModulationActiveWaveProps waveProps,
      double modIndex,
      double amplitudeWave1,
      double amplitudeWave2) {
    BiFunction<Double, Double, Double> recombinationMode = RecombinationMode.ADD.getFunction();

    when(stateMock.getRecombinationMode()).thenReturn(recombinationMode);
    when(stateMock.getModulationIndex()).thenReturn(modIndex);
    when(waveform1.getProps()).thenReturn(waveProps);
    when(waveform2.getProps()).thenReturn(waveProps);
    when(stateMock.getWave1()).thenReturn(waveform1);
    when(stateMock.getWave2()).thenReturn(waveform2);
    when(waveform1.compute(1.0 / AppConstants.SAMPLE_RATE)).thenReturn(amplitudeWave1);
    when(waveform2.compute(1.0 / AppConstants.SAMPLE_RATE)).thenReturn(amplitudeWave2);
  }

  public static void mockReactor(GenerationState stateMock, StreamReactor streamReactor) {

    ObjectFactory<StreamReactor> reactorMock = Mockito.mock(ObjectFactory.class);
    when(stateMock.getReactor()).thenReturn(reactorMock);

    when(reactorMock.getObject()).thenReturn(streamReactor);
  }

  public static void mockReactor(ControllersState stateMock, StreamReactor streamReactor) {

    ObjectFactory<StreamReactor> reactorMock = Mockito.mock(ObjectFactory.class);
    when(stateMock.getReactor()).thenReturn(reactorMock);

    when(reactorMock.getObject()).thenReturn(streamReactor);
  }

  public static void mockReactor(
      ObjectFactory<StreamReactor> reactor,
      StreamReactor streamReactor,
      OutStream outStream,
      GenerationListeners persistentInitObservers,
      SmartObservable<Integer> clockStream) {
    var copyOnWriteArrayList = Mockito.mock(java.util.concurrent.CopyOnWriteArrayList.class);
    when(clockStream.getObservers()).thenReturn(copyOnWriteArrayList);
    when(reactor.getObject()).thenReturn(streamReactor);
    when(streamReactor.getOutStream()).thenReturn(outStream);
    when(streamReactor.getPersistentInitObservers()).thenReturn(persistentInitObservers);
    when(streamReactor.getClockStream()).thenReturn(clockStream);
  }
}
