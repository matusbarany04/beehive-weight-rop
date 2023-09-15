importScripts("https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js");
//importScripts("https://cdn.jsdelivr.net/npm/@stomp/stompjs@5.0.0/bundles/stomp.umd.min.js");

const token = new URL(location).searchParams.get('token');
console.log(token);

let host = location.host.split(':')[0] + ":8080";

const socket = new WebSocket("ws://" + host + "/ws/304/hqsmdkat/websocket");

socket.addEventListener('open', function (event) {
    // WebSocket connection is open
    console.log('WebSocket connection opened');
    let connect = ["CONNECT\naccept-version:1.1,1.0\nheart-beat:10000,10000\n\n\u0000"];
    let subscribe = ["SUBSCRIBE\nid:sub-0\ndestination:/all/messages\n\n\u0000"];
    socket.send(JSON.stringify(connect));
    setTimeout(() => socket.send(JSON.stringify(subscribe)), 2000);

});

const privateSocket = new WebSocket("ws://" + host + "/ws/private/" + token + "/websocket");

privateSocket.addEventListener('open', function (event) {
    // WebSocket connection is open
    console.log('WebSocket connection opened');
    let connect = ["CONNECT\naccept-version:1.1,1.0\nheart-beat:10000,10000\n\n\u0000"];
    let subscribe = ["SUBSCRIBE\nid:sub-0\ndestination:/specific/" + token + "\n\n\u0000"];
    privateSocket.send(JSON.stringify(connect));
    setTimeout(() => privateSocket.send(JSON.stringify(subscribe)), 2000);

});

privateSocket.addEventListener('message', onMessage);
socket.addEventListener('message', onMessage);

function onMessage(event) {
    // Handle incoming WebSocket messages here
    if (event.data[0] === 'a') {
        console.log('Received message from server:', event.data);
        const options = {
            body: "this is test",
            icon: '../img/beeman.png',
            vibrate: [100, 50, 100],
            data: {
                dateOfArrival: Date.now(),
                primaryKey: 1,
            },
            actions: [
                {
                    action: 'explore',
                    title: 'Explore this new world',
                    icon: 'images/checkmark.png',
                },
                {
                    action: 'close',
                    title: "I don't want any of this",
                    icon: 'images/xmark.png',
                },
            ],
        };
        self.registration.showNotification('Test', options).then(r => console.log(r));
    }
}

socket.addEventListener('close', function (event) {
    // WebSocket connection is closed
    console.log('WebSocket connection closed');
});