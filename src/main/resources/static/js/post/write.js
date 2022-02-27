const fields = ["title", "category", "price", "stock", "content", "fileList"];

function editFormSubmit(){
    //이미지 체크 (slider.js)
    var imgCheck = files.sizeMinCheck();
    if(imgCheck == false){
        return;
    }

    var form = $("#editForm")[0];
    var formData = new FormData(form);

    //(slider.js) 파일 저장
    files.map.forEach(function(v, k){
        formData.append("fileList", v);
    });

    //data json으로 저장
    var data = {
        "title" : $.trim($("#title").val()),
        "category" : $.trim($("#category option:selected").val()),
        "price" : $.trim($("#price").val()),
        "stock" : $.trim($("#stock").val()),
        "content" : $.trim($("#content").val())
    }
    formData.append("jsonData", new Blob([JSON.stringify(data)] , {type: "application/json"}));

    $.ajax({
        url: "/api/post",
        type: "post",
        data: formData,
        contentType: false,
        processData: false,
        enctype : 'multipart/form-data',
        success: function(data){
            successProcess();
            $("#editForm").submit();
        },
        error: function(error){
            errorProcess(error.responseJSON);
            console.log(error.responseJSON);
            //console.clear();    //개발자도구에서 오류 안나오게 할 수 있음
        }
    });

}


$("#title, #category, #price, #stock, #content").on("click", function(){
    if($(this).hasClass("is-invalid")){
        $(this).removeClass("is-invalid");

        var thisid = $(this).attr("id");
        $("#"+thisid+"Help").removeClass("ani");
    }
});



function successProcess(){
    for(var i=0; i<fields.length; i++){
        feedbackClass(fields[i], "none", null);
    }
}

function errorProcess(errorList){
    for(var i=0; i<fields.length; i++){

        feedbackClass(fields[i], "none", null);     //초기화

        var findErrorObj = errorList.find((error) => { return error.field == fields[i]; });
        if(findErrorObj != undefined){
            feedbackClass(findErrorObj.field, "block", findErrorObj.message);
            $("#"+findErrorObj.field+"Help").addClass("ani");
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