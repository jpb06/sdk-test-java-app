# sdk-test-java-app

This is a POC showcasing communication between a Java heavy client and a next web app. The web app can be found [Here](https://github.com/jpb06/sdk-test).

## Next app <-> Heavy client Communication

![Worfklow](./assets/framebus.png).

We need two way communication:
- Java heavy client -> Next app.
- Next app -> Java heavy client.

To do so, [fseglard](https://github.com/fseglard) has written a sdk based on [post message api](https://developer.mozilla.org/en-US/docs/Web/API/Window/postMessage).

## Sdk API

### `init`

```tsx
WidgetSdk.init(elementId: string): void
```

The `init` function replaces an element in the DOM with an iframe that will target one of our next app widget pages.
Typical usage looks like this:

```tsx
<div id="my-widget" />
<script src="/libs/sdk.min.js" />
<script>
  const widget = new window.WidgetSdk({
    baseUrl: 'http://localhost:3000',
    frameHeight: 600,
    frameWidth: 900,
    theme: 'neutral',
    token: 'my-token',
    type: 'user',
  });

  widget.init('my-widget');
</script>
```

### `emit`

```typescript
WidgetSdk.emit<TEventName extends FramebusEventName>(
    eventName: TEventName, 
    payload: FramebusEventPayload<TEventName>
): void
```

The `emit` function sends an event.
Typical usage looks like this:

```typescript
widget.emit('user.actions.doStuff', {
  bro: 'cool', 
  struff: 25,
});
```

### `on`

```typescript
WidgetSdk.on<TEventName extends FramebusEventName>(
    eventName: TEventName, 
    listener: FramebusEventListener<TEventName>
): void
```

The `on` function creates a listener for an event type.
Typical usage looks like this:

```typescript
widget.on('user.get', ({ id }) => {
  console.info(`user.get event fired with id ${id}`);
  // Do something cool
});
```


## Java app

![Worfklow](./assets/java-app-workflow.png).

In order to do so, the strategy is to create a javascript sdk that will generate an iframe, and to rely on [post messages](https://developer.mozilla.org/en-US/docs/Web/API/Window/postMessage) to communicate between the java webview and our web app.

## Key code snippets

### Java -> Javascript communication

This part is easy enough. All we need to do is have a js script defined and execute a script via the `webEngine`:

```javascript
function getUser (id) {
    console.log(`trigger getUser ${id}`);
    window.sdk.emit('user.get', { id });
}
```

```java
webEngine.executeScript("getUser('128')");
```

### Javascript -> Java communication

We first need to create a bridge between our js and java. To do so, we will inject a class on `global.window`:

```java
public class JavascriptBridge {
    public void userAction(Object data) {
        System.out.println("Javascript -> Java - userAction(data) invoked");
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
        System.out.println("Java - userAction event payload: " + gson.toJson(data));
    }
}

webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>()
{ 
  @Override
  public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState)
  {
    if (newState == Worker.State.SUCCEEDED)
    {
      JSObject jsGlobalWindow = (JSObject) webEngine.executeScript("window");
      jsGlobalWindow.setMember("javaApp", new JavascriptBridge());
    }
  }
});
```

In our js, we will setup a listener that will call `javaApp.userAction`:

```javascript
window.sdk.on("user.action", (data) => {
  console.info("Javascript - User.action listener");
  
  if(data.payload) {
    window.javaApp.userAction(data.payload);
  }
})
```

