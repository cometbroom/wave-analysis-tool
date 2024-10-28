/* (C)2024 */
package com.nbmp.waveform.model.generation.output;

import java.util.EnumMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
@Getter
public class OutputStream {

  public static final int CHANNELS = 16;
  private final Map<Channel, Sinks.Many<Output>> channelSinks = new EnumMap<>(Channel.class);

  @PostConstruct
  public void init() {
    for (Channel channel : Channel.values()) {
      channelSinks.put(channel, Sinks.many().unicast().onBackpressureBuffer());
    }
  }

  /**
   * Returns a Flux of Output objects for the specified channel.
   *
   * @param channel the channel to get outputs from
   * @return Flux of Output objects
   */
  public Flux<Output> getChannelFlux(Channel channel) {
    return channelSinks.get(channel).asFlux();
  }

  /**
   * Adds outputs to the specified channels.
   *
   * @param index     the index of the output
   * @param channel1  output value for channel 1
   * @param channel2  output value for channel 2
   * @param channel3  output value for channel 3
   */
  public void addOutputs3Channel(int index, double channel1, double channel2, double channel3) {
    emitOutput(Channel.CH1, new Output(index, channel1));
    emitOutput(Channel.CH2, new Output(index, channel2));
    emitOutput(Channel.CH3, new Output(index, channel3));
  }

  private void emitOutput(Channel channel, Output output) {
    Sinks.Many<Output> sink = channelSinks.get(channel);
    if (sink != null) {
      sink.tryEmitNext(output);
    }
  }

  @RequiredArgsConstructor
  @Getter
  public enum Channel {
    CH1(0),
    CH2(1),
    CH3(2),
    CH4(3),
    CH5(4),
    CH6(5),
    CH7(6),
    CH8(7),
    CH9(8),
    CH10(9),
    CH11(10),
    CH12(11),
    CH13(12),
    CH14(13),
    CH15(14),
    CH16(15);

    private final int value;
  }

  // Assume Output is a simple POJO with index and value
  @Getter
  @RequiredArgsConstructor
  public static class Output {
    private final int index;
    private final double value;
  }
}
