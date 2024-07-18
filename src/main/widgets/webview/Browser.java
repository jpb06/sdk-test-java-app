package com.jpb06.widgets.app;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebView;
import com.sun.javafx.webkit.WebConsoleListener;
import netscape.javascript.JSObject;

public class Browser extends Region {
  final WebView webView = new WebView();
    
  final EventHandler<WebErrorEvent> errorHandler = new EventHandler<WebErrorEvent>() {
    public void handle(WebErrorEvent event) {
      System.err.println("Webview error");
      System.err.println(event.getMessage());
    }
  };

  //private void executeSdkScript(WebEngine webEngine) {
  //  try {
  //    InputStream is = new URL("https://sdk-test-git-main-jpb06s-projects.vercel.app/libs/sdk.js").openStream();
  //    String jsContent = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
  //    webEngine.executeScript(jsContent);
  //  } catch (IOException e) {
  //   throw new RuntimeException(e);
  //  }
  //}

  //private String readFromInputStream(InputStream inputStream) throws IOException {
  //  StringBuilder resultStringBuilder = new StringBuilder();
  //
  //  try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
  //    String line;
  //    while ((line = br.readLine()) != null) {
  //      resultStringBuilder.append(line).append("\n");
  //    }
  //  }
  //
  //  return resultStringBuilder.toString();
  //}

  //private void executeLoadScript(WebEngine webEngine) {
  //  try {
  //    ClassLoader classLoader = getClass().getClassLoader();
  //    InputStream loadScriptInputStream = classLoader.getResourceAsStream("load-widget.js");
  //    String loadScriptData = readFromInputStream(loadScriptInputStream);
  //    webEngine.executeScript(loadScriptData);
  //  } catch(IOException e) {
  //    throw new RuntimeException(e);
  //  }
  //}
    
  public Browser() {        
    WebEngine webEngine = webView.getEngine();
    
    WebConsoleListener.setDefaultListener((webView, message, lineNumber, sourceId) -> 
      System.out.println("Javascript - " + message + " (at line " + lineNumber + ")"));
  
    Button btn = new Button();
    btn.setText("framebus emit 'user.get' with { id: '128' }");
    btn.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (webEngine != null) 
        {
          webEngine.executeScript("getUser('128')");
        }
      }
    });
        
    webEngine.setJavaScriptEnabled(true);
    webEngine.setOnError(errorHandler);
    webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
    { 
      @Override
      public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState)
      {
        if (newState == Worker.State.SUCCEEDED)
        {
          JSObject jsGlobalWindow = (JSObject) webEngine.executeScript("window");
          jsGlobalWindow.setMember("javaApp", new JavascriptBridge());

          // executeSdkScript(webEngine);
          // executeLoadScript(webEngine);
        }
      }
    });
        
    File file = new File(getClass().getClassLoader().getResource("main.html").getFile());
    webEngine.load(file.toURI().toString());

    StackPane root = new StackPane();
    VBox box = new VBox();
    box.getChildren().add(btn);
    box.getChildren().add(webView);
    
    root.getChildren().add(box);
    getChildren().add(root);
  }
}