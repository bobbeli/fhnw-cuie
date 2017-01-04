package ch.fhnw.cuie.module06.simplecontroltemplateexample.demo;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import ch.fhnw.cuie.module06.simplecontroltemplateexample.ExampleControl;

/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {
    private ExampleControl customControl;

    private TextField   valueField;
    private Slider      angleSlider;
    private CheckBox    timerRunningBox;
    private Slider      pulseSlider;
    private ColorPicker colorPicker;

    public DemoPane() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        customControl = new ExampleControl();

        valueField = new TextField();
        valueField.setText("Wow!");

        angleSlider = new Slider(0, 360, 90);

        timerRunningBox = new CheckBox("Timer running");
        timerRunningBox.setSelected(false);

        pulseSlider = new Slider(0.5, 2.0, 1.0);
        pulseSlider.setShowTickLabels(true);
        pulseSlider.setShowTickMarks(true);

        colorPicker = new ColorPicker();
    }

    private void layoutControls() {
        setCenter(customControl);
        VBox box = new VBox(10, new Label("Control Properties"), valueField, angleSlider, timerRunningBox, pulseSlider, colorPicker);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addBindings() {
        customControl.textProperty().bindBidirectional(valueField.textProperty());
        customControl.angleProperty().bindBidirectional(angleSlider.valueProperty());
        customControl.timerIsRunningProperty().bindBidirectional(timerRunningBox.selectedProperty());

        customControl.pulseProperty().bind(Bindings.createObjectBinding(() -> Duration.seconds(pulseSlider.getValue()), pulseSlider.valueProperty()));

        colorPicker.valueProperty().bindBidirectional(customControl.baseColorProperty());
    }

}
