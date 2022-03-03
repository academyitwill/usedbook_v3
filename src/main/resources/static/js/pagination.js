function addPagination(pagination){
    var paramPage = urlParams.get("page");

    let numCount = 10;  //페이지번호 몇개 표시할지
    let startPage = Math.floor(paramPage / 10) + 1;
    let endPage = pagination.totalPages;
    let prevPage = !pagination.first;
    let nextPage = !pagination.last;


    let startUrl = createPageUrl(paramPage -1);
    let endUrl = createPageUrl(Number(paramPage) +1);

    var result = `
    <li class="page-item  ${prevPage == false ? 'disabled' : ''}">
        <a class="page-link" href="${startUrl}">Previous</a>
    </li>`;

    for(var num = startPage; num <= endPage; num++){
        let pageUrl = createPageUrl(num);
        result = result + `
        <li class="page-item  ${num == paramPage ? 'active' : ''}">
            <a class="page-link" href="${pageUrl}">${num}</a>
        </li>
        `;
    }

    result = result + `
    <li class="page-item  ${nextPage == false ? 'disabled' : ''}">
        <a class="page-link" href="${endUrl}">Next</a>
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


function createPageUrl(value){
    let url = new URLSearchParams(location.search);
    url.set("page", value);
    url = window.location.pathname + "?" + url;
    return url;
}