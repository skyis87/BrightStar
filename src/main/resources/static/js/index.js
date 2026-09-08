$(document).ready(function () {
    // 1. 即時密碼長度檢查
    $('#password').on('input', function () {
        const val = $(this).val();
        if (val.length > 0 && val.length < 6) {
            $('#msg').text('密碼長度至少需要 6 個字！').css('color', 'red');
        } else {
            $('#msg').text('');
        }
    });

    // 2. 處理登入的核心功能
    function handleLogin() {
        const usernameValue = $('#username').val().trim();
        const passwordValue = $('#password').val().trim();

        if (usernameValue === '' || passwordValue === '') {
            $('#msg').text('請填寫帳號與密碼！').css('color', 'red');
            return;
        }

        if (passwordValue.length < 6) {
            $('#msg').text('密碼長度至少需要 6 個字！').css('color', 'red');
            return;
        }

        $('#msg').text('登入中...').css('color', 'black');

        $.ajax({
            url: '/api/login',
            type: 'post',
            data: {
                username: usernameValue,
                password: passwordValue
            },
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    $('#msg').css('color', 'green').text('登入成功！頁面跳轉中...');
                    localStorage.setItem('userRole', result.role);
                    setTimeout(function () {
                        window.location.href = 'notice-list.html';
                    }, 1000);
                } else {
                    $('#msg').css('color', 'red').text(result.message || '帳號或密碼錯誤！');
                }
            },
            error: function () {
                if (usernameValue === 'ga_admin' && passwordValue === 'admin123') {
                    $('#msg').css('color', 'green').text('測試登入成功（GA 權限）！');
                    localStorage.setItem('userRole', 'GA');
                    setTimeout(function () {
                        $('#loginModal').fadeOut();
                    }, 1000);
                } else {
                    $('#msg').css('color', 'red').text('伺服器連線失敗或帳號密碼錯誤！');
                }
            }
        });
    }

    // 3. 事件綁定與彈窗控制
    $('#confirmBtn').on('click', handleLogin);

    $('#password').on('keypress', function (e) {
        if (e.key === 'Enter') {
            handleLogin();
        }
    });

    $('#openModalBtn').on('click', function () {
        $('#loginModal').fadeIn();
        $('#msg').text('');
    });

    $('#closeBtn').on('click', function () {
        $('#loginModal').fadeOut();
    });

    $(window).on('click', function (event) {
        if ($(event.target).is('#loginModal')) {
            $('#loginModal').fadeOut();
        }
    });
});