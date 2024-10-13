/* (C)2024 */
package com.nbmp.waveform.application;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.util.BuilderFactory;

import org.springframework.context.ApplicationContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpringFXMLLoader<T> {
  private final URL location;
  private final ApplicationContext applicationContext;
  private Charset charset = Charset.defaultCharset();
  private T controller;

  private boolean loaded;

  public Parent load() throws IOException {
    if (loaded) {
      throw new IllegalStateException("Cannot load fxml multiple times");
    }
    FXMLLoader loader = new FXMLLoader();
    loader.setCharset(charset);
    loader.setLocation(location);
    loader.setControllerFactory(applicationContext::getBean);
    loader.setBuilderFactory(
        new BuilderFactory() {
          JavaFXBuilderFactory defaultFactory = new JavaFXBuilderFactory();

          @Override
          public javafx.util.Builder<?> getBuilder(Class<?> type) {
            String[] beanNames = applicationContext.getBeanNamesForType(type);
            if (beanNames.length == 1) {
              return (javafx.util.Builder<Object>) () -> applicationContext.getBean(beanNames[0]);
            } else {
              return defaultFactory.getBuilder(type);
            }
          }
        });
    Parent root = loader.load();
    controller = loader.getController();
    loaded = true;
    return root;
  }

  public T getController() {
    if (!loaded) {
      throw new IllegalStateException("Controller is only available after loading");
    }
    return controller;
  }
}
