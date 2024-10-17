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

/**
 * A custom component that combines a TextField with a Label within an HBox layout.
 * This component is used to provide labeled numeric input functionality in the UI.
 */
@Getter
@Setter
@Slf4j
public class LabeledTextField extends HBox {

  @FXML public HBox root;
  @FXML public Label label;
  @FXML public TextField textField;
  public int maximum = 5000;
  public int minimum = 1;
  public String text;

  private final AtomicReference<Integer> value = new AtomicReference<>(0);
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  /**
   * Constructor that loads the LabeledTextField component from the FXML file.
   */
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

  /**
   * Sets up the event listeners for the TextField to handle user interactions.
   */
  public void setupEventListeners() {
    textField.setOnMouseClicked((event) -> textField.selectAll());

    textField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
              }
              PauseTransition pause = new PauseTransition(Duration.millis(2000));
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

  /**
   * Gets the current value from the TextField.
   *
   * @return the current value as an integer
   */
  public int getValue() {
    return Integer.parseInt(textField.getText());
  }

  /**
   * Sets the value of the TextField and notifies any registered listeners.
   *
   * @param newValue the new value to set in the TextField
   */
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

  /**
   * Adds a PropertyChangeListener to be notified when the value changes.
   *
   * @param pcl the listener to be added
   */
  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  /**
   * Adds a listener to be notified when the value of the TextField changes.
   *
   * @param consumer the action to perform when the value changes
   */
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
