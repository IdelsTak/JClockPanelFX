package com.github.idelstak.jclockpanelfx;

import java.util.Calendar;
import java.util.Date;
import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class JClockPanelFX extends Application {

    private static final Calendar CALENDAR = Calendar.getInstance();
    private boolean hasSeconds;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Analog Clock");

        // Create a Pane to hold the clock elements
        Pane pane = new Pane();

        // Set the clock size
        double clockSize = 400;
        pane.setPrefSize(clockSize, clockSize);

        // Create clock elements
        Circle clockFace = new Circle(clockSize / 2, clockSize / 2, clockSize / 2 - 10);
        clockFace.setFill(Color.TRANSPARENT);
        clockFace.setStroke(Color.BLACK);

        Circle centerPoint = new Circle(clockSize / 2, clockSize / 2, 5);
        centerPoint.setFill(Color.BLACK);

        Line hourHand = new Line(clockSize / 2, clockSize / 2, clockSize / 2, clockSize / 4);
        hourHand.setStrokeWidth(8);

        Line minuteHand = new Line(clockSize / 2, clockSize / 2, clockSize / 2, clockSize / 6);
        minuteHand.setStrokeWidth(4);

        Line secondHand = new Line(clockSize / 2, clockSize / 2, clockSize / 2, clockSize / 6);
        secondHand.setStroke(Color.RED);
        secondHand.setStrokeWidth(2);

        // Rotate the clock hands
        Rotate hourRotate = new Rotate(0, clockSize / 2, clockSize / 2);
        hourHand.getTransforms().add(hourRotate);

        Rotate minuteRotate = new Rotate(0, clockSize / 2, clockSize / 2);
        minuteHand.getTransforms().add(minuteRotate);

        Rotate secondRotate = new Rotate(0, clockSize / 2, clockSize / 2);
        secondHand.getTransforms().add(secondRotate);

        // Create a Group to hold clock elements and tickers
        Group clockGroup = new Group();

        // Create tickers
        for (int i = 0; i < 12; i++) {
            double angle = Math.toRadians(i * 30); // 30 degrees per hour
            double startX = clockSize / 2 + (clockSize / 2 - 30) * Math.sin(angle);
            double startY = clockSize / 2 - (clockSize / 2 - 30) * Math.cos(angle);
            double endX = clockSize / 2 + (clockSize / 2 - 20) * Math.sin(angle);
            double endY = clockSize / 2 - (clockSize / 2 - 20) * Math.cos(angle);

            Line ticker = new Line(startX, startY, endX, endY);
            ticker.setStroke(Color.BLACK);
            clockGroup.getChildren().add(ticker);

            // Number labels
            Text numberLabel = new Text(String.valueOf(i == 0 ? 12 : i));
            double labelX = clockSize / 2 + (clockSize / 2 - 40) * Math.sin(angle) - 5;
            double labelY = clockSize / 2 - (clockSize / 2 - 40) * Math.cos(angle) + 5;
            numberLabel.setX(labelX);
            numberLabel.setY(labelY);
            clockGroup.getChildren().add(numberLabel);

            for (int j = 0; j < 5; j++) {
                double angleMinutes = Math.toRadians((i * 30) + (j * 6)); // 6 degrees per minute (30 degrees divided by 4)
                double startMinutesX = clockSize / 2 + (clockSize / 2 - 30) * Math.sin(angleMinutes);
                double startMinutesY = clockSize / 2 - (clockSize / 2 - 30) * Math.cos(angleMinutes);
                double endMinutesX = clockSize / 2 + (clockSize / 2 - 25) * Math.sin(angleMinutes);
                double endMinutesY = clockSize / 2 - (clockSize / 2 - 25) * Math.cos(angleMinutes);

                Line minuteTicker = new Line(startMinutesX, startMinutesY, endMinutesX, endMinutesY);
                minuteTicker.setStroke(Color.BLACK);
                clockGroup.getChildren().add(minuteTicker);
            }
        }

        // Add clock elements and tickers to the pane
        clockGroup.getChildren().addAll(clockFace, centerPoint, hourHand, minuteHand, secondHand);
        pane.getChildren().add(clockGroup);

        // Create a Timeline for updating the clock
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateClock(hourRotate, minuteRotate, secondRotate))
        );
        timeline.setCycleCount(Animation.INDEFINITE);

        // Set initial values
        hasSeconds = true;

        // Create the scene
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the clock
        timeline.play();
    }

    private void updateClock(Rotate hourRotate, Rotate minuteRotate,
            Rotate secondRotate) {
        CALENDAR.setTime(new Date());

        if (hasSeconds) {
            secondRotate.setAngle(getSecondAngle());
        }

        minuteRotate.setAngle(getMinuteAngle());
        hourRotate.setAngle(getHourAngle());
    }

    private double getSecondAngle() {
        double second = CALENDAR.get(Calendar.SECOND);
        return 360 * (second / 60.0);
    }

    private double getMinuteAngle() {
        double minute = CALENDAR.get(Calendar.MINUTE);
        return 360 * ((minute + getSecondAngle() / 360) / 60.0);
    }

    private double getHourAngle() {
        double hour = CALENDAR.get(Calendar.HOUR_OF_DAY);
        return 360 * ((hour + getMinuteAngle() / 360) / 12.0);
    }
}
