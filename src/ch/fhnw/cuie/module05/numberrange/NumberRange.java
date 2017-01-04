package ch.fhnw.cuie.module05.numberrange;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;

public class NumberRange extends Region {
    private static final double PREFERRED_SIZE = 300;
    private static final double MINIMUM_SIZE   = 75;
    private static final double MAXIMUM_SIZE   = 800;

    //declare all parts
    private Circle backgroundCircle;
    private Arc    bar;
    private Circle thumb;

    //declare all Properties

    // needed for resizing
    private Pane drawingPane;

    public NumberRange() {
        initializeSelf();
        initializeParts();
        layoutParts();
        addEventHandlers();
        addValueChangeListeners();
        setupBinding();
    }

    private void initializeSelf() {
        // load stylesheets
        String fonts = getClass().getResource("fonts.css").toExternalForm();
        getStylesheets().add(fonts);

        String stylesheet = getClass().getResource("style.css").toExternalForm();
        getStylesheets().add(stylesheet);

        // initialize resizing constraints
        Insets padding           = getPadding();
        double verticalPadding   = padding.getTop() + padding.getBottom();
        double horizontalPadding = padding.getLeft() + padding.getRight();
        setMinSize(MINIMUM_SIZE + horizontalPadding, MINIMUM_SIZE + verticalPadding);
        setPrefSize(PREFERRED_SIZE + horizontalPadding, PREFERRED_SIZE + verticalPadding);
        setMaxSize(MAXIMUM_SIZE + horizontalPadding, MAXIMUM_SIZE + verticalPadding);
    }

    private void initializeParts() {
        double center = PREFERRED_SIZE * 0.5;
        int    width  = 15;
        double radius = center - width;

        backgroundCircle = new Circle(center, center, radius);
        backgroundCircle.getStyleClass().add("backgroundCircle");

        bar = new Arc(center, center, radius, radius, 90.0, -180.0);
        bar.getStyleClass().add("bar");
        bar.setType(ArcType.OPEN);

        thumb = new Circle(center, center + center - width, 13);
        thumb.getStyleClass().add("thumb");

        drawingPane = new Pane();
        drawingPane.getStyleClass().add("numberRange");
        drawingPane.setMaxSize(PREFERRED_SIZE, PREFERRED_SIZE);
        drawingPane.setMinSize(PREFERRED_SIZE, PREFERRED_SIZE);
        drawingPane.setPrefSize(PREFERRED_SIZE, PREFERRED_SIZE);
    }

    private void layoutParts() {
        // add all parts to drawingPane
        drawingPane.getChildren().addAll(backgroundCircle, bar, thumb);

        getChildren().add(drawingPane);
    }

    private void addEventHandlers() {

    }

    private void addValueChangeListeners() {
    }

    private void setupBinding() {
    }

    private double angle(double cx, double cy, double x, double y) {
        double a      = x - cx;
        double b      = cy - y;
        double radius = Math.sqrt(a * a + b * b);

        double alpha = 0;

        if (a > 0 && b > 0) {
            alpha = Math.toDegrees(Math.asin(a / radius));
        } else if (a > 0 && b <= 0) {
            alpha = 90 + Math.toDegrees(Math.asin(-b / radius));
        } else if (a <= 0 && b <= 0) {
            alpha = 180 + Math.toDegrees(Math.asin(-a / radius));
        } else {
            alpha = 270 + Math.toDegrees(Math.asin(b / radius));
        }
        return alpha;
    }

    private Point2D pointOnCircle(double cX, double cY, double radius, double angle) {
        return new Point2D(cX - (radius * Math.cos(Math.toRadians(angle - 90))),
                           cY + (radius * Math.sin(Math.toRadians(angle - 90))));
    }

    //resize by scaling
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        resize();
    }

    private void resize() {
        double width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        double height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        double size   = Math.max(Math.min(Math.min(width, height), MAXIMUM_SIZE), MINIMUM_SIZE);

        double scalingFactor = size / PREFERRED_SIZE;

        if (width > 0 && height > 0) {
            drawingPane.relocate((getWidth() - PREFERRED_SIZE) * 0.5, (getHeight() - PREFERRED_SIZE) * 0.5);
            drawingPane.setScaleX(scalingFactor);
            drawingPane.setScaleY(scalingFactor);
        }
    }
}
