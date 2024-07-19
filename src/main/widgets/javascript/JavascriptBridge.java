package  com.jpb06.widgets.app;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class JavascriptBridge {
    public void yolo() {
        System.out.println("Javascript -> Java - Yolo() invoked");
    }

    public void userActionsDoStuff(Object data) {
        System.out.println("Javascript -> Java - userAction(data) invoked");
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        System.out.println("Java - userAction event payload: " + gson.toJson(data));
    }
}
