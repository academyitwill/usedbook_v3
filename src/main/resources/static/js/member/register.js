
const fields = ["email", "nickname", "password"];
const validUtil = new ValidUtil(fields);


 $("#email, #nickname, #password").on("input", function(){
    registerCheck(this);
});

var validENP = false;   //Email Nickname Password
var validPPC = false;   //Password PasswordConfirm


function registerCheck(element){
    var email = $.trim($("#email").val());
    var nickname = $.trim($("#nickname").val());
    var password = $.trim($("#password").val());

    $.ajax({
        type:"post",
        url:"/api/registerCheck",
        contentType: 'application/json',
        data: JSON.stringify({
            "email" : email,
            "nickname" : nickname,
            "password" : password
        }),
        success: function(){
            validENP = true;
            validUtil.successProcess();
        },
        error:function(error){
            validENP = false;
            validUtil.errorProcess(error.responseJSON);
            console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });
}


//비밀번호, 비밀번호 확인 일치 체크
$("#password, #passwordConfirm").on("change keyup", function(){
    var password = $.trim($("#password").val());
    var passwordConfirm = $.trim($("#passwordConfirm").val());

    if(password != null){
        if(password != passwordConfirm){
            validUtil.inputClass("passwordConfirm", "red");
            validUtil.feedbackClass("passwordConfirm", "block", "비밀번호가 일치하지 않습니다");
            validPPC = false;
        }else{
            validUtil.inputClass("passwordConfirm", "green");
            validUtil.feedbackClass("passwordConfirm", "none", null);
            validPPC = true;
        }
    }
});


function registerSubmit(){
    if(validENP && validPPC){   //submit가능
        $("#registerForm").submit();
    }
}

//button활성화
$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    if(validENP && validPPC){   //ajax보다 위에있으면 하나 늦게 반응된다
        $("#registerForm button").attr('disabled', false);
    }else{
        $("#registerForm button").attr('disabled', true);
    }
});


//공백제거
$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});





