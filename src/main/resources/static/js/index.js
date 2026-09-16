$(document).ready(function () {

    // 🔹 即時密碼長度檢查
    $('#password').on('input', function () {
        const val = $(this).val();

        if (val.length > 0 && val.length < 6) {
            $('#msg').text('パスワードは6文字以上である必要があります！').css('color', 'red');
        } else {
            $('#msg').text('');
        }
    });

    //  登入核心邏輯
    function handleLogin() {

        const usernameValue = $('#username').val().trim();
        const passwordValue = $('#password').val().trim();

        // 防呆驗證
        if (usernameValue === '' || passwordValue === '') {
            $('#msg').text('ユーザー名とパスワードを入力してください！').css('color', 'red');
            return;
        }

        if (passwordValue.length < 6) {
            $('#msg').text('パスワードは6文字以上である必要があります！').css('color', 'red');
            return;
        }

        $('#msg').text('ログイン中...').css('color', 'black');

        //  關鍵：以 JSON 格式發送請求
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
                    $('#msg').css('color', 'green').text('ログイン成功！ページを移動しています...');

                    // 將後端回傳的權限角色寫入 localStorage（預設為 'EMPLOYEE'）
                    localStorage.setItem('userRole', result.role || 'EMPLOYEE');

                    // 延遲 1 秒後跳轉至系統入口頁
                    setTimeout(function () {
                        window.location.href = '/portalHome'; 
                    }, 1000);

                } else {
                    $('#msg').css('color', 'red').text(result.message || 'ユーザー名またはパスワードが正しくありません！');
                }
            },

            error: function (xhr) {

                console.log("エラーステータス:", xhr.status);
                console.log("レスポンス内容:", xhr.responseText);

                if (xhr.status === 415) {
                    $('#msg').css('color', 'red').text('データ形式エラー');
                } else if (xhr.status === 500) {
                    $('#msg').css('color', 'red').text('サーバーエラーが発生しました');
                } else {
                    $('#msg').css('color', 'red').text('ログインに失敗しました。しばらくしてからもう一度お試しください');
                }
            }
        });
    }

    //  點擊登入按鈕事件
    $('#confirmBtn').on('click', handleLogin);

    //  密碼輸入框按下 Enter 鍵直接送出
    $('#password').on('keypress', function (e) {
        if (e.key === 'Enter') {
            handleLogin();
        }
    });

    //  Modal 彈跳視窗控制
    $('#openModalBtn').on('click', function () {
        $('#loginModal').fadeIn();
        $('#msg').text('');
    });

    $('#closeBtn').on('click', function () {
        $('#loginModal'].fadeOut();
    });

    // 點擊 Modal 外部陰影處關閉視窗
    $(window).on('click', function (event) {
        if ($(event.target).is('#loginModal')) {
            $('#loginModal').fadeOut();
        }
    });

});