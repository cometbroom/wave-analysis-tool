/* (C)2024 */
package com.nbmp.waveform.model.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.nbmp.waveform.controller.SmartObservable;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class StreamReactorTest {
  private static final int RANGE = 10;

  @Mock SmartObservable.Observer<Integer> runnableMock;
  @Mock GenerationListeners generationListenersMock;
  @Mock OutStream outStreamMock;
  @InjectMocks StreamReactor streamReactor = new StreamReactor(RANGE);

  @Test
  void run() {
    MutableInt testIdx = new MutableInt(0);
    streamReactor.addObserver(runnableMock);
    Mockito.doAnswer(
            invocation -> {
              testIdx.increment();
              return null;
            })
        .when(runnableMock)
        .onUpdate(Mockito.anyInt());
    streamReactor.run();
    Mockito.verify(runnableMock, Mockito.times(RANGE)).onUpdate(Mockito.anyInt());
    if (testIdx.intValue() != RANGE) {
      fail("Expected " + RANGE + " invocations, got " + testIdx.intValue());
    }
  }

  @Test
  void run_sampleSizeLinesUp() {
    List<Integer> samples = new ArrayList<>();
    streamReactor.addObserver(runnableMock);
    Mockito.doAnswer(
            invocation -> {
              int reactorIdx = invocation.getArgument(0);
              samples.add(reactorIdx);
              return null;
            })
        .when(runnableMock)
        .onUpdate(Mockito.anyInt());
    streamReactor.run();
    assertEquals(RANGE, samples.size());
  }

  @Test
  void resume() {
    MutableInt testIdx = new MutableInt(0);
    streamReactor.addObserver(runnableMock);
    Mockito.doAnswer(
            invocation -> {
              testIdx.increment();
              return null;
            })
        .when(runnableMock)
        .onUpdate(Mockito.anyInt());
    streamReactor.runFor(RANGE / 2);
    assertEquals(RANGE / 2, testIdx.intValue());
    Mockito.verify(runnableMock, Mockito.times(RANGE / 2)).onUpdate(Mockito.anyInt());
    streamReactor.resume();
    Mockito.verify(runnableMock, Mockito.times(RANGE)).onUpdate(Mockito.anyInt());
    if (testIdx.intValue() != RANGE) {
      fail("Expected " + RANGE + " invocations, got " + testIdx.intValue());
    }
  }

}
