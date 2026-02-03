package com.valkryst.core;

import com.valkryst.controller.ConnectionScreenController;
import eu.hansolo.enzo.notification.Notification;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(final String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Notification.Notifier.setPopupLocation(primaryStage, Pos.BOTTOM_CENTER);

        final Scene scene = new Scene(new ConnectionScreenController(primaryStage).getView());
        scene.getStylesheets().add("global.css");
        scene.getRoot().getStyleClass().add("main-root");

        primaryStage.setTitle("Valkryst's WoW Bug Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
