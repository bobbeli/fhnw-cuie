package ch.fhnw.cuie.module06.statusicon;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * @author Dieter Holz
 */
public class StatusIcon extends Region {
    public enum Status {

        CLOSED_HORIZONZAL(new Point2D(25, 40), new Point2D(75, 40),
                          new Point2D(25, 50), new Point2D(75, 50),
                          new Point2D(25, 60), new Point2D(75, 60)),

        CLOSED_VERTICAL(new Point2D(40, 35), new Point2D(40, 65),
                        new Point2D(50, 35), new Point2D(50, 65),
                        new Point2D(60, 35), new Point2D(60, 65)),

        OPEN_LEFT(new Point2D(25, 50), new Point2D(45, 40),
                  new Point2D(25, 50), new Point2D(75, 50),
                  new Point2D(25, 50), new Point2D(45, 60)),

        OPEN_RIGHT(new Point2D(55, 40), new Point2D(75, 50),
                   new Point2D(25, 50), new Point2D(75, 50),
                   new Point2D(55, 60), new Point2D(75, 50));

        private final Point2D line1Start;
        private final Point2D line2Start;
        private final Point2D line3Start;

        private final Point2D line1End;
        private final Point2D line2End;
        private final Point2D line3End;

        Status(Point2D line1Start, Point2D line1End, Point2D line2Start, Point2D line2End, Point2D line3Start, Point2D line3End) {
            this.line1Start = line1Start;
            this.line1End   = line1End;
            this.line2Start = line2Start;
            this.line2End   = line2End;
            this.line3Start = line3Start;
            this.line3End   = line3End;
        }

        public Point2D getLine1Start() {
            return line1Start;
        }

        public Point2D getLine2Start() {
            return line2Start;
        }

        public Point2D getLine3Start() {
            return line3Start;
        }

        public Point2D getLine1End() {
            return line1End;
        }

        public Point2D getLine2End() {
            return line2End;
        }

        public Point2D getLine3End() {
            return line3End;
        }
    }

    private static final String FONTS_CSS = "fonts.css";
    private static final String STYLE_CSS = "style.css";

    private static final double ARTBOARD_WIDTH  = 100;
    private static final double ARTBOARD_HEIGHT = 100;

    private static final double ASPECT_RATIO = ARTBOARD_WIDTH / ARTBOARD_HEIGHT;

    private static final double MINIMUM_WIDTH  = 30;
    private static final double MINIMUM_HEIGHT = MINIMUM_WIDTH / ASPECT_RATIO;

    private static final double MAXIMUM_WIDTH = 800;

    // all parts
    private Line line1;
    private Line line2;
    private Line line3;

    private Circle frame;

    private Pane drawingPane;

    // all properties
    private final ObjectProperty<Status> status = new SimpleObjectProperty<>();

    public StatusIcon() {
        init();
        initializeParts();
        layoutParts();
        addEventHandlers();
        addValueChangedListeners();
        addBindings();

        setStatus(Status.CLOSED_HORIZONZAL);
    }

    private void init() {
        addStyleSheets(this);
        getStyleClass().add(getStyleClassName());
    }

    private void initializeParts() {
        line1 = new Line();
        line1.getStyleClass().add("line");
        line1.setMouseTransparent(true);

        line2 = new Line();
        line2.getStyleClass().add("line");
        line2.setMouseTransparent(true);

        line3 = new Line();
        line3.getStyleClass().add("line");
        line3.setMouseTransparent(true);

        frame = new Circle(50, 50, 50);
        frame.getStyleClass().add("frame");

        // always needed
        drawingPane = new Pane();
        drawingPane.setMaxSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setMinSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
        drawingPane.setPrefSize(ARTBOARD_WIDTH, ARTBOARD_HEIGHT);
    }

    private void layoutParts() {
        drawingPane.getChildren().addAll(frame, line1, line2, line3);
        getChildren().add(drawingPane);
    }

    private void addEventHandlers() {
        frame.setOnMouseClicked(event -> {
            int currentOrdinal = getStatus().ordinal();
            int nextOrdinal    = (currentOrdinal == Status.values().length - 1) ? 0 : currentOrdinal + 1;
            setStatus(Status.values()[nextOrdinal]);
        });
    }

    private void addValueChangedListeners() {

        // always needed
        widthProperty().addListener((observable, oldValue, newValue) -> resize());
        heightProperty().addListener((observable, oldValue, newValue) -> resize());
    }

    private void addBindings() {
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

    // some useful helper-methods

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

    public Status getStatus() {
        return status.get();
    }

    public ObjectProperty<Status> statusProperty() {
        return status;
    }

    public void setStatus(Status status) {
        this.status.set(status);
    }
}
