package com.nbmp.waveform.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.nbmp.waveform")
@Configuration
public class AppConfig {
    public static final int SAMPLE_RATE = 1000;
    public static double timeStep = 0.01, totalTime = 5;
    public static String xLabel = "Time (s)", yLabel = "Amplitude";
}
