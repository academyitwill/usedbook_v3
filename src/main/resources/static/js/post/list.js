const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4

$(document).ready(function(){
    loadList(null, null);
});

$("#title, #writer, #salestatus, #createtime, #viewcount").on("click", function(){
    changeClass(this.id, this.className);
    loadList(this.id, this.className);
});

function loadList(e_id, e_class){

    var orderText = "";
    if(queryString == ""){  //쿼리스트링이 비어있으면 ?로 시작해야함
        orderText = "?";
    }else{
        orderText = "&";
    }

    if(e_id != null){
        orderText = orderText + "otext=" + e_id + "&otype=" + e_class;
    }

    $.ajax({
        url: "/api"+baseUrl+queryString+orderText,
        type: "get",
        success: function(data){
            $(".head h3").text(data.pagination.categoryKor);

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.posts));
            $(".pagination").append(addPagination(data.pagination));
        },
        error: function(error){

        }
    });

}



function addTR(posts){
    var result = "";
    posts.forEach(function(post){
        result = result + `
            <tr>
                <th scope="row">${post.id}</th>
                <td>${post.category}</td>
                <td style="text-overflow: ellipsis;">
                    <a href="/post/detail/${post.id}">${post.title}</a><span> (<span>${post.commentCount}</span>)</span>
                </td>
                <td>${post.saleStatus}</td>
                <td>${post.writer}</td>
                <td>${post.createTime}</td>
                <td>${post.viewCount}</td>
            </tr>
            `;
    });
    return result;
}