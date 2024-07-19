(() => {
  console.log("Javascript - load widget script");
  const sdk = new window.WidgetSdk({
    baseUrl: 'https://sdk-test-git-main-jpb06s-projects.vercel.app',
    frameHeight: 500,
    frameWidth: 700,
    theme: 'neutral',
    token: 'jesuisuntoken',
    type: 'user'
  });
  window.sdk = sdk;

  window.sdk.init('widget-root');

  window.sdk.on("user.actions.doStuff", (data) => {
    console.info("Javascript - User.actions.doStuff listener");
    console.info(JSON.stringify(data));
    if(data.payload) {
      window.javaApp.userActionsDoStuff(data.payload);
    }
  })
})();


