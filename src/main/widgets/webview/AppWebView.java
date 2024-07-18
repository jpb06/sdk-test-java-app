package com.jpb06.widgets.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
 
public class AppWebView extends Application {
  private Scene scene;
  
  @Override public void start(Stage stage) {
    stage.setTitle("Widgets sdk Web view");
                
    this.scene = new Scene(new Browser(), 950, 700);

    stage.setScene(this.scene);
    stage.show();   
  }
 
  public static void main(String[] args){
    launch(args);
  }
}