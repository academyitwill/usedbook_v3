class ValidUtil {
    constructor(fields){
        this.fields = fields;
    }

    successProcess(){
        for(var i=0; i<this.fields.length; i++){
            this.inputClass(this.fields[i], "green");
            this.feedbackClass(this.fields[i], "none", null);
        }
    }

    errorProcess(errorList){
        for(var i=0; i<this.fields.length; i++){
            var findErrorObj = errorList.find((error) => { return error.field == this.fields[i]; });

            if(findErrorObj != undefined){
                this.inputClass(findErrorObj.field, "red");
                this.feedbackClass(findErrorObj.field, "block", findErrorObj.message);
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


