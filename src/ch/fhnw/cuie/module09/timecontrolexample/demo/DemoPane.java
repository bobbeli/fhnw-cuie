package ch.fhnw.cuie.module09.timecontrolexample.demo;

import java.time.LocalTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import ch.fhnw.cuie.module09.timecontrolexample.TimeControl;

/**
 * @author Dieter Holz
 */
public class DemoPane extends BorderPane {
    private TimeControl businessControl;

    private Label     timeLabel;
    private Slider    hourSlider;
    private Slider    minuteSlider;

    private CheckBox  readOnlyBox;
    private CheckBox  mandatoryBox;
    private TextField labelField;

    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>(LocalTime.now());

    public DemoPane() {
        initializeControls();
        layoutControls();
        addValueChangeListeners();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        businessControl = new TimeControl();

        timeLabel = new Label();
        hourSlider = new Slider(0, 23, 0);
        minuteSlider = new Slider(0, 59, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField("Time (HH:mm)");
    }

    private void layoutControls() {
        setCenter(businessControl);
        VBox box = new VBox(10, new Label("Time Control Properties"),
                            timeLabel, hourSlider, minuteSlider,
                            new Label("readOnly"), readOnlyBox,
                            new Label("mandatory"), mandatoryBox,
                            new Label("Label"), labelField);
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }


    private void addValueChangeListeners() {
//        hourSlider.valueProperty().addListener((observable, oldValue, newValue) -> setStartTime(LocalTime.of((int)hourSlider.getValue(), (int) minuteSlider.getValue())));
//        minuteSlider.valueProperty().addListener((observable, oldValue, newValue) -> setStartTime(LocalTime.of((int)hourSlider.getValue(), (int) minuteSlider.getValue())));
//
//        startTimeProperty().addListener((observable, oldValue, newValue) -> {
//            hourSlider.setValue(newValue.getHour());
//            minuteSlider.setValue(newValue.getMinute());
//        });
    }

    private void addBindings() {

        timeLabel.textProperty().bind(startTimeProperty().asString());
        hourSlider.valueProperty().bindBidirectional(businessControl.hourProperty());
        minuteSlider.valueProperty().bindBidirectional(businessControl.minuteProperty());


        businessControl.timeProperty().bindBidirectional(startTimeProperty());

        businessControl.readOnlyProperty().bind(readOnlyBox.selectedProperty());
        businessControl.mandatoryProperty().bind(mandatoryBox.selectedProperty());
        businessControl.labelProperty().bind(labelField.textProperty());
    }

    public LocalTime getStartTime() {
        return startTime.get();
    }

    public ObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
    }
}
