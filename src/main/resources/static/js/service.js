
    var isLoggedIn = [[${currentUser != null}]];
    var serviceId = 0;
    var csrfHeaderName = 'X-CSRF-TOKEN';
    var csrfToken = '';
    $(document).ready(function () {
    $("#reservationForm").submit(function (e) {
        if (!isLoggedIn) {
            e.preventDefault();
            window.location.href = "/user/login";
        }
    });

    $("#del").click(function () {
    location.href = "/service/del?serviceId=[[${sdto.serviceId}]]";
});

    $("#contactButton").click(function () {
    location.href = "/chat/room/[[${sdto.serviceId}]]";
});

    $("#contactButton").click(function () {
    if (isLoggedIn) {
    $.ajax({
    type: "POST",
    url: "/chat/room",
    data: JSON.stringify({ serviceId: serviceId }),
    contentType: "application/json",
    beforeSend: function(xhr) {
    xhr.setRequestHeader(csrfHeaderName, csrfToken);
},
    success: function (data) {
    window.location.href = "/chat/room/" + data.roomId;
},
    error: function (xhr, status, error) {
    console.log("Error: " + error);
}
});
} else {
    window.location.href = "/user/login";
}
});

});
