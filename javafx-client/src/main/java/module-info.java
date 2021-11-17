module com.example.javafxclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.web;
    requires lombok;
    requires com.fasterxml.jackson.annotation;
    requires spring.core;


    opens com.example.jfxclient to javafx.fxml;
    exports com.example.jfxclient;
    exports com.example.jfxclient.dto;
    exports com.example.jfxclient.controller;
    exports com.example.jfxclient.rest;
    exports com.example.jfxclient.table;
    exports com.example.jfxclient.handler;
    exports com.example.jfxclient.popup;
    opens com.example.jfxclient.controller to javafx.fxml;
    opens com.example.jfxclient.dto to javafx.fxml;
    opens com.example.jfxclient.rest to javafx.fxml;
    opens com.example.jfxclient.table to javafx.fxml;
    opens com.example.jfxclient.handler to javafx.fxml;
    opens com.example.jfxclient.popup to javafx.fxml;
}