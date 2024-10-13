/* (C)2024 */
package com.nbmp.waveform.controller.component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabeledComboBox extends HBox implements Initializable {

  @FXML private HBox root;
  private String text;
  @FXML private Label label;
  @FXML private ComboBox<String> comboBox;

  public LabeledComboBox() {
    FXMLLoader fxmlLoader =
        new FXMLLoader(getClass().getResource("/components/LabeledComboBox.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    if (text != null) {
      label.setText(text);
    }
  }

  public void setText(String text) {
    this.text = text;
    if (label != null) {
      label.setText(text);
    }
  }

  public HBox getRoot() {
    return root;
  }
}
