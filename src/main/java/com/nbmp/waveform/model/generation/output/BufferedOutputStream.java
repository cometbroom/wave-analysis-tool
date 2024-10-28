/* (C)2024 */
package com.nbmp.waveform.model.generation.output;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Getter
public class BufferedOutputStream extends OutputStream {

  private final int BUFFER_SIZE = 4096;
  private final Map<Channel, List<Output>> buffers = new ConcurrentHashMap<>();
  private final Map<Channel, Sinks.Many<List<Output>>> bufferedChannelSinks =
      new EnumMap<>(Channel.class);

  @PostConstruct
  public void init() {
    // Initialize a Sink for each channel
    for (Channel channel : Channel.values()) {
      buffers.put(channel, Collections.synchronizedList(new ArrayList<>()));
      bufferedChannelSinks.put(channel, Sinks.many().unicast().onBackpressureBuffer());
    }
  }

  /**
   * Returns a Flux of Output buffers for the specified channel.
   *
   * @param channel the channel to get outputs from
   * @return Flux of List<Output> objects
   */
  public Flux<List<Output>> getBufferedChannelFlux(Channel channel) {
    return bufferedChannelSinks.get(channel).asFlux();
  }

  /**
   * Adds outputs to the specified channels.
   *
   * @param index     the index of the output
   * @param channel1  output value for channel 1
   * @param channel2  output value for channel 2
   * @param channel3  output value for channel 3
   */
  @Override
  public void addOutputs3Channel(int index, double channel1, double channel2, double channel3) {
    addToBuffer(Channel.CH1, new Output(index, channel1));
    addToBuffer(Channel.CH2, new Output(index, channel2));
    addToBuffer(Channel.CH3, new Output(index, channel3));
  }

  private void addToBuffer(Channel channel, Output output) {
    List<Output> buffer = buffers.get(channel);
    buffer.add(output);

    if (buffer.size() >= BUFFER_SIZE) {
      emitBuffer(channel, new ArrayList<>(buffer));
      buffer.clear();
    }
  }

  private void emitBuffer(Channel channel, List<Output> outputBuffer) {
    Sinks.Many<List<Output>> sink = bufferedChannelSinks.get(channel);
    if (sink != null) {
      sink.tryEmitNext(outputBuffer);
    }
  }

  /**
   * Call this method to emit any remaining outputs in buffers, e.g., when shutting down.
   */
  public void flushBuffers() {
    for (Channel channel : Channel.values()) {
      List<Output> buffer = buffers.get(channel);
      if (!buffer.isEmpty()) {
        emitBuffer(channel, new ArrayList<>(buffer));
        buffer.clear();
      }
    }
  }
}
