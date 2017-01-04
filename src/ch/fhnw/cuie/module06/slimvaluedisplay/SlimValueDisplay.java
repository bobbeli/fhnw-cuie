package ch.fhnw.cuie.module06.slimvaluedisplay;

import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * @author Dieter Holz
 */
public class SlimValueDisplay extends Region {
    // needed for StyleableProperties
    private static final StyleablePropertyFactory<SlimValueDisplay> FACTORY = new StyleablePropertyFactory<>(Region.getClassCssMetaData());

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
        return FACTORY.getCssMetaData();
    }

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final double ARTBOARD_WIDTH  = 250;
    private static final double ARTBOARD_HEIGHT = 265;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 80;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 800;

    private static final String FORMAT = "%.1f";

    // all parts
    private Line   separator;
    private Text   titleLabel;
    private Text   valueLabel;
    private Text   unitLabel;
    private Circle barBackground;
    private Arc    bar;

    // all properties
    private final StringProperty title    = new SimpleStringProperty("TITLE");
    private final StringProperty unit     = new SimpleStringProperty("UNIT");
    private final DoubleProperty minValue = new SimpleDoubleProperty(0);
    private final DoubleProperty maxValue = new SimpleDoubleProperty(1000);
    private final DoubleProperty value    = new SimpleDoubleProperty();
    private final DoubleProperty animatedValue    = new SimpleDoubleProperty();

    private final BooleanProperty          timerIsRunning = new SimpleBooleanProperty(false);
    private final ObjectProperty<Duration> pulse          = new SimpleObjectProperty<>(Duration.seconds(0.5));

    // All Animations
    private final Timeline timeline = new Timeline();

    //CSS stylable properties
    private static final CssMetaData<SlimValueDisplay, Color> BASE_COLOR_META_DATA = FACTORY.createColorCssMetaData("-base-color", s -> s.baseColor);

    private final StyleableObjectProperty<Color> baseColor = new SimpleStyleableObjectProperty<Color>(BASE_COLOR_META_DATA, this, "baseColor") {
        @Override
        protected void invalidated() {
            setStyle(BASE_COLOR_META_DATA.getProperty() + ": " + (getBaseColor()).toString().replace("0x", "#"));
            applyCss();
        }
    };

    // all animations

    // all parts need to be children of the drawingPane
    private Pane drawingPane;

    private final AnimationTimer timer = new AnimationTimer() {
        private long lastTimerCall;

        @Override
        public void handle(long now) {
            if (now > lastTimerCall + (getPulse().toMillis() * 1_000_000L)) {
                performPeriodicTask();
                lastTimerCall = now;
            }
        }
    };

    public SlimValueDisplay() {
        initializeSelf();
        initializeParts();
        layoutParts();
        initializeAnimations();
        addEventHandlers();
        addValueChangedListeners();
        setupBindings();
    }

    private void initializeSelf() {
        addStyleSheets(this);
        getStyleClass().add(getStyleClassName());
    }

    private void initializeParts() {
        double cx = ARTBOARD_WIDTH * 0.5;

        separator = new Line(25, 15, 225, 15);
        separator.getStyleClass().add("separator");
        separator.setStrokeLineCap(StrokeLineCap.ROUND);

        titleLabel = createCenteredText(cx, 19, "title");
        titleLabel.setTextOrigin(VPos.TOP);

        valueLabel = createCenteredText(cx, 150, "value");

        unitLabel = createCenteredText(cx, 188, "unit");
        unitLabel.setTextOrigin(VPos.TOP);

        barBackground = new Circle(125, 150, 100);
        barBackground.getStyleClass().add("barBackground");

        bar = new Arc(125, 150, 100, 100, 90, 0);
        bar.getStyleClass().add("bar");
        bar.setType(ArcType.OPEN);

        // always needed
        drawingPane = new Pane();
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.getStyleClass().add("drawingPane");
    }

    private void layoutParts() {
        // add all your parts here
        drawingPane.getChildren().addAll(barBackground, bar, separator, titleLabel, valueLabel, unitLabel);

        getChildren().add(drawingPane);
    }

    private void initializeAnimations() {

    }

    private void addEventHandlers() {

    }

    private void addValueChangedListeners() {
        valueProperty().addListener((observable, oldValue, newValue) -> {
            timeline.stop();
            timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(500),new KeyValue(animatedValue, newValue)));
            timeline.play();
        });

        animatedValueProperty().addListener((observable, oldValue, newValue) -> {
            bar.setVisible(true);
            checkBoundaries(getValue());
            bar.setLength(getAngle(newValue));

        });

        minValueProperty().addListener((observable, oldValue, newValue) -> {
            bar.setVisible(true);
            checkBoundaries(getValue());
            bar.setLength(getAngle(getValue()));
        });

        maxValueProperty().addListener((observable, oldValue, newValue) -> {
            bar.setVisible(true);
            checkBoundaries(getValue());
            bar.setLength(getAngle(getValue()));
        });

        // if you need the timer
        timerIsRunning.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                timer.start();
            } else {
                timer.stop();
            }
        });

        // always needed
        widthProperty().addListener((observable, oldValue, newValue) -> resize());
        heightProperty().addListener((observable, oldValue, newValue) -> resize());
    }

    private void checkBoundaries(Number newValue) {
        setTimerIsRunning(newValue.doubleValue() > getMaxValue() || newValue.doubleValue() < getMinValue());
    }

    private double getAngle(Number value) {
        return -(3.6 * getPercentage(value));
    }

    private double getPercentage(Number newValue) {
        double min = getMinValue();
        double max = getMaxValue();
        return Math.max(1.0, Math.min(100.0, (newValue.doubleValue() - min) / ((max - min) / 100.0)));
    }

    private void setupBindings() {
        titleLabel.textProperty().bind(titleProperty());
        unitLabel.textProperty().bind(unitProperty());
        valueLabel.textProperty().bind(valueProperty().asString(FORMAT));
    }

    private void performPeriodicTask() {
        bar.setVisible(!bar.isVisible());
    }

    // some useful helper-methods

    private Text createCenteredText(String styleClass) {
        return createCenteredText(ARTBOARD_WIDTH * 0.5, ARTBOARD_HEIGHT * 0.5, styleClass);
    }

    private Text createCenteredText(double cx, double cy, String styleClass) {
        Text text = new Text();
        text.getStyleClass().add(styleClass);
        text.setTextOrigin(VPos.CENTER);
        text.setTextAlignment(TextAlignment.CENTER);
        double width = cx > ARTBOARD_WIDTH * 0.5 ? ((ARTBOARD_WIDTH - cx) * 2.0) : cx * 2.0;
        text.setWrappingWidth(width);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setY(cy);

        return text;
    }

    /*
     * angle in degrees, 0 is north
     */
    private double angle(double cx, double cy, double x, double y) {
        double deltaX = x - cx;
        double deltaY = y - cy;
        double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        double nx     = deltaX / radius;
        double ny     = deltaY / radius;
        double theta  = Math.toRadians(90) + Math.atan2(ny, nx);

        return Double.compare(theta, 0.0) >= 0 ? Math.toDegrees(theta) : Math.toDegrees((theta)) + 360.0;
    }

    /*
     * angle in degrees, 0 is north
     */
    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.sin(Math.toRadians(angle - 180))),
                           cY + (radius * Math.cos(Math.toRadians(angle - 180))));
    }

    /*
     * Needed if you want to know what's defined in css during initialization
     */
    private void applyCss(Node node) {
        Group group = new Group(node);
        group.getStyleClass().add(getStyleClassName());
        addStyleSheets(group);
        new Scene(group);
        node.applyCss();
    }

    private void resize() {
        Insets padding         = getPadding();
        double availableWidth  = getWidth() - padding.getLeft() - padding.getRight();
        double availableHeight = getHeight() - padding.getTop() - padding.getBottom();

        double width = Math.max(Math.min(Math.min(availableWidth, availableHeight * ASPECT_RATIO), MAXIMUM_WIDTH), MINIMUM_WIDTH);

        double scalingFactor = width / ARTBOARD_WIDTH;

        if (availableWidth > 0 && availableHeight > 0) {
            drawingPane.relocate((getWidth() - ARTBOARD_WIDTH) * 0.5, (getHeight() - ARTBOARD_HEIGHT) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }

    private void addStyleSheets(Parent parent) {
        String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
        parent.getStylesheets().add(fonts);

        String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
        parent.getStylesheets().add(stylesheet);
    }

    private String getStyleClassName() {
        String className = this.getClass().getSimpleName();

        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }

    // compute sizes

    @Override
    protected double computeMinWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return MINIMUM_WIDTH + horizontalPadding;
    }

    @Override
    protected double computeMinHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return MINIMUM_HEIGHT + verticalPadding;
    }

    @Override
    protected double computePrefWidth(double height) {
        Insets padding           = getPadding();
        double horizontalPadding = padding.getLeft() + padding.getRight();

        return ARTBOARD_WIDTH + horizontalPadding;
    }

    @Override
    protected double computePrefHeight(double width) {
        Insets padding         = getPadding();
        double verticalPadding = padding.getTop() + padding.getBottom();

        return ARTBOARD_HEIGHT + verticalPadding;
    }

    // getter and setter for all properties

    public Color getBaseColor() {
        return baseColor.get();
    }

    public StyleableObjectProperty<Color> baseColorProperty() {
        return baseColor;
    }

    public void setBaseColor(Color baseColor) {
        this.baseColor.set(baseColor);
    }

    public boolean isTimerIsRunning() {
        return timerIsRunning.get();
    }

    public BooleanProperty timerIsRunningProperty() {
        return timerIsRunning;
    }

    public void setTimerIsRunning(boolean timerIsRunning) {
        this.timerIsRunning.set(timerIsRunning);
    }

    public Duration getPulse() {
        return pulse.get();
    }

    public ObjectProperty<Duration> pulseProperty() {
        return pulse;
    }

    public void setPulse(Duration pulse) {
        this.pulse.set(pulse);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public double getMinValue() {
        return minValue.get();
    }

    public DoubleProperty minValueProperty() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue.set(minValue);
    }

    public double getMaxValue() {
        return maxValue.get();
    }

    public DoubleProperty maxValueProperty() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue.set(maxValue);
    }

    public double getValue() {
        return value.get();
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public double getAnimatedValue() {
        return animatedValue.get();
    }

    public DoubleProperty animatedValueProperty() {
        return animatedValue;
    }

    public void setAnimatedValue(double animatedValue) {
        this.animatedValue.set(animatedValue);
    }
}

