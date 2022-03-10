
$(document).ready(function(){
    loadBasketToOrder();
});


function loadBasketToOrder(){
    $.ajax({
        url: "/api/order/temp",
        type: "get",
        success: function(data){
            $("table tbody *").replaceWith();
            $("table tbody").append(addTR(data));
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/basket");
        }
    });
}




function saveOrder(){

    var postcode = document.querySelector("#sample6_postcode").value;
    var defaultAddress = document.querySelector("#sample6_address").value;
    var detailAddress = document.querySelector("#sample6_detailAddress").value;
    var extraAddress = document.querySelector("#sample6_extraAddress").value;
    if(postcode == "" || detailAddress == ""){
        alert("배송정보를 입력해주세요");
        return;
    }

    var payment = document.querySelector(".btn-group input[type='radio']:checked").value;
    if(payment == ""){
        alert("결제방법을 입력해주세요");
        return;
    }

    var postList = new Array();
    var postIds = document.querySelectorAll(".postId");
    postIds.forEach(function(postId){
        var post = {
            itemId:0,
            itemCount:0
        }

        post.itemId = postId.value;
        post.itemCount = $(postId).parents("tr").find(".orderCount div").text();
        postList.push(post);
    });


    $.ajax({
        url: "/api/order",
        type: "post",
        contentType: 'application/json',
        data: JSON.stringify({
            "orderItemsList" : postList,
            "payment" : payment,
            "address" : {
                "postcode" : postcode,
                "defaultAddress" : defaultAddress,
                "detailAddress" : detailAddress,
                "extraAddress" : extraAddress
            }
        }),
        success: function(orderId){
            window.location.replace("/order/"+orderId);
        },
        error: function(error){
            alert(error.responseText);
        }
    });
}




function addTR(orderBasketList){
    var result = "";
    var sum = 0;

    orderBasketList.forEach(function(basket){

        sum = sum + (basket.count * basket.itemPrice);

        result = result + `
            <tr>
                <td>
                    <div style="float:left;">
                        <div><a href="/item/${basket.itemId}">${basket.itemTitle}</a></div>
                        <div>판매가격 : <span class="postPrice won">${addComma(basket.itemPrice)}</span></div>
                        <div><span style="float:left">구매수량 : </span>
                            <div class="orderCount input-group">
                                <button class="btn btn-sm btn-outline-secondary" onclick="changeCount('minus', ${basket.itemId}, event)">-</button>
                                <div class="form-control">${basket.count}</div>
                                <button class="btn btn-sm btn-outline-secondary" onclick="changeCount('plus', ${basket.itemId}, event)">+</button>
                            </div>
                        </div>
                    </div>
                </td>

                <td><span class="buyPrice won">${addComma(basket.count * basket.itemPrice)}</span></td>
                <input type="hidden" class="postStock" value="${basket.itemStock}">

                <input type="hidden" class="postId" value="${basket.itemId}">
            </tr>
            `;
    });
    changeTotalPrice(sum);

    return result;
}




//addOrderBasket.js와 유사
function changeCount(text, postId, event){
    var trs = $(event.target).parents("tr");
    var countDiv = trs.find(".orderCount.input-group div");
    var count = parseInt(countDiv.html());
    var buttons = trs.find(".orderCount button");

    var postStock = parseInt(trs.find(".postStock").val());
    var postPrice = parseInt(trs.find(".postPrice").text().replace(',', ''));
    var buyPrice = trs.find(".buyPrice");

    if(text == "minus" && count > 1){
        countDiv.html(count -1);
    }
    if(text == "plus" && count < postStock){
        countDiv.html(count +1);
    }

    count = parseInt(countDiv.html());
    buyPrice.text(addComma(count * postPrice));

    changeTotalPrice();

    buttons[0].disabled = false;
    buttons[1].disabled = false;
    if(count <= 1 ){
        buttons[0].disabled = true;
    }

    if(count >= postStock){
        buttons[1].disabled = true;
    }
}




function changeTotalPrice(initvalue){
    var sum = 0;

    document.querySelectorAll(".buyPrice").forEach(function(price){
        var price = price.innerHTML.replace(',', '');
        sum = sum + parseInt(price);
    });

    if(initvalue != null){
        sum = initvalue;
    }

    $(".totalPrice").text(addComma(sum));
}
