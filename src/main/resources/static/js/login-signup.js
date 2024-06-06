(function () {
    var signup = $('.container--static .button--signup');
    var login = $('.container--static .button--login');
    var signupContent = $('.container--sliding .slider-content.signup');
    var loginContent = $('.container--sliding .slider-content.login');
    var slider = $('.container--sliding');

    signup.on('click', function () {
        loginContent.css('display', 'none');
        signupContent.css('display', 'initial');
        slider.animate({
            'left': '30%'
        }, 350, 'easeOutBack');
    });

    login.on('click', function () {
        signupContent.css('display', 'none');
        loginContent.css('display', 'initial');
        slider.animate({
            'left': '70%'
        }, 350, 'easeOutBack');
    });
})();

function toggleGradeSection() {
    var roleSelect = document.getElementById('role');
    var gradeSection = document.getElementById('grade-section');
    if (roleSelect.value === 'CARERECEIVER') {
        gradeSection.style.display = 'block';
    } else {
        gradeSection.style.display = 'none';
    }
}

// DOM이 로드되었을 때 등급 섹션 토글
document.addEventListener('DOMContentLoaded', function() {
    toggleGradeSection();
});

function convertEmptyToNull() {
    var gradeSelect = document.getElementById('grade');
    if (gradeSelect.value === "") {
        gradeSelect.value = null;
    }
}
