const itemId = window.location.pathname.replace("/item/", "");
let loginMemberNickname = null;
$(document).ready(function(){
    loadComment();
    loginMemberNickname = document.querySelector(".loginMemberNickname").value; //"" 또는 닉네임
    if(loginMemberNickname != ""){
        document.querySelector(".commentWriter").innerHTML = loginMemberNickname;
    }
});


function loadComment(){
    $.ajax({
        url: "/api/item/"+itemId+"/comment/list",
        type: "get",
        success: function(commentList){
            //회원인 경우에만 댓글 답글 작성 가능
            if(loginMemberNickname != ""){
                document.querySelector("#commentForm .input-group textarea").innerHTML = "";
                document.querySelector("#commentForm .input-group textarea").disabled = false;
                document.querySelector("#commentForm .input-group button").disabled = false;
            }

            commentList.forEach(function(comment){
                if(comment.depth == 0){
                    $("#commentList").append(addComment(comment));
                }else{
                    $(".c"+comment.parentId).append(addComment(comment));
                }
            });
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/item/list");
        }
    });
}

function writeComment(identify, event){
    var textarea = $(event.target).parents(".input-group").children("textarea")[0];
    var parentId = 0;
    var depth = 0;
    if(identify != null){
        parentId = parseInt(identify.split("_")[0]);
        depth = parseInt(identify.split("_")[1])+1;
    }

    $.ajax({
        url: "/api/item/"+itemId+"/comment",
        type: "post",
        contentType: 'application/json',
        data: JSON.stringify({
            "content":textarea.value,
            "parentId":parentId,
            "depth":depth
        }),
        success: function(comment){
            if(depth == 0){
                $("#commentList").append(addComment(comment));
            }else{
                $(".c"+comment.parentId).append(addComment(comment));
                $(event.target).parents("#replyForm").remove();
                replyClickAgain = null;
            }

            textarea.value = "";
        },
        error: function(error){
            alert(error.responseText);
            //window.location.replace("/posts");
        }
    });
}


function updateComment(commentId, event){
    var textarea = $(event.target).parents(".input-group").children("textarea")[0];

    $.ajax({
        url: "/api/item/"+itemId+"/comment/"+commentId,
        type: "put",
        contentType: 'application/json',
        data: JSON.stringify({
            "content":textarea.value,
        }),
        success: function(updated){
            document.querySelector("#updateForm").remove();
            $(".c"+commentId+" .commentRow .commentContent")[0].innerHTML = textarea.value;
            $(".c"+commentId+" .commentRow .commentContent").show();
            $(".c"+commentId+" .commentRow .commentMenu").show();
            prevUpdateId = null;
        },
        error: function(error){
            alert("수정되지 않았습니다");
        }
    });
}


function deleteComment(commentId, event){

    var deleteOk = confirm("댓글을 삭제하시겠습니까?");
    if(deleteOk == false){
        return;
    }

//    $.ajax({
//        url: "/api/comment/"+postId+"/"+commentId,
//        type: "delete",
//        success: function(deleted){
//            if(deleted > 0){
//
//            }else{
//                alert("삭제되지 않았습니다");
//            }
//        },
//        error: function(error){
//            alert(error.responseText);
//            //window.location.replace("/posts");
//        }
//    });

    $.ajax({
        url: "/api/item/"+itemId+"/comment/"+commentId,
        type: "delete",
        success: function(deleted){
            var row = $(event.target).parents(".commentRow");
            var tags = row.children();
            for(var i=0; i<tags.length; i++){
                if(tags[i].tagName != "I"){
                    tags[i].remove();
                }
            }

            var result = `<div> 삭제된 댓글입니다. </div>`;
            row.append(result);
        },
        error: function(error){
            alert("삭제되지 않았습니다");
            //window.location.replace("/posts");
        }
    });
}






var prevUpdateId = null;
function openUpdateForm(commentId, event){

    var updateContent = $(event.target).parents(".commentRow").children(".commentContent")[0];
    var updateMenu = $(event.target).parents(".commentRow").children(".commentMenu")[0];

    //다시 클릭한 경우엔 삭제하고 끝
    if(prevUpdateId == commentId){
        document.querySelector("#updateForm").remove();
        $(updateContent).show();
        $(updateMenu).show();
        prevUpdateId = null;
        return;
    }

    //다른 답글버튼을 눌렀을땐 삭제하고 해당부분에 추가
    if(document.querySelector("#updateForm") != null){
        document.querySelector("#updateForm").remove();
        $(".c"+prevUpdateId+" .commentRow .commentContent").show();
        $(".c"+prevUpdateId+" .commentRow .commentMenu").show();
        prevUpdateId = null;
    }

    var result = `
        <form id="updateForm" class="commentWriteForm">
            <div class="row">
                <div class="input-group">
                    <textarea class="form-control" rows="3"; name="content" placeholder="댓글 남기기">${updateContent.innerHTML}</textarea>
                    <button class="btn btn-outline-primary" type="button" onclick="updateComment('${commentId}', event)"><i class="fas fa-comments"></i></button>
                </div>
            </div>

             <div class="commentMenu">
                  <div style="float:right;" class="mt-2">
                      <span onclick="openUpdateForm('${commentId}', event)">수정취소</span>
                  </div>
             </div>
        </form>
    `;

    prevUpdateId = commentId;
    $(updateContent).after(result);
    $(updateContent).hide();
    $(updateMenu).hide();
}




var replyClickAgain = null;
function openReplyForm(identify, event){
    //다시 클릭한 경우엔 삭제하고 끝
    if(replyClickAgain == event.target){
        replyClickAgain = null;
        document.querySelector("#replyForm").remove();
        return;
    }
    //다른 답글버튼을 눌렀을땐 삭제하고 해당부분에 추가
    if(document.querySelector("#replyForm") != null){
        document.querySelector("#replyForm").remove();
    }

    var depth=0;
    if(identify != null){
        depth = parseInt(identify.split("_")[1]) + 1;
    }

    var margin = "margin-left:"+(depth * 15)+"px;";
    var result = `
    <form id="replyForm" class="commentWriteForm" style="${margin}">
        <div class="row">
            <div class="mb-2">
                <i class="fas fa-user"></i><span class="commentWriter">${loginMemberNickname}</span>
            </div>

            <div class="input-group">
                <textarea class="form-control" rows="3"; name="content" placeholder="댓글 남기기"></textarea>
                <button class="btn btn-outline-primary" type="button" onclick="writeComment('${identify}', event)"><i class="fas fa-comments"></i></button>
            </div>
        </div>
    </form>
    `;

    $(event.target).parents(".commentRow").after(result);
    replyClickAgain = event.target;
}



function addComment(comment){
    var identify = comment.id+"_"+comment.depth;

    var margin = "margin-left:"+(comment.depth * 20)+"px;";
    var replyIcon = `<i class="fas fa-reply"></i>`;
    if(comment.parentId == 0){
        replyIcon = "";
    }

    var replySpan = `<span onclick="openReplyForm('${identify}', event)">답글</span>`;
    //로그인하지 않았을 경우, comment의 depth가 2일 경우 답글 불가능
    if(loginMemberNickname == "" || comment.depth == 2){
        replySpan = "";
    }

    var commentMenu = `
    <span              onclick="openUpdateForm('${comment.id}', event)">수정</span>
    <span class="ms-2" onclick="deleteComment('${comment.id}', event)">삭제</span>
    `;
    //로그인한자와 댓글작성자가 같지 않다면 수정, 삭제 불가
    if(loginMemberNickname != comment.writer){
        commentMenu = "";
    }


    var result = `
     <div class="commentWrap c${comment.id}">
         <div class="row commentRow" style="${margin}">
             <div class="mb-2">
                  <i class="fas fa-user"></i> <span class="commentWriter" onclick="popover(event, '')" style="cursor:pointer;">${comment.writer}</span>
                  <span class="ms-3" style="font-size:0.7em;">${comment.createTime}</span>
              </div>
              <div class="mb-2 commentContent">${comment.content}</div>

             <div class="commentMenu">
                  ${replySpan}
                  <div style="float:right;" >
                     ${commentMenu}
                  </div>
             </div>
             ${replyIcon}
         </div>
     </div>
      `;

      if(comment.viewStatus == 0){
          result = `
          <div class="commentWrap c${comment.id}">
               <div class="row commentRow" style="${margin}">
                   <div> 삭제된 댓글입니다. </div>
                   ${replyIcon}
               </div>
           </div>
           `;
      }

    return result;
}