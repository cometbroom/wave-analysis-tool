/* (C)2024 */
package com.nbmp.waveform.model.dto;

/**
 * Record representing a bi-channel time-amplitude series.
 */
public record BiTimeSeries(double[][] timeAmplitude1, double[][] timeAmplitude2) {}
