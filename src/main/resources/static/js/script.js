document.addEventListener('DOMContentLoaded', function () {
    var alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                alert.remove();
            }, 500);
        }, 4000);
    });

    var loginForm = document.querySelector('form[action*="login"]');
    if (loginForm) {
        loginForm.addEventListener('submit', function (event) {
            var username = document.getElementById('username');
            var password = document.getElementById('password');
            if (!username.value.trim() || !password.value.trim()) {
                event.preventDefault();
                alert('Please enter both username and password.');
            }
        });
    }
});