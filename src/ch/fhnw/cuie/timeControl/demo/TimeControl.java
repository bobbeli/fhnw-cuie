package ch.fhnw.cuie.timeControl.demo;

import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.ClockBuilder;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.layout.*;

import java.time.*;

/**
 * This Class provides a Time Controll UI
 *
 */

public class TimeControl extends StackPane{

    private Clock clock;
    private Background background;
    private ZonedDateTime time;

    private final IntegerProperty                   hour   = new SimpleIntegerProperty();
    private final IntegerProperty                   minute = new SimpleIntegerProperty();

    public TimeControl(){
        init();
        layoutControl();
        addValueChangeListener();
    }

    public void init(){
        time = ZonedDateTime.now();
        clock = ClockBuilder
                .create()
                .prefSize(200, 100)
                .skinType(Clock.ClockSkinType.DB)
                .secondsVisible(false)
                .running(true)
                .build();

        clock.setDateVisible(true);
        clock.setTime(time);


    }

    public void layoutControl() {
        getChildren().add(clock);
    }

    private void addValueChangeListener() {
        hourProperty().addListener((observable, oldValue, newValue) -> {
            int hour = newValue.intValue();
            if(getTime() != null) {
                changeHour(1);
            }
            else if(getTime() == null){
                time = ZonedDateTime.now();
            }
        });

        minuteProperty().addListener((observable, oldValue, newValue) -> {
            int minute = newValue.intValue();
            if(getTime()!= null){
                changeMinute(1);
            }
            else if(getTime() == null){
                time = ZonedDateTime.now();
            }
        });
    }

    /**
     * Getters & Settters for all Properties
     */

    public void changeHour(long hour) {
        if( hour > 0 ){
            time = time.plusHours(hour);
        } else {
            time = time.minusHours(-hour);
        }
        clock.setTime(time);
    }

    public void changeMinute(long minute) {
        if( minute > 0 ){
            time = time.plusMinutes(minute);
        } else {
            time = time.minusMinutes(minute);
        }
        clock.setTime(time);
    }


    public ZonedDateTime getTime() {
        return time;
    }

    public IntegerProperty hourProperty() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour.set(hour);
    }

    public int getMinute() {
        return minute.get();
    }

    public IntegerProperty minuteProperty() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute.set(minute);
    }

}
