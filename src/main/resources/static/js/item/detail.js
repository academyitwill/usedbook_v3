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
    if(post.saleStatus == "판매완료" && post.memberIsSeller == false){
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




