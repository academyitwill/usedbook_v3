
const fields = ["email", "nickname", "password"];
const validUtil = new ValidUtil(fields);

let validEmail = false;
let validNickname = false;
let validPassword = false;
let validPPC = false;   //Password PasswordConfirm


$("#email").on("input", function(){
    var email = $.trim($("#email").val());
    email = encodeURIComponent(email);
    fetch("/api/member/email/check/"+email, {
        method: "POST"
    })
    .then(result => {
        return result.json();   //응답결과 json파싱
    })
    .then(data => {
        if(data == true){
            validEmail = true;
            validUtil.successProcess("email");
        }else{
            validEmail = false;
            validUtil.errorProcess(data);
        }
    })
});


$("#nickname").on("input", function(){
    var nickname = $.trim($("#nickname").val());
    nickname = encodeURIComponent(nickname);
    fetch("/api/member/nickname/check/"+nickname, {
        method: "POST",
    })
    .then(result => {
        return result.json();   //응답결과 json파싱
    })
    .then(data => {
        if(data == true){
            validNickname = true;
            validUtil.successProcess("nickname");
        }else{
            validNickname = false;
            validUtil.errorProcess(data);
        }
    })
});

$("#password").on("input", function(){
    var password = $.trim($("#password").val());
    fetch("/api/member/password/check", {
        method: "POST",
        headers: {"Content-Type":"application/json"},
        body: JSON.stringify({"password":password}),
    })
    .then(result => {
        return result.json();   //응답결과 json파싱
    })
    .then(data => {
        if(data == true){
            validPassword = true;
            validUtil.successProcess("password");
        }else{
            //{cause: 'map[password]', code: 'parameter.validated', message: '비밀번호는 숫자, 영어, 특수문자 각각 1개 이상 포함하고 2자 ~ 20자로 입력해주세요', httpStatus: 'BAD_REQUEST'}
            validPassword = false;
            data.cause = data.cause.substr(4, 8);
            validUtil.errorProcess(data);
        }
    })
});




//비밀번호, 비밀번호 확인 일치 체크
$("#password, #passwordConfirm").on("change keyup", function(){
    var password = $.trim($("#password").val());
    var passwordConfirm = $.trim($("#passwordConfirm").val());

    if(password != null && password != ""){
        if(password != passwordConfirm){
            validUtil.inputClass("passwordConfirm", "red");
            validUtil.feedbackClass("passwordConfirm", "block", "비밀번호가 일치하지 않습니다.");
            validPPC = false;
        }else{
            validUtil.inputClass("passwordConfirm", "green");
            validUtil.feedbackClass("passwordConfirm", "none", null);
            validPPC = true;
        }
    }else{
        validUtil.inputClass("password", "red");
        validUtil.inputClass("passwordConfirm", "red");
        validUtil.feedbackClass("passwordConfirm", "block", "비밀번호를 입력해주세요.");
    }
});


function registerSubmit(){
    if(validEmail && validNickname && validPassword && validPPC){   //submit가능

        var email = $.trim($("#email").val());
        var nickname = $.trim($("#nickname").val());
        var password = $.trim($("#password").val());

        fetch("/api/member", {
            method: "POST",
            headers: {"Content-Type":"application/json"},
            body: JSON.stringify({
                "email":email,
                "nickname":nickname,
                "password":password
            }),
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Something went wrong');
        })
        .then((responseJson) => {
            window.location.replace('/joinOk');
        })
        .catch((error) => {
            console.log(error)
        });
    }
}

//button활성화
$("#email, #nickname, #password, #passwordConfirm").on("keyup", function(){
    if(validEmail && validNickname && validPassword && validPPC){   //ajax보다 위에있으면 하나 늦게 반응된다
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





