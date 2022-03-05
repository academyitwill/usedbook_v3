const baseUrl = window.location.pathname;   //posts    //posts/novel
var id = baseUrl.replace("/item/", "");

//update.js와 유사
$(document).ready(function(){
    loadPost();
});

//update.js와 같음
function loadPost(){
    $.ajax({
        url: "/api/item/"+id,
        type: "get",
        success: function(data){
            replacePost(data);
            replaceBasket(data);
        },
        error: function(error){
            alert(error.responseJSON.message);
            window.location.replace("/item/list");
        }
    });
}


function replacePost(post){
    document.querySelector("#title").innerHTML = post.title;
    document.querySelector("#writer").innerHTML = post.seller;
    document.querySelector("#category").innerHTML = post.category;
    document.querySelector("#price").innerHTML = addComma(post.price);
    document.querySelector("#stock").innerHTML = post.stock;
    document.querySelector("#salestatus").innerHTML = post.saleStatus;
    document.querySelector("#content").innerHTML = post.content;
    document.querySelector("#likeCount").innerHTML = post.likeCount;
    document.querySelector("#viewCount").innerHTML = post.viewCount;
    document.querySelector("#createTime").innerHTML = post.createTime;
    if(post.likeStatus == true){
        document.querySelector("#likeButton").classList.add("clicked");
        document.querySelector("#likeButton").setAttribute("onClick", "dislike()");
    }
    if(post.memberIsSeller == true){
        document.querySelector("#postMenu")
        .innerHTML = `
            <a href="/item/edit/${id}">수정</a>
            <a href="#" onclick="deletePost()">삭제</a>
        `;
    }
    if(post.saleStatus == "판매완료"){
        document.querySelector("#postMenu").innerHTML = "";
    }

    files.init(post.files);
}


function deletePost(){

    var question = confirm("게시글을 삭제하시겠습니까?");
    if(question == false){
        return;
    }

    $.ajax({
        url: "/api/item/"+id,
        type: "delete",
        success: function(data){
            alert("게시글이 삭제되었습니다");
            window.location.replace("/item/list");
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/item/list");
        }
    });

}




function like(){
    var button = document.querySelector("#likeButton");
    var count = document.querySelector("#likeCount");

    $.ajax({
        url: "/api/likeitem/"+id,
        type: "post",
        success: function(data){
            button.classList.toggle("clicked");
            count.innerHTML = parseInt(count.innerHTML) + 1;
            button.setAttribute("onClick", "dislike()");
        },
        error: function(error){
            alert(error.responseText);
        }
    });
}

function dislike(){
    var button = document.querySelector("#likeButton");
    var count = document.querySelector("#likeCount");

    $.ajax({
        url: "/api/likeitem/"+id,
        type: "delete",
        success: function(data){
            button.classList.toggle("clicked");
            count.innerHTML = parseInt(count.innerHTML) - 1;
            button.setAttribute("onClick", "like()");
        },
        error: function(error){
            alert(error.responseText);
        }
    });
}





function addComma(num) {
    num = num + '';

  // 문자열 길이가 3과 같거나 작은 경우 입력 값을 그대로 리턴
  if (num.length <= 3) {
    return num;
  }

  // 3단어씩 자를 반복 횟수 구하기
  var count = Math.floor((num.length - 1) / 3);

  // 결과 값을 저정할 변수
  var result = "";

  // 문자 뒤쪽에서 3개를 자르며 콤마(,) 추가
  for (var i = 0; i < count; i++) {

    // 마지막 문자(length)위치 - 3 을 하여 마지막인덱스부터 세번째 문자열 인덱스값 구하기
    var length = num.length;
    var strCut = num.substr(length - 3, length);
    // 반복문을 통해 value 값은 뒤에서 부터 세자리씩 값이 리턴됨.

    // 입력값 뒷쪽에서 3개의 문자열을 잘라낸 나머지 값으로 입력값 갱신
    num = num.slice(0, length - 3);

    // 콤마(,) + 신규로 자른 문자열 + 기존 결과 값
    result = "," + strCut + result;
  }

  // 마지막으로 루프를 돌고 남아 있을 입력값(value)을 최종 결과 앞에 추가
  result = num + result;

  // 최종값 리턴
  return result;
}