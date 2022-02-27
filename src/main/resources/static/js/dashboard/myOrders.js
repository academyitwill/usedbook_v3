const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4

$(document).ready(function(){
    loadList();
});


function loadList(){


    $.ajax({
        url: "/api"+baseUrl+queryString,
        type: "get",
        success: function(data){

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.orders));
            $(".pagination").append(addPagination(data.pagination));
        },
        error: function(error){
            alert(error.responseText);
        }
    });

}



function addTR(orders){

    var result = "";
    orders.forEach(function(order){
        var sum = 0;
        var content = ``;

        for(var i=0; i<order.orderPostList.length; i++){
            var post = order.orderPostList[i];
            var hr = `<hr>`;
            sum = sum + (post.price * post.count);

            if(i == order.orderPostList.length -1){
                hr = "";
            }

            content = content + `
            <div><a href="/post/detail/${post.postId}">${post.title}</a></div>
            <div>판매자 : ${post.writer}</div>
            <div class="won">판매가격 : ${post.price}</div>
            <div class="ea">구매수량 : ${post.count}</div>
            ${hr}
            `;
        }

        result = result + `
        <tr>
            <td class="enter"><a href="/order/detail/${order.orderId}" style="text-decoration:none;">${order.orderId}</a></td>
            <td>
                주문일시 : <span>${order.orderTime}</span>
                <hr>
                ${content}
            </td>
            <td class="won">${sum}</td>
        </tr>
        `;

    });


    return result;
}
