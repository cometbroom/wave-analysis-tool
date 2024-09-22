/* (C)2024 */
package com.nbmp.waveform.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableInt;

public class GlobalUtils {
  private static Map<String, MutableInt> counterPerClass = new LinkedHashMap<>();

  public static MutableInt getCounter(String className) {
    if (!counterPerClass.containsKey(className)) counterPerClass.put(className, new MutableInt());

    return counterPerClass.get(className);
  }
}
