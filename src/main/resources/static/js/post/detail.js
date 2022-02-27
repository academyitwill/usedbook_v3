const baseUrl = window.location.pathname;   //posts    //posts/novel
var id = baseUrl.replace("/post/detail/", "");

//update.js와 유사
$(document).ready(function(){
    loadPost();
});

//update.js와 같음
function loadPost(){
    $.ajax({
        url: "/api/post/"+id,
        type: "get",
        success: function(data){
            replacePost(data);
            replaceBasket(data);
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/posts");
        }
    });
}


function replacePost(post){
    document.querySelector("#title").innerHTML = post.title;
    document.querySelector("#writer").innerHTML = post.writer;
    document.querySelector("#category").innerHTML = post.category;
    document.querySelector("#price").innerHTML = post.price;
    document.querySelector("#stock").innerHTML = post.stock;
    document.querySelector("#salestatus").innerHTML = post.saleStatus;
    document.querySelector("#content").innerHTML = post.content;
    document.querySelector("#likeCount").innerHTML = post.likeCount;
    document.querySelector("#viewCount").innerHTML = post.viewCount;
    document.querySelector("#createTime").innerHTML = post.createTime;
    if(post.likeStatus == true){
        document.querySelector("#likeButton").classList.add("clicked");
    }
    if(post.menuStatus == true){
        document.querySelector("#postMenu")
        .innerHTML = `
            <a href="/post/update/${id}">수정</a>
            <a href="#" onclick="deletePost()">삭제</a>
        `;
    }
    if(post.saleStatus == "판매완료"){
        document.querySelector("#postMenu").innerHTML = "";
    }

    files.init(post.fileNames);
}


function deletePost(){

    var question = confirm("게시글을 삭제하시겠습니까?");
    if(question == false){
        return;
    }

    $.ajax({
        url: "/api/post/"+id,
        type: "delete",
        success: function(data){
            alert("게시글이 삭제되었습니다");
            window.location.replace("/posts");
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/posts");
        }
    });

}




function like(){
    var button = document.querySelector("#likeButton");
    var count = document.querySelector("#likeCount");

    var like = !button.classList.contains("clicked");

    $.ajax({
        url: "/api/post/like/"+like+"/"+id,
        type: "get",
        success: function(data){
            if(data >= 1){
                button.classList.toggle("clicked");
                if(like == true){
                    count.innerHTML = parseInt(count.innerHTML) + 1;
                }else{
                    count.innerHTML = parseInt(count.innerHTML) - 1;
                }
            }
        },
        error: function(error){
            alert(error.responseText);
        }
    });
}

