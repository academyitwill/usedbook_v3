
const fields = ["email", "nickname"];
let validUtil = new ValidUtil(fields);


function findSubmit(){
    var email = $.trim($("#email").val());
    var nickname = $.trim($("#nickname").val());

    $.ajax({
        type:"post",
        url:"/api/member/findPassword",
        contentType: 'application/json',
        data: JSON.stringify({
            "email":email,
            "nickname":nickname
        }),
        success: function(){
            window.location.href='/findPasswordEmailSend';
        },
        error:function(error){
            validUtil.errorProcess(error.responseJSON);
            //console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });
}



//공백제거
$("#email, #nickname").on("keyup change", function(){
    var str = this.value.replace(" ", "");
    $(this).val(str);
});