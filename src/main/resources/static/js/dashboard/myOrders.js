const baseUrl = window.location.pathname;   //posts    //posts/novel
const queryString = window.location.search;       //?page=4
const urlParams = new URLSearchParams(location.search);

$(document).ready(function(){
    loadList();
});


function loadList(){


    $.ajax({
        url: "/api/order/list?"+urlParams,
        type: "get",
        success: function(data){

            $("table tbody *").replaceWith();
            $(".pagination *").replaceWith();

            $("table tbody").append(addTR(data.content));
            $(".pagination").append(addPagination(data));
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

        for(var i=0; i<order.orderItems.length; i++){
            var post = order.orderItems[i];
            var hr = `<hr>`;
            sum = sum + (post.itemPrice * post.itemCount);

            if(i == order.orderItems.length -1){
                hr = "";
            }

            content = content + `
            <div><a href="/item/${post.itemId}">${post.itemTitle}</a></div>
            <div class="won">판매가격 : ${addComma(post.itemPrice)}</div>
            <div class="ea">구매수량 : ${post.itemCount}</div>
            ${hr}
            `;
        }

        result = result + `
        <tr>
            <td class="enter"><a href="/order/${order.orderId}" style="text-decoration:none;">${order.orderId}</a></td>
            <td>
                주문일시 : <span>${order.orderTime}</span>
                <hr>
                ${content}
            </td>
            <td class="won">${addComma(sum)}</td>
        </tr>
        `;

    });


    return result;
}
