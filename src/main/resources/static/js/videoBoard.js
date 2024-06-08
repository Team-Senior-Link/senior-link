$(document).ready(function () {
    $("#del").click(function () {
        const videoBoardId = $(this).data("videoboard-id");
        if (videoBoardId) {
            location.href = `/videoBoard/del?videoBoardId=${encodeURIComponent(videoBoardId)}`;
        } else {
            alert("비디오 게시판 ID가 유효하지 않습니다.");
        }
    });
});
