(function() {
    var signup = $('.container--static .button--signup');
    var login = $('.container--static .button--login');
    var signupContent = $('.container--sliding .slider-content.signup');
    var loginContent = $('.container--sliding .slider-content.login');
    var slider = $('.container--sliding');

    signup.on('click', function() {
        loginContent.css('display', 'none');
        signupContent.css('display', 'initial');
        slider.animate({
            'left': '30%'
        }, 350, 'easeOutBack');
    });

    login.on('click', function() {
        signupContent.css('display', 'none');
        loginContent.css('display', 'initial');
        slider.animate({
            'left': '70%'
        }, 350, 'easeOutBack');
    });
})();