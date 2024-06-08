$(document).ready(function () {
    $("#del").click(function () {
        const certificationId = $(this).data("certification-id");
        location.href = `/certification/del?certificationId=${encodeURIComponent(certificationId)}`;
    });

    $("#approve").click(function () {
        const userId = $(this).data("user-id");
        location.href = `/user/approve?userId=${encodeURIComponent(userId)}`;
    });

    $("#disapprove").click(function () {
        const userId = $(this).data("user-id");
        location.href = `/user/disapprove?userId=${encodeURIComponent(userId)}`;
    });
});
