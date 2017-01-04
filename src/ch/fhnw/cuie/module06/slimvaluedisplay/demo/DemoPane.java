package ch.fhnw.cuie.module06.slimvaluedisplay.demo;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

import ch.fhnw.cuie.module06.slimvaluedisplay.SlimValueDisplay;

/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {
    private SlimValueDisplay customControl;

    private TextField titleField;
    private TextField valueField;
    private Label     valueLabel;
    private TextField unitField;
    private TextField minValueField;
    private TextField maxValueField;

    private ColorPicker colorPicker;

    public DemoPane() {
        initializeControls();
        layoutControls();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        customControl = new SlimValueDisplay();

        titleField = new TextField();
        valueField = new TextField();
        valueLabel = new Label();
        unitField = new TextField();
        minValueField = new TextField();
        maxValueField = new TextField();

        colorPicker = new ColorPicker();
    }

    private void layoutControls() {
        VBox box = new VBox(titleField, valueField, valueLabel, unitField, new Label("min, max Value:"), minValueField, maxValueField, colorPicker);
        box.setSpacing(10);
        box.setPadding(new Insets(20));

        setCenter(customControl);
        setRight(box);
    }

    private void addBindings() {
        titleField.textProperty().bindBidirectional(customControl.titleProperty());
        unitField.textProperty().bindBidirectional(customControl.unitProperty());
        Bindings.bindBidirectional(valueField.textProperty(), customControl.valueProperty(), new NumberStringConverter());
        valueLabel.textProperty().bind(customControl.valueProperty().asString("%.2f"));

        Bindings.bindBidirectional(minValueField.textProperty(), customControl.minValueProperty(), new NumberStringConverter());
        Bindings.bindBidirectional(maxValueField.textProperty(), customControl.maxValueProperty(), new NumberStringConverter());

        colorPicker.valueProperty().bindBidirectional(customControl.baseColorProperty());

    }

}
