var id = $("#id").text();
/*
함수 안에있으면 함수가 실행될때마다 id값을 불러오게되는데
사용자가 개발자도구로 값을 수정하게되면 바뀐 id값이 불러와진다!!
그럼 내가 아닌 다른사람의 내용을 바꿀수도있게된다
*/

function nickNameUpdate(){
    var newNickname = $("#nickname").val();

    $.ajax({
        type:"patch",
        url:"/api/member/"+id,
        contentType: 'application/json',
        data:JSON.stringify({
            "updateFieldName":"nickname",
            "nickname":newNickname
        }),
        success: function(lists){
            feedbackClass("nickname", "none", null);
            $("#nickname").val(newNickname);
            $(".navbar .login-position a span").text(newNickname);
            alert("닉네임이 수정되었습니다.");
        },
        error: function(error){
            var errorList = error.responseJSON;
            feedbackClass("nickname", "block", errorList[0].message);
        }
    });
}




function passwordUpdate(){
    var oldPassword = $("#oldPassword").val();
    var newPassword = $("#newPassword").val();
    var newPasswordConfirm = $("#newPasswordConfirm").val();

    if(newPassword != "" && newPassword != newPasswordConfirm){
        feedbackClass("newPasswordConfirm", "block", "비밀번호가 일치하지 않습니다.");
        return;
    }
    feedbackClass("newPasswordConfirm", "none", null);

    $.ajax({
        type:"patch",
        url:"/api/member/"+id,
        contentType: 'application/json',
        data:JSON.stringify({
            "updateFieldName":"password",
            "oldPassword":oldPassword,
            "newPassword":newPassword
        }),
        success: function(data){
            feedbackClass("oldPassword", "none", null);
            feedbackClass("newPassword", "none", null);
            $("#oldPassword").val(newPassword);
            $("#newPassword").val("");
            $("#newPasswordConfirm").val("");
            setTimeout("alert('비밀번호가 수정되었습니다.')", 100);
        },
        error: function(error){
            errorProcess(error.responseJSON);
            console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });
}

const fields = ["oldPassword", "newPassword"];

function errorProcess(errorList){
    for(var i=0; i<fields.length; i++){
        var findErrorObj = errorList.find((error) => { return error.field == fields[i]; });

        if(findErrorObj != undefined){
            feedbackClass(findErrorObj.field, "block", findErrorObj.message);
            break;
            return false;
        }else{
            feedbackClass(fields[i], "none", null);
        }
    }
}

function feedbackClass(selector, display, text){
    if(display == "block"){
        $("#"+selector+"Help").css("display", "block").text(text)
        .addClass("invalid-feedback").removeClass("valid-feedback");
    }
    if(display == "none"){
        $("#"+selector+"Help").css("display", "none");
    }
}


//공백제거
$("#nickname, #oldPassword, #newPassword, #newPasswordConfirm").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});