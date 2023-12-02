



self.addEventListener('push', function(event) {
    const payload = event.data.json();
    console.log(payload);
    const options = {
        title: payload.title,
        body: payload.message,
        icon: "../img/beeman.png",
        vibrate: [100, 50, 100],
    };
    event.waitUntil(self.registration.showNotification(options.title, options));
});

/*const socket = new WebSocket("ws://" + location.host + "/websocket/connect");

socket.onopen = (event) => {
    console.log("WebSocket connection opened:", event);

    socket.send("hello");
}

socket.onmessage = (msg) => {
    console.log("Received message from server:", msg);
    const options = {
        body: msg.data,
        icon: "../img/beeman.png",
        vibrate: [100, 50, 100],
        data: {
            dateOfArrival: Date.now(),
            primaryKey: 1,
        },
        actions: [
            {
                action: "explore",
                title: "Explore this new world",
                icon: "images/checkmark.png",
            },
            {
                action: "close",
                title: "I don't want any of this",
                icon: "images/xmark.png",
            },
        ],
    };
    self.registration.showNotification("New message", options);
};*/