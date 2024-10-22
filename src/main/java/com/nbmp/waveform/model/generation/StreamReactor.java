package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.application.AppConstants;
import com.nbmp.waveform.model.dto.SignalCollection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;

import java.util.*;
import java.util.function.Function;

@Getter
public class StreamReactor {
  // Stream of indeces that get iterated
  private Flux<Integer> stream;

  // A mirrored stream that groups the indeces by their section and gives section along with index
  private Flux<GroupedFlux<Section, Integer>> groupedStream;


  public List<FunctionRangeDescriptor> wave1, wave2, resultWave;

  public enum Section {
    START, MIDDLE, END, ALL
  }

  private final Map<Section, Pipeline> sectionTasks = new HashMap<>();

  private final Function<Integer, Section> sectionMapper = (i) -> {
    if (i < 1) return Section.START;
    else if (i > 1 && i < getSampleCount() - 1) return Section.MIDDLE;
    else return Section.END;
  };



  public StreamReactor() {
    refreshMapFunctions();
  }

  public void refreshMapFunctions() {
    sectionTasks.put(Section.START, new Pipeline());
    sectionTasks.put(Section.MIDDLE, new Pipeline());
    sectionTasks.put(Section.END, new Pipeline());
    wave1 = new LinkedList<>();
    wave2 = new LinkedList<>();
    resultWave = new LinkedList<>();
    stream = Flux.range(0, getSampleCount());
    groupedStream = stream.groupBy(sectionMapper);
//    groupedStream.subscribe(group -> {

//    stream = Flux.range(0, getSampleCount())
//        .map(i -> {
//          double t = AppConstants.TIME_STEP * i, timeStep = AppConstants.TIME_STEP;
//          return new SignalCollection(t, composeFunctions(wave1, i).apply(timeStep), composeFunctions(wave2, i).apply(timeStep), composeFunctions(resultWave, i).apply(timeStep));
//        });
  }


  public void add(List<FunctionRangeDescriptor> target, Function<Double, Double> func, int start, int end) {
    target.add(new FunctionRangeDescriptor(start, end, func));
  }

  public void addWave1(Function<Double, Double> func, int start, int end) {
    add(wave1, func, start, end);
  }

  public void addWave2(Function<Double, Double> func, int start, int end) {
    add(wave2, func, start, end);
  }

  public void addResultWave(Function<Double, Double> func, int start, int end) {
    add(resultWave, func, start, end);
  }

  public int getSampleCount() {
    try {
      double sampleCountDouble = Generator.SAMPLE_RATE * AppConstants.duration.getValue() / 1000.0;
      return Math.toIntExact(Math.round(sampleCountDouble));
    } catch (ArithmeticException ex) {
      throw new ArithmeticException("Error occurred while getting sample count");
    }
  }

  private Function<Double, Double> composeFunctions(List<FunctionRangeDescriptor> functions, int idx) {
    return functions.stream().filter(func -> func.start <= idx && idx <= func.end)
        .map(func -> func.function)
        .reduce(Function.identity(), Function::andThen);
    }

  @AllArgsConstructor
  public static class Pipeline {
    protected List<Function<Double, Double>> functions;

    public Pipeline() {
      this(new LinkedList<>());
    }

    public void add(Function<Double, Double> func) {
      functions.add(func);
    }

    public Double run(Double in) {
      return functions.stream().reduce(Function.identity(), Function::andThen).apply(in);
    }

    public Function<Double, Double> merge(Pipeline pipeline2) {
      functions.addAll(pipeline2.functions);
      return functions.stream().reduce(Function.identity(), Function::andThen);
    }
  }



  @AllArgsConstructor
  public static class FunctionRangeDescriptor {
    private int start, end;
    protected Function<Double, Double> function;

  }

}
