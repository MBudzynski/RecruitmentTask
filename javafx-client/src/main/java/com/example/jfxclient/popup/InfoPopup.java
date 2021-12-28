package com.example.jfxclient.popup;

import com.example.jfxclient.handler.InfoPopupHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InfoPopup {


    public Stage createInfoPopup(String text, InfoPopupHandler handler){
        Stage stage = new Stage();
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER);
        pane.setSpacing(10);
        Label label = new Label(text);
        Button okButton = new Button("OK");
        okButton.setOnAction((x)->{
            stage.close();
            handler.handle();
        });
        pane.getChildren().addAll(label,okButton);
        stage.setScene(new Scene(pane,300,100));
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

}
