/* (C)2024 */
package com.nbmp.waveform.controller.component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class LabeledTextField extends HBox {

  @FXML public HBox root;
  @FXML public Label label;
  @FXML public TextField textField;
  public int maximum = 5;
  public int minimum = 1;
  public String text;

  AtomicReference<Integer> value = new AtomicReference<>(0);
  private PropertyChangeSupport support = new PropertyChangeSupport(this);

  public LabeledTextField() {
    String resourceName = "/components/LabeledTextField.fxml";
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
    log.debug("LabeledTextField from {} bound to controller {}", resourceName, this);
  }

  @FXML
  public void initialize() {
    if (text != null) {
      label.setText(text);
    }
    setupEventListeners();
  }

  public void setupEventListeners() {
    textField.setOnMouseClicked((event) -> textField.selectAll());

    textField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
              }
              PauseTransition pause = new PauseTransition(Duration.millis(500));
              pause.setOnFinished(
                  event -> {
                    int newDuration = getValue();
                    if (newDuration > maximum) {
                      textField.setText(maximum + "");
                    }
                    if (newDuration < minimum) {
                      textField.setText(minimum + "");
                    }
                    setValue(getValue());
                  });
              pause.playFromStart();
            });
  }

  public int getValue() {
    return Integer.parseInt(textField.getText());
  }

  public void setValue(int newValue) {
    int oldValue = value.get();
    this.value.set(newValue);

    // For when component is still loading
    if (this.textField != null) {
      this.textField.setText(String.valueOf(newValue));
    }
    support.firePropertyChange("TextFieldValue", oldValue, newValue);
  }

  public void setText(String text) {
    this.text = text;
    if (text != null) {
      label.setText(text);
    }
  }

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void addListener(Consumer<Integer> consumer) {
    addPropertyChangeListener(
        evt -> {
          if (evt.getNewValue() != null) {
            try {
              consumer.accept((int) evt.getNewValue());
            } catch (ClassCastException ex) {
              System.out.println("ClassCastException occurred: " + ex.getMessage());
            }
          }
        });
  }

  public HBox getRoot() {
    return root;
  }
}
