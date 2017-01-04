package ch.fhnw.cuie.module07.controllibrary;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Dieter Holz
 */
class LinearSkin extends SkinBase<NumberRangeControl> {
	private static final String FONTS_CSS = "fonts.css";
	private static final String STYLE_CSS = "linearStyle.css";

	private static final double ARTBOARD_WIDTH  = 400;
	private static final double ARTBOARD_HEIGHT = 24;

	private static final double MINIMUM_WIDTH  = ARTBOARD_HEIGHT * 2.5;
	private static final double MINIMUM_HEIGHT = 10;

	private static final double MAXIMUM_WIDTH  = 1024;
	private static final double MAXIMUM_HEIGHT = 100;

	private static final String FORMAT = "%.0f";

	// all parts
	private Text   value;
	private Circle thumb;
	private Line   valueBar;
	private Line   scale;
	private Group  thumbGroup;

	private Pane drawingPane;

	private double mouseX;
	private double strokeWidthFromCSS;

	LinearSkin(NumberRangeControl control) {
		super(control);
		init();
		initializeParts();
		layoutParts();
		addEventHandlers();
		addValueChangedListeners();
		addBindings();

		Platform.runLater(this::relocateValue);
	}

	@Override
	protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
		super.layoutChildren(contentX, contentY, contentWidth, contentHeight);
		resize();
	}

	private void init() {
		addStyleSheets(getSkinnable());
	}

	private void initializeParts() {
		value = new Text(0, ARTBOARD_HEIGHT * 0.5, String.format(FORMAT, getSkinnable().getValue()));
		value.getStyleClass().add("value");
		value.setTextOrigin(VPos.CENTER);
		value.setTextAlignment(TextAlignment.CENTER);
		value.setMouseTransparent(true);

		thumb = new Circle(ARTBOARD_HEIGHT * 0.5, ARTBOARD_HEIGHT * 0.5, ARTBOARD_HEIGHT * 0.5);
		thumb.getStyleClass().add("thumb");

		thumbGroup = new Group(thumb, value);

		valueBar = new Line();
		valueBar.getStyleClass().add("valueBar");
		valueBar.setStrokeLineCap(StrokeLineCap.ROUND);
		applyCss(valueBar);
		strokeWidthFromCSS = valueBar.getStrokeWidth();

		scale = new Line();
		scale.getStyleClass().add("scale");
		scale.setStrokeLineCap(StrokeLineCap.ROUND);

		// always needed
		drawingPane = new Pane();
	}

	private void layoutParts() {
		drawingPane.getChildren().addAll(scale, valueBar, thumbGroup);
		getChildren().add(drawingPane);
	}

	private void addEventHandlers() {
		thumb.setOnMousePressed(event -> {
			mouseX = event.getSceneX();
		});

		thumb.setOnMouseDragged(event -> {
			getSkinnable().setAnimated(false);

			double delta    = ((getSkinnable().getMaxValue() - getSkinnable().getMinValue()) / (scale.getEndX() - scale.getStartX())) * (event.getSceneX() - mouseX);
			double newValue = Math.min(getSkinnable().getMaxValue(), Math.max(getSkinnable().getMinValue(), getSkinnable().getValue() + delta));
			getSkinnable().setValue(newValue);

			getSkinnable().setAnimated(true);
			mouseX = event.getSceneX();
		});

// thumbGroup.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, getSkinnable().getBaseColor().brighter(), 2d, 0d, 0d, 0d));

//		thumb.setOnMouseReleased(event -> {
//			thumbGroup.setEffect(null);
//		});

	}

	private void addValueChangedListeners() {
		getSkinnable().percentageProperty()
		              .addListener((observable, oldValue, newValue) -> {
			              double xPos = getThumbGroupXPos(newValue);
			              thumbGroup.setLayoutX(xPos);
			              valueBar.setEndX(xPos);
		              });
		getSkinnable().animatedValueProperty()
		              .addListener((observable, oldValue, newValue) -> {
			              value.setText(String.format(FORMAT, newValue.doubleValue()));
			              relocateValue();
		              });

	}

	private void addBindings() {
	}

	private void resize() {

		Insets padding         = getSkinnable().getPadding();
		double availableWidth  = getSkinnable().getWidth() - padding.getLeft() - padding.getRight();
		double availableHeight = getSkinnable().getHeight() - padding.getTop() - padding.getBottom();

		double actualHeight   = Math.max(Math.min(availableHeight, MAXIMUM_HEIGHT), MINIMUM_HEIGHT);
		double scalingFactorY = actualHeight / ARTBOARD_HEIGHT;
		double actualWidth    = Math.max(Math.min(availableWidth, MAXIMUM_WIDTH), MINIMUM_WIDTH * scalingFactorY);

		if (availableWidth > 0 && availableHeight > 0) {
			drawingPane.setMaxSize(actualWidth, actualHeight);
			drawingPane.setMinSize(actualWidth, actualHeight);
			drawingPane.relocate((getSkinnable().getWidth() - actualWidth) * 0.5, (getSkinnable().getHeight() - actualHeight) * 0.5);

			double centerY           = actualHeight * 0.5;
			double thumbHeight       = thumbGroup.getLayoutBounds().getHeight();
			double scaledStrokeWidth = strokeWidthFromCSS * scalingFactorY;
			double margin            = thumbHeight * 0.5 * scalingFactorY;

			scale.setStartX(margin);
			scale.setStartY(centerY);
			scale.setEndX(actualWidth - margin);
			scale.setEndY(centerY);
			scale.setStrokeWidth(scaledStrokeWidth);

			valueBar.setStartX(margin);
			valueBar.setStartY(centerY);
			valueBar.setEndY(centerY);
			valueBar.setEndX(getThumbGroupXPos(getSkinnable().getPercentage()));
			valueBar.setStrokeWidth(scaledStrokeWidth);

			thumbGroup.setLayoutY((actualHeight - thumbHeight) * 0.5);
			thumbGroup.setLayoutX(getThumbGroupXPos(getSkinnable().getPercentage()));
			thumbGroup.setScaleX(scalingFactorY);
			thumbGroup.setScaleY(scalingFactorY);
		}
	}

	private double getThumbGroupXPos(Number newValue) {
		return scale.getStartX() + (newValue.doubleValue() / 100.0) * (scale.getEndX() - scale.getStartX()) - thumbGroup.getLayoutBounds().getHeight() * 0.5;
	}

	private void relocateValue() {
		value.autosize();
		value.setX((thumbGroup.getLayoutBounds().getWidth() - value.getLayoutBounds().getWidth()) * 0.5);
	}

	private void applyCss(Node node) {
		Group group = new Group(node);
		group.getStyleClass().add("numberRangeControl");
		addStyleSheets(group);
		new Scene(group);
		node.applyCss();
	}

	private void addStyleSheets(Parent parent) {
		String fonts = getClass().getResource(FONTS_CSS).toExternalForm();
		parent.getStylesheets().add(fonts);

		String stylesheet = getClass().getResource(STYLE_CSS).toExternalForm();
		parent.getStylesheets().add(stylesheet);
	}

	// compute sizes

	@Override
	protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double horizontalPadding = leftInset + rightInset;

		return MINIMUM_WIDTH + horizontalPadding;
	}

	@Override
	protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double verticalPadding = topInset + bottomInset;

		return MINIMUM_HEIGHT + verticalPadding;
	}

	@Override
	protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double horizontalPadding = leftInset + rightInset;

		return ARTBOARD_WIDTH + horizontalPadding;
	}

	@Override
	protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double verticalPadding = topInset + bottomInset;

		return ARTBOARD_HEIGHT + verticalPadding;
	}

	@Override
	protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
		double horizontalPadding = leftInset + rightInset;

		return MAXIMUM_WIDTH + horizontalPadding;
	}

	@Override
	protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
		double verticalPadding = topInset + bottomInset;

		return MAXIMUM_HEIGHT + verticalPadding;
	}

}
