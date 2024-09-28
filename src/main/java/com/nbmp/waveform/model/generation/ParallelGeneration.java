package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.view.GraphDashboard;
import com.nbmp.waveform.model.guides.Guide;
import javafx.concurrent.Task;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParallelGeneration extends Generator {
    protected List<Guide> allGuides = new LinkedList<>();

    public ParallelGeneration(double timeStep, double totalTime, Guide... guides) {
        super(timeStep, totalTime);
    }

    public ParallelGeneration(Guide... guides) {
        allGuides.addAll(Arrays.asList(guides));
    }

    public void generate() {
        allGuides.forEach(guide -> {
            Task<Void> task =
                    new Task<>() {
                        @Override
                        protected Void call() {
                            for (double t = 0; t < totalTime; t += timeStep) {
                                guide.addPoint(t, timeStep);
                            }
                            return null;
                        }
                    };
            new Thread(task).start();
        });
        for (double t = 0; t < totalTime; t += timeStep) {
            for (var guide : allGuides) {
                guide.addPoint(t, timeStep);
            }
        }
    }

    public void generateAndBindToGraph(GraphDashboard graph) {
        graph.addSeries(allGuides.stream().map(Guide::getSeries).toList());
        generate();
    }

    public void regenerate() {
        allGuides.forEach(
                guide -> {
                    if (guide.isInteractive()) guide.getSeries().getData().clear();
                });
        generate();
    }

}
