package ch.fhnw.cuie.timeControl.demo;

import java.time.*;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * @author Dieter Holz
 */
public class Pane extends BorderPane {
    private Label businessControl;
    private TimeControl clock;

    private Slider hourSlider;
    private Slider minuteSlider;

    private CheckBox  readOnlyBox;
    private CheckBox  mandatoryBox;
    private TextField labelField;

    private ZonedDateTime time;
    private Background background;

    private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>(LocalTime.now());

    public Pane() {
        initializeControls();
        layoutControls();
        addValueChangeListeners();
        addBindings();
    }

    private void initializeControls() {
        setPadding(new Insets(10));

        hourSlider = new Slider(0, 23, 0);
        minuteSlider = new Slider(0, 59, 0);

        readOnlyBox = new CheckBox();
        readOnlyBox.setSelected(false);

        mandatoryBox = new CheckBox();
        mandatoryBox.setSelected(true);

        labelField = new TextField("Time (HH:mm)");

        background = new Background(new BackgroundFill(Paint.valueOf("LightBlue"),null,null));

        clock = new TimeControl();
        this.setBackground(background);
    }

    private void layoutControls() {
        setCenter( clock);
        VBox box = new VBox(10,
                            new Label("Set Hour"),
                            hourSlider,
                            new Label("Set Minute"),
                            minuteSlider
                            );
        box.setPadding(new Insets(10));
        box.setSpacing(10);
        setRight(box);
    }

    private void addValueChangeListeners() {
    }

    private void addBindings() {
        hourSlider.valueProperty().bindBidirectional(clock.hourProperty());
        minuteSlider.valueProperty().bindBidirectional(clock.minuteProperty());
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
