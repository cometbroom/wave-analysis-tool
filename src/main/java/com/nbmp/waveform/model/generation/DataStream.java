/* (C)2024 */
package com.nbmp.waveform.model.generation;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.SignalCollection;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataStream {
  private Stream<Double> wave1;
  private Stream<Double> wave2;
  private Stream<Double> resultWave;
  private double sumAmplitude;
  private double mean;

  private AtomicReference<Integer> duration = new AtomicReference<>(0);

  public DataStream() {
    var dataArray = new Double[getSampleCount()];

    this.wave1 = Stream.of(dataArray);
    this.wave2 = Stream.of(dataArray);
    this.resultWave = Stream.of(dataArray);
  }

  public DataStream map(Function<Double, Double> mapFunction) {
    this.wave1 = wave1.map(mapFunction);
    this.wave2 = wave2.map(mapFunction);
    this.resultWave = resultWave.map(mapFunction);
    return this;
  }

  public DataStream skip(int n) {
    this.wave1 = wave1.skip(n);
    this.wave2 = wave2.skip(n);
    this.resultWave = resultWave.skip(n);
    return this;
  }

  public void read(Consumer<SignalCollection> applyFunction) {
    zip(wave1, wave2, resultWave).forEach(applyFunction);
  }

  private int getSampleCount() {
    try {
      double sampleCountDouble = Generator.SAMPLE_RATE * duration.get() / 1000.0;
      return Math.toIntExact(Math.round(sampleCountDouble));
    } catch (ArithmeticException ex) {
      throw new ArithmeticException("Error occurred while getting sample count");
    }
  }

  private Stream<SignalCollection> zip(
      Stream<Double> first, Stream<Double> second, Stream<Double> third) {
    Iterator<Double> firstIterator = first.iterator();
    Iterator<Double> secondIterator = second.iterator();
    Iterator<Double> thirdIterator = third.iterator();

    Iterator<SignalCollection> iterator =
        new Iterator<>() {
          @Override
          public boolean hasNext() {
            return firstIterator.hasNext() && secondIterator.hasNext() && thirdIterator.hasNext();
          }

          @Override
          public SignalCollection next() {
            return new SignalCollection(
                AppConstants.TIME_STEP,
                firstIterator.next(),
                secondIterator.next(),
                thirdIterator.next());
          }
        };

    Spliterator<SignalCollection> spliterator =
        Spliterators.spliterator(iterator, getSampleCount(), Spliterator.ORDERED);
    return StreamSupport.stream(spliterator, false);
  }
}
