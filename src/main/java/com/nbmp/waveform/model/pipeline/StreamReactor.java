/* (C)2024 */
package com.nbmp.waveform.model.pipeline;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.reactivestreams.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.MoreObjects;
import com.nbmp.waveform.controller.SmartObservable;
import com.nbmp.waveform.model.dto.OutputDataType;

import lombok.Getter;
import lombok.experimental.Delegate;
import reactor.core.CoreSubscriber;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

@Getter
public class StreamReactor {
  @Delegate private SmartObservable<Integer> clockStream = new SmartObservable<>(0);

  private Flux<Integer> clock;
  private Flux<OutputDataType> outClock;
  @Autowired private OutStream outStream;

  @Autowired private GenerationListeners persistentInitObservers;
  private CopyOnWriteArrayList<SmartObservable.Observer<Integer>> clockObservers =
      new CopyOnWriteArrayList<>();

  private CopyOnWriteArrayList<Observer> nonPersistentInitObservers = new CopyOnWriteArrayList<>();
  private CopyOnWriteArrayList<Observer> completionObservers = new CopyOnWriteArrayList<>();
  private Queue<SmartObservable<Integer>> postProcessDump = new LinkedList<>();
  private final Integer range;

  public StreamReactor(int range) {
    this.range = range;
    clock = Flux.range(0, range);
  }

  public Disposable subscribe(Consumer<? super Integer> consumer) {
    return clock.subscribe(consumer);
  }

  public <V> Flux<V> map(Function<? super Integer, ? extends V> mapper) {
    return clock.map(mapper);
  }

  public Flux<OutputDataType> mapToOutput(Function<? super Integer, OutputDataType> mapper) {
    return clock.map(mapper);
  }

  public Disposable subscribe() {
    return clock.subscribe();
  }

  public void subscribe(CoreSubscriber<? super Integer> actual) {
    clock.subscribe(actual);
  }

  public void subscribe(Subscriber<? super Integer> actual) {
    clock.subscribe(actual);
  }

  public void addObserver(SmartObservable.Observer<Integer> observer) {
    clockObservers.add(observer);
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

  private int clockUpdateMapper(int i) {
    clockObservers.forEach(observer -> observer.onUpdate(i));
    return i;
  }

  public void run(int start, int end) {
    start();
    clock = Flux.range(start, end - start).map(this::clockUpdateMapper);
    clock.subscribe();
    complete();
  }

  private void complete() {
    completionObservers.reversed().forEach(Observer::onAction);
  }

  private void start() {
    persistentInitObservers.forEach(Observer::onAction);
    nonPersistentInitObservers.forEach(Observer::onAction);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("clockStream", clockStream)
        .add("outStream", outStream)
        .add("persistentInitObservers", persistentInitObservers)
        .add("nonPersistentInitObservers", nonPersistentInitObservers)
        .add("completionObservers", completionObservers)
        .add("postProcessDump", postProcessDump)
        .add("range", range)
        .toString();
  }

  public interface Observer {
    void onAction();
  }

  public interface QuadConsumer {
    void accept(Integer t, Double u, Double v, Double w);
  }
}
