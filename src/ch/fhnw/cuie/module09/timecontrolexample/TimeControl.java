package ch.fhnw.cuie.module09.timecontrolexample;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author Dieter Holz
 */
public class TimeControl extends Control {
    private static final String CONVERTIBLE_REGEX = "now|(\\d{1,2}[:]{0,1}\\d{0,2})";
    private static final String TIME_FORMAT_REGEX = "\\d{2}:\\d{2}";

    private static final String FORMATTED_TIME_PATTERN = "HH:mm";

    private static final Pattern CONVERTIBLE_PATTERN = Pattern.compile(CONVERTIBLE_REGEX);
    private static final Pattern TIME_FORMAT_PATTERN = Pattern.compile(TIME_FORMAT_REGEX);

    private static final PseudoClass MANDATORY_CLASS   = PseudoClass.getPseudoClass("mandatory");
    private static final PseudoClass INVALID_CLASS     = PseudoClass.getPseudoClass("invalid");
    private static final PseudoClass CONVERTIBLE_CLASS = PseudoClass.getPseudoClass("convertible");

    private final ObjectProperty<LocalTime> time   = new SimpleObjectProperty<>();
    private final IntegerProperty           hour   = new SimpleIntegerProperty();
    private final IntegerProperty           minute = new SimpleIntegerProperty();

    private final BooleanProperty mandatory   = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(MANDATORY_CLASS, get());
        }
    };
    private final BooleanProperty invalid     = new SimpleBooleanProperty(false) {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(INVALID_CLASS, get());
        }
    };
    private final BooleanProperty convertible = new SimpleBooleanProperty() {
        @Override
        protected void invalidated() {
            pseudoClassStateChanged(CONVERTIBLE_CLASS, get());
        }
    };

    private final BooleanProperty readOnly      = new SimpleBooleanProperty();
    private final StringProperty  label         = new SimpleStringProperty();
    private final List<String>    errorMessages = new ArrayList<>();

    private final StringProperty userFacingText = new SimpleStringProperty();


    public TimeControl() {
        initializeSelf();
        addValueChangeListener();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new TimeSkin(this);
    }

    public void convertUserInput() {
        LocalTime newTime = convertToTime(getUserFacingText());
        if (newTime != null) {
            if (newTime.equals(getTime())) {
                setUserFacingText(convertToString(newTime));
            } else {
                setTime(newTime);
            }
        }
    }

    public void reset() {
        setUserFacingText(getTime() != null ? convertToString(getTime()) : "");
    }

    public void increaseHour() {
        if (getTime() != null) {
            setTime(getTime().plusHours(1));
        }
    }

    public void decreaseHour() {
        if (getTime() != null) {
            setTime(getTime().minusHours(1));
        }
    }

    public void increaseMinute() {
        if (getTime() != null) {
            setTime(getTime().plusMinutes(1));
        }
    }

    public void decreaseMinute() {
        if (getTime() != null) {
            setTime(getTime().minusMinutes(1));
        }
    }

    public boolean isInTimeFormat(String userInput) {
        return userInput != null &&
               TIME_FORMAT_PATTERN.matcher(userInput).matches() &&
               convertToTime(userInput) != null;
    }

    public boolean isConvertible(String userInput) {
        return convertToTime(userInput) != null;
    }

    private boolean initializeSelf() {
        return getStyleClass().add("timeControl");
    }

    private void addValueChangeListener() {
        userFacingText.addListener((observable, oldValue, userInput) -> {
            boolean convertible    = isConvertible(userInput);
            boolean isInTimeFormat = isInTimeFormat(userInput);

            setConvertible(convertible && !isInTimeFormat);

            boolean invalidInput = !convertible && !isInTimeFormat;
            if (isMandatory()) {
                setInvalid(invalidInput || userInput.isEmpty());
            } else {
                setInvalid(!userInput.isEmpty() && invalidInput);
            }

            if (isInTimeFormat) {
                convertUserInput();
            }
        });

        timeProperty().addListener((observable, oldValue, newValue) -> {
            setInvalid(false);
            setUserFacingText(convertToString(newValue));
            setHour(newValue.getHour());
            setMinute(newValue.getMinute());
        });

        hourProperty().addListener((observable, oldValue, newValue) -> {
            int hour = newValue.intValue();
            if(getTime() != null && getTime().getHour() != hour) {
                setTime(LocalTime.of(hour, getTime().getMinute()));
            }
            else if(getTime() == null){
                setTime(LocalTime.of(hour, 0));
            }
        });

        minuteProperty().addListener((observable, oldValue, newValue) -> {
            int minute = newValue.intValue();
            if(getTime()!= null && getTime().getMinute() != minute){
                setTime(LocalTime.of(getTime().getHour(), minute));
            }
            else if(getTime() == null){
                setTime(LocalTime.of(0, minute));
            }
        });
    }


    private LocalTime convertToTime(String userInput) {
        boolean matches = CONVERTIBLE_PATTERN.matcher(userInput).matches();
        if (!matches) {
            return null;
        }

        int hour;
        int minute;
        if (userInput.equals("now")) {
            LocalTime now = LocalTime.now();
            hour = now.getHour();
            minute = now.getMinute();
        } else if (userInput.contains(":")) {
            hour = Integer.parseInt(userInput.substring(0, userInput.indexOf(':')));
            String minuteString = userInput.substring(userInput.indexOf(':') + 1);
            minute = minuteString.isEmpty() ? 0 : Integer.parseInt(minuteString);
        } else if(userInput.length() > 2){
            hour = Integer.parseInt(userInput.substring(0, 2));
            minute = Integer.parseInt(userInput.substring(2));;
        } else {
            hour = Integer.parseInt(userInput);
            minute = 0;
        }

        if (hour > 23 || minute > 59) {
            return null;
        } else {
            return LocalTime.of(hour, minute);
        }
    }

    private String convertToString(LocalTime newValue) {
        return newValue.format(DateTimeFormatter.ofPattern(FORMATTED_TIME_PATTERN));
    }

    // all the getters and setters

    public LocalTime getTime() {
        return time.get();
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public boolean isReadOnly() {
        return readOnly.get();
    }

    public BooleanProperty readOnlyProperty() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public boolean isMandatory() {
        return mandatory.get();
    }

    public BooleanProperty mandatoryProperty() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory.set(mandatory);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public boolean getInvalid() {
        return invalid.get();
    }

    public BooleanProperty invalidProperty() {
        return invalid;
    }

    public void setInvalid(boolean invalid) {
        this.invalid.set(invalid);
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getUserFacingText() {
        return userFacingText.get();
    }

    public StringProperty userFacingTextProperty() {
        return userFacingText;
    }

    public void setUserFacingText(String userFacingText) {
        this.userFacingText.set(userFacingText);
    }

    public boolean isInvalid() {
        return invalid.get();
    }

    public boolean isConvertible() {
        return convertible.get();
    }

    public BooleanProperty convertibleProperty() {
        return convertible;
    }

    public void setConvertible(boolean convertible) {
        this.convertible.set(convertible);
    }

    public int getHour() {
        return hour.get();
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
