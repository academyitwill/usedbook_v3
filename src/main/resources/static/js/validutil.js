class ValidUtil {
    constructor(fields){
        this.fields = fields;
    }

    successProcess(field){
        this.inputClass(field, "green");
        this.feedbackClass(field, "none", null);
    }

    errorProcess(errorList){
        var list = [];
        if(errorList.length == undefined){  //Custom오류로 객체1개만 반환될 때
            list.push(errorList);
            errorList = list;
        }

        for(var i=0; i<this.fields.length; i++){
            var findErrorObj = errorList.find((error) => { return error.cause == this.fields[i]; });

            if(findErrorObj != undefined){
                this.inputClass(findErrorObj.cause, "red");
                this.feedbackClass(findErrorObj.cause, "block", findErrorObj.message);
                break;
                return false;
            }else{
                this.inputClass(this.fields[i], "green");
                this.feedbackClass(this.fields[i], "none", null);
            }
        }
    }

    inputClass(selector, color){
        if(color == "green"){    //초록색
            $("#"+selector).addClass("is-valid").removeClass("is-invalid");
        }
        if(color == "red"){  //빨간색
            $("#"+selector).addClass("is-invalid").removeClass("is-valid");
        }
    }

    feedbackClass(selector, display, text){
        if(display == "block"){
            $("#"+selector+"Help").css("display", "block").text(text)
            .addClass("invalid-feedback").removeClass("valid-feedback");
        }
        if(display == "none"){
            $("#"+selector+"Help").css("display", "none");
        }
    }

}


