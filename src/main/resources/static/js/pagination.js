function addPagination(pagination){
    var paramPage = new URLSearchParams(location.search).get("page");
    if(paramPage == null){
        paramPage = 1;
    }

    var result = `
    <li class="page-item  ${pagination.prevPage == false ? 'disabled' : ''}">
        <a class="page-link" href="${baseUrl}?page=${pagination.startPage-1}">Previous</a>
    </li>`;

    for(var num = pagination.startPage; num <= pagination.endPage; num++){
        result = result + `
        <li class="page-item  ${num == paramPage ? 'active' : ''}">
            <a class="page-link" href="${baseUrl}?page=${num}">${num}</a>
        </li>
        `;
    }

    result = result + `
    <li class="page-item  ${pagination.nextPage == false ? 'disabled' : ''}">
        <a class="page-link" href="${baseUrl}?page=${pagination.endPage+1}">Next</a>
    </li>
    `;

    return result;
}


function changeClass(e_id, e_class){
    if(e_class == "asc"){
        $("#"+e_id).removeClass("asc").addClass("desc");
        $("#"+e_id+" span").text("↓");
    }
    if(e_class == "desc"){
        $("#"+e_id).removeClass("desc").addClass("asc");
        $("#"+e_id+" span").text("↑");
    }
}