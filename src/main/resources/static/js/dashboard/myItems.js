const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4
const urlParams = new URLSearchParams(location.search);

$(document).ready(function(){
    loadList(null, null);
});

$("#title, #salestatus, #createtime, #viewcount").on("click", function(){
    changeClass(this.id, this.className);
    loadList(this.id, this.className);
});

function loadList(e_id, e_class){
    if(e_id != null){
        urlParams.set("sort", e_id+","+e_class);
    }

    $.ajax({
        url: "/api/item/dashboard?"+urlParams,
        type: "get",
        success: function(data){

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.pagination.content));
            $(".pagination").append(addPagination(data.pagination));
        },
        error: function(error){
            alert(error.responseText);
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
                    <a href="/item/${post.id}">${post.title}</a><span> (<span>${post.commentCount}</span>)</span>
                </td>
                <td>${post.saleStatus}</td>
                <td>${post.createTime}</td>
                <td>${post.viewCount}</td>
            </tr>
            `;
    });
    return result;
}
