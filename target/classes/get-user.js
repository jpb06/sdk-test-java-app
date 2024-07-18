function getUser (id) {
    console.log(`trigger getUser ${id}`);
    window.sdk.emit('user.get', { id });
}