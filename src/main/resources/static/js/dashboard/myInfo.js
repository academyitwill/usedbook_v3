var id = $("#id").text();

const fields = ["oldPassword", "newPassword"];
const validUtil = new ValidUtil(fields);
/*
함수 안에있으면 함수가 실행될때마다 id값을 불러오게되는데
사용자가 개발자도구로 값을 수정하게되면 바뀐 id값이 불러와진다!!
그럼 내가 아닌 다른사람의 내용을 바꿀수도있게된다
*/

function nickNameUpdate(){
    var newNickname = $("#nickname").val();

    $.ajax({
        type:"put",
        url:"/api/member/"+id+"/nickname/"+newNickname,
        contentType: 'application/json',
        data:JSON.stringify(),
        success: function(lists){
            validUtil.feedbackClass("nickname", "none", null);
            $("#nickname").val(newNickname);
            $(".navbar .login-position a span").text(newNickname);
            alert("닉네임이 수정되었습니다.");
        },
        error: function(error){
            var errorList = error.responseJSON;
            validUtil.feedbackClass("nickname", "block", errorList.message);
        }
    });
}




function passwordUpdate(){
    var oldPassword = $("#oldPassword").val();
    var newPassword = $("#newPassword").val();
    var newPasswordConfirm = $("#newPasswordConfirm").val();

    if(newPassword != "" && newPassword != newPasswordConfirm){
        validUtil.feedbackClass("newPasswordConfirm", "block", "비밀번호가 일치하지 않습니다.");
        return;
    }
    validUtil.feedbackClass("newPasswordConfirm", "none", null);

    $.ajax({
        type:"put",
        url:"/api/member/"+id+"/password",
        contentType: 'application/json',
        data:JSON.stringify({
            "oldPassword":oldPassword,
            "newPassword":newPassword
        }),
        success: function(data){
            $("#oldPassword").removeClass("is-invalid");
            $("#newPassword").removeClass("is-invalid");
            validUtil.feedbackClass("oldPassword", "none", null);
            validUtil.feedbackClass("newPassword", "none", null);
            $("#newPassword").val("");
            $("#newPasswordConfirm").val("");
            setTimeout("alert('비밀번호가 수정되었습니다.')", 100);
        },
        error: function(error){
            if(error.responseJSON.code == 'password.notequal'){
                error.responseJSON.cause = 'oldPassword';
            }
            validUtil.errorProcess(error.responseJSON);
//            console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });
}



//공백제거
$("#nickname, #oldPassword, #newPassword, #newPasswordConfirm").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});
