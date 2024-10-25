/* (C)2024 */
package com.nbmp.waveform.model.pipeline;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.nbmp.waveform.controller.SmartObservable;

import lombok.Getter;
import lombok.experimental.Delegate;

@Getter
public class StreamReactor {
  @Delegate private SmartObservable<Integer> clockStream = new SmartObservable<>(0);
  @Autowired private OutStream outStream;

  @Autowired private GenerationListeners persistentInitObservers;
  private CopyOnWriteArrayList<Observer> nonPersistentInitObservers = new CopyOnWriteArrayList<>();
  private CopyOnWriteArrayList<Observer> completionObservers = new CopyOnWriteArrayList<>();
  private Queue<SmartObservable<Integer>> postProcessDump = new LinkedList<>();
  private final Integer range;

  public StreamReactor(int range) {
    this.range = range;
  }

  public void resume() {
    resume(range);
  }

  public void resume(int sampleCount) {
    int newStartIdx = clockStream.getValue() + 1;
    if (newStartIdx < sampleCount) {
      run(newStartIdx, sampleCount);
    }
  }

  public void schedulePostProcess(SmartObservable<Integer> observable) {
    postProcessDump.add(observable);
    onComplete(
        () -> {
          SmartObservable<Integer> next = postProcessDump.poll();
          if (next != null) {
            next.fireEvents();
          }
        });
  }

  public void runFor(int durationInSamples) {
    run(clockStream.getValue(), clockStream.getValue() + durationInSamples);
  }

  public void runFor(int durationInSamples, SmartObservable.Observer<Integer> observer) {
    clockStream.addObserver(observer);
    run(clockStream.getValue(), clockStream.getValue() + durationInSamples);
    clockStream.removeObserver(observer);
  }

  public void onStart(Runnable runnable) {
    persistentInitObservers.add(runnable::run);
  }

  public void onComplete(Runnable runnable) {
    completionObservers.add(runnable::run);
  }

  public void onRestart(Runnable runnable) {
    nonPersistentInitObservers.add(runnable::run);
  }

  public void addOutputs(
      Integer i, double wave1Amplitude, double wave2Amplitude, double recombination) {
    outStream.setValue(i, wave1Amplitude, wave2Amplitude, recombination);
  }

  public void addOutStreamObserver(QuadConsumer consumer) {
    outStream
        .getStream()
        .addObserver(
            (pair) ->
                consumer.accept(
                    pair.getKey(),
                    pair.getValue().channel1(),
                    pair.getValue().channel2(),
                    pair.getValue().channel3()));
  }

  public void run() {
    run(0, range);
  }

  public void run(int start, int end) {
    start();
    for (int i = start; i < end; i++) {
      clockStream.setValue(i);
    }
    complete();
  }

  private void complete() {
    completionObservers.reversed().forEach(Observer::onAction);
  }

  private void start() {
    persistentInitObservers.forEach(Observer::onAction);
    nonPersistentInitObservers.forEach(Observer::onAction);
  }

  public interface Observer {
    void onAction();
  }

  public interface QuadConsumer {
    void accept(Integer t, Double u, Double v, Double w);
  }
}
