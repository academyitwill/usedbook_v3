const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4

$(document).ready(function(){
    loadList(null, null);
});

$("#title, #salestatus, #createtime, #viewcount").on("click", function(){
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

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.comments));
            $(".pagination").append(addPagination(data.pagination));
        },
        error: function(error){
            alert(error.responseText);
        }
    });

}



function addTR(comments){
    var result = "";
    comments.forEach(function(commentResponse){
        result = result + `
            <tr>
                <th scope="row">${commentResponse.postId}</th>
                <td>${commentResponse.postSaleStatus}</td>
                <td>
                    <a href="/post/detail/${commentResponse.postId}">${commentResponse.postTitle}</a>
                </td>
                <td>${commentResponse.commentContent}</td>
                <td>${commentResponse.commentCreatetime}</td>
            </tr>
            `;
    });
    return result;
}
