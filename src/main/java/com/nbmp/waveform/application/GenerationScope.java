/* (C)2024 */
package com.nbmp.waveform.application;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class GenerationScope implements Scope {
  private static Object object = null;

  public static void refreshScope() {
    object = null;
  }

  @Override
  public @NotNull Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
    if (object == null) {
      object = objectFactory.getObject();
    }
    return object;
  }

  @Override
  public Object remove(@NotNull String name) {
    return object = null;
  }

  @Override
  public void registerDestructionCallback(String name, Runnable callback) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object resolveContextualObject(String key) {
    if (key.equals("generation")) {
      return object;
    }
    return null;
  }

  @Override
  public String getConversationId() {
    return "";
  }
}
