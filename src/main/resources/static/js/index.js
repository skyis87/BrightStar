$(document).ready(function () {

    // 🔹 即時密碼檢查
    $('#password').on('input', function () {
        const val = $(this).val();

        if (val.length > 0 && val.length < 6) {
            $('#msg').text('密碼長度至少需要 6 個字！').css('color', 'red');
        } else {
            $('#msg').text('');
        }
    });

    //  登入核心
    function handleLogin() {

        const usernameValue = $('#username').val().trim();
        const passwordValue = $('#password').val().trim();

        // 防呆
        if (usernameValue === '' || passwordValue === '') {
            $('#msg').text('請填寫帳號與密碼！').css('color', 'red');
            return;
        }

        if (passwordValue.length < 6) {
            $('#msg').text('密碼長度至少需要 6 個字！').css('color', 'red');
            return;
        }

        $('#msg').text('登入中...').css('color', 'black');

        //  關鍵：用 JSON 傳送
        $.ajax({
            url: '/api/login',
            type: 'POST',
            contentType: 'application/json',   
            dataType: 'json',
            data: JSON.stringify({             
                username: usernameValue,
                password: passwordValue
            }),

            success: function (result) {

				if (result.success) {
				    $('#msg').css('color', 'green').text('登入成功！頁面跳轉中...');

				    // 將後端回傳的權限角色寫入 localStorage（預設為 'EMPLOYEE'）
				    localStorage.setItem('userRole', result.role || 'EMPLOYEE');

					setTimeout(function () {
					    window.location.href = '/portalHome'; // 改成跳轉至入口頁
					}, 1000);
				

                } else {
                    $('#msg').css('color', 'red').text(result.message || '帳號或密碼錯誤！');
                }
            },

            error: function (xhr) {

                console.log("錯誤狀態:", xhr.status);
                console.log("回傳內容:", xhr.responseText);

                if (xhr.status === 415) {
                    $('#msg').css('color', 'red').text('資料格式錯誤（不是 JSON）');
                } else if (xhr.status === 500) {
                    $('#msg').css('color', 'red').text('伺服器錯誤（後端問題）');
                } else {
                    $('#msg').css('color', 'red').text('登入失敗，請稍後再試');
                }
            }
        });
    }

    //  按鈕點擊
    $('#confirmBtn').on('click', handleLogin);

    //  Enter 送出
    $('#password').on('keypress', function (e) {
        if (e.key === 'Enter') {
            handleLogin();
        }
    });

    //  Modal 控制
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