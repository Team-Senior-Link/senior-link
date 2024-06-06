function disableButtons(form) {
    var buttons = form.querySelectorAll('button');
    buttons.forEach(function(button) {
        button.disabled = true;
        button.innerText = "처리중"; // 버튼 텍스트 변경
    });
}