package com.nbmp.waveform.generation;

import javafx.scene.chart.XYChart;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jfree.data.general.Series;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYSeries;

public class BufferedSeries extends XYSeries {
    private double[][] buffer;
  private final MutableInt index = new MutableInt();

    @RequiredArgsConstructor
    public class Type {
        public static final int TIME = 0, AMPLITUDE = 1;
    }

    public BufferedSeries(Comparable key, int size) {
        super(key, false, false);
        buffer = new double[size][2];
    }

    @Override
    public void add(double t, double y) {
        if (index.intValue() == buffer.length) {
            index.setValue(0);
            for (double[] xyValues : buffer) {
                super.add(xyValues[Type.TIME], xyValues[Type.AMPLITUDE]);
            }
            buffer = new double[buffer.length][2];
        }
        buffer[index.getAndIncrement()] = new double[]{t, y};
    }
}
