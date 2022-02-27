
const fields = ["email", "password"];
const validUtil = new ValidUtil(fields);


function loginSubmit(){
    var email = $.trim($("#email").val());
    var password = $.trim($("#password").val());

    $.ajax({
        type:"post",
        url:"/api/loginCheck",
        contentType: 'application/json',
        data: JSON.stringify({
            "email":email,
            "password":password
        }),
        success: function(){
            validUtil.successProcess();
            $("#loginForm").submit();
        },
        error:function(error){
            validUtil.errorProcess(error.responseJSON);
            console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });
}

//공백제거
$("#email, #password").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});