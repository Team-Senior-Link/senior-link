
    $(document).ready(function() {
    var roomId = /*[[${roomId}]]*/ [[${roomId}]];
    var username = /*[[${username}]]*/ '[[${username}]]';

    var sockJs = new SockJS("/stomp/chat");
    var stomp = Stomp.over(sockJs);

    function renderMessage(sender, timestamp, message) {
    var str = '';
    if (sender === username) {
    str = "<div class='col-6'>";
    str += "<div class='alert alert-primary'>";
    str += "<b>" + sender + " (" + timestamp + ") : " + message + "</b>";
    str += "</div></div>";
} else {
    str = "<div class='col-6'>";
    str += "<div class='alert alert-secondary'>";
    str += "<b>" + sender + " (" + timestamp + ") : " + message + "</b>";
    str += "</div></div>";
}
    return str;
}

    stomp.connect({}, function() {
    stomp.subscribe("/sub/chat/room/" + roomId, function(chat) {
    var content = JSON.parse(chat.body);
    var message = content.message;
    var sender = content.sender;
    var timestamp = new Date(content.timestamp).toLocaleString();
    var str = renderMessage(sender, timestamp, message);

    $("#msgArea").append(str);
});

    stomp.send('/pub/chat/enter', {}, JSON.stringify({
    roomId: roomId,
    sender: username
}));
});

    function sendMessage() {
    var msg = document.getElementById("msg");
    stomp.send('/pub/chat/message', {}, JSON.stringify({
    roomId: roomId,
    message: msg.value,
    sender: username
}));
    msg.value = '';
}

    $("#button-send").on("click", function(e) {
    sendMessage();
});

    $("#msg").on("keypress", function(e) {
    if (e.key === "Enter") {
    sendMessage();
}
});
});
