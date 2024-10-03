package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.model.guides.WaveGuide;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class WaveformGenerator extends Generator {
//    public double[] generateOnlyY(Guide guide, int duration) {
//        var data = new double[duration * (int) SAMPLE_RATE];
//        int i = 0;
//        for (double t = 0; t < duration; t += timeStep) {
//            data[i++] = guide.compute(t, timeStep);
//        }
//        return data;
//    }

    public double[][] generate(WaveGuide guide, int start, int duration) {
        int sampleCount = duration * (int) SAMPLE_RATE;
        var data = new double[sampleCount][2];
        int i = 0;
        for (double t = start; t < duration; t += timeStep) {
            data[i++] = new double[]{t, guide.compute(t, timeStep)};
        }
        return data;
    }

}