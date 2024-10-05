package com.nbmp.waveform.model.generation;

import com.nbmp.waveform.controller.WavesRegister;
import com.nbmp.waveform.utils.GlobalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public class ChaosSynthesis {
    //Modulation index
    private double k = 0.5;
    private WaveformGenerator generator = new WaveformGenerator();
    private final WavesRegister wave1, wave2;

    public double[][][] compute(int duration) {
        int sampleCount = Generator.SAMPLE_RATE * duration;

        double[][][] data = new double[2][][];
        double[][] wave1Gen = new double[sampleCount][2];
        double[][] wave2Gen = new double[sampleCount][2];

        //Generate first point AKA initial conditions
        wave1Gen[0] = new double[]{0, wave1.guide().compute(0.0, Generator.timeStep)};
        wave2Gen[0] = new double[]{0, wave2.guide().compute(0.0, Generator.timeStep)};


        //Angular frequencies
        double omega1 = wave1.guide().getFrequency() * 2 * Math.PI;
        double omega2 = wave2.guide().getFrequency() * 2 * Math.PI;

        for (int i = 1; i < sampleCount; i++) {
            double t = i / (double) Generator.SAMPLE_RATE;

            //Basic coupling by using last computer value of the other wave
            double phi2 = k * wave1Gen[i-1][GenerationApi.AMPLITUDE];
            wave2Gen[i] =  new double[]{t, wave2.guide().getAmplitude() * Math.sin(omega2 * t + phi2)};

            double phi1 = k * wave2Gen[i-1][GenerationApi.AMPLITUDE];
            wave1Gen[i] = new double[]{t, wave1.guide().getAmplitude() * Math.sin(omega1 * t + phi1)};
        }
        return new double[][][]{wave1Gen, wave2Gen};
    }


}
