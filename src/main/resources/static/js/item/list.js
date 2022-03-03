const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4
const urlParams = new URLSearchParams(location.search);

$(document).ready(function(){
    loadList(null, null);
});

$("#title, #seller, #saleStatus, #createTime, #viewCount").on("click", function(){
    changeClass(this.id, this.className);
    loadList(this.id, this.className);
});

function loadList(e_id, e_class){
    if(e_id != null){
        urlParams.set("sort", e_id+","+e_class);
    }

    $.ajax({
        url: "/api"+baseUrl+"?"+urlParams,
        type: "get",
        success: function(data){
            $(".head .categoryName").text(data.category);

            if(urlParams.get("search") != null){
                replaceSearch();
            }

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
                <td>${post.seller}</td>
                <td>${post.createTime}</td>
                <td>${post.viewCount}</td>
            </tr>
            `;
    });
    return result;
}



function replaceSearch(){
    let search = urlParams.get("search").split(",");
    let searchType = search[0];
    let searchValue = search[1];

    document.querySelectorAll("#search option").forEach(function(option){
        if(option.value == searchType){
            option.selected = true;
        }
    });
    document.querySelector("#searchValue").value = searchValue;
}






function searchFormSubmit(){
    event.preventDefault(); //submit시 queryString이 모두 사라지게되는 것 방지
    //https://ejolie.dev/posts/form-submission-algorithm 참고

    let option = document.querySelector("#search > option:checked").value;
    let value = document.querySelector("#searchValue").value;

    let url = new URLSearchParams(location.search);
    url.set("search", option+","+value);
    url.set("page", 1);

    if(value == null || value == ""){
        url.delete("search");
    }

    window.location.href = baseUrl + "?" + url;
}


$("#searchValue").on("keyup", function(event){
    if(event.keyCode == 13){
        searchFormSubmit();
    }
});