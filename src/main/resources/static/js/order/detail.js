const baseUrl = window.location.pathname;   //posts    //posts/novel
var orderId = baseUrl.replace("/order/detail/", "");

$(document).ready(function(){
    loadOrder();
});


function loadOrder(){
    $.ajax({
        url: "/api/order/"+orderId,
        type: "get",
        success: function(data){
            document.querySelector(".orderId").innerHTML = data.orderId;
            document.querySelector(".orderTime").innerHTML = data.orderTime;
            $("table tbody").append(addTR(data.orderPostList));
            addAddress(data.address);
            addPayment(data.payment);
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/order/list");
        }
    });
}




function addAddress(address){
    document.querySelector("#sample6_postcode").value = address.postcode;
    document.querySelector("#sample6_address").value = address.defaultAddress;
    document.querySelector("#sample6_detailAddress").value = address.detailAddress;
    document.querySelector("#sample6_extraAddress").value = address.extraAddress;

    var inputs = document.querySelectorAll(".addressInfo input");
    inputs.forEach(function(input){
        input.readOnly = true;
    });
}

function addPayment(payment){
    var radios = document.querySelectorAll("input[type='radio']");

    radios.forEach(function(radio){
        if(radio.value == payment){
            radio.checked = true;
        }
    });
}






function addTR(orderPostList){
    var result = "";
    var sum = 0;

    orderPostList.forEach(function(post){

        sum = sum + (post.count * post.price);

        result = result + `
            <tr>
                <td>
                    <div style="float:left;">
                        <div><a href="/post/detail/${post.id}">${post.title}</a></div>
                        <div>판매자 : ${post.writer}</div>
                        <div>판매가격 : <span class="postPrice won">${post.price}</span></div>
                        <div>구매수량 : ${post.count}</div>
                    </div>
                </td>

                <td><span class="buyPrice won">${post.count * post.price}</span></td>
            </tr>
            `;
    });
    changeTotalPrice(sum);

    return result;
}




function changeTotalPrice(initvalue){
    var sum = 0;

    document.querySelectorAll(".buyPrice").forEach(function(price){
        sum = sum + parseInt(price.innerHTML);
    });

    if(initvalue != null){
        sum = initvalue;
    }

    $(".totalPrice").text(sum);
}
