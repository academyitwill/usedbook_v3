
$(document).ready(function(){
    loadBasket();
});


function loadBasket(){
    $.ajax({
        url: "/api/basket",
        type: "get",
        success: function(data){
            $("table tbody *").replaceWith();
            $("table tbody").append(addTR(data));
        },
        error: function(error){

        }
    });
}


function updateBasket(postId, count, event){

    $.ajax({
        url: "/api/basket/item/"+postId+"/"+count,
        type: "put",
        success: function(data){

        },
        error: function(error){
            alert(error.responseJSON.message);
        }
    });
}


function deleteBasket(postId, event){

    $.ajax({
        url: "/api/basket/item/"+postId,
        type: "delete",
        success: function(data){
             $(event.target).parents("tr").remove();
             changeTotalPrice();
        },
        error: function(error){
            alert(error.responseJSON.message);
        }
    });
}


function basketToOrder(){
    var boxesChecked = document.querySelectorAll("input[type='checkbox']:checked");
    if(boxesChecked.length -1 == 0){
        alert("상품을 선택해주세요");
        return;
    }

    var arr = new Array();
    for(var i=1; i<boxesChecked.length; i++){
        arr.push(boxesChecked[i].value);
    }

    $.ajax({
        url: "/api/order/temp",
        type: "post",
        contentType: 'application/json',
        data: JSON.stringify({
            "itemIds" : arr
        }),
        success: function(data){
            window.location.replace("/order");
        },
        error: function(error){
            alert(error.responseJSON.message);
        }
    });
}







function addTR(orderBasketList){
    var result = "";
    var sum = 0;

    orderBasketList.forEach(function(basket){

        var checkbox = `<input class="form-check-input" type="checkbox" value="${basket.itemId}" checked>`;
        var soldout = "";
        var buttonDisabled = "";
        if(basket.saleStatus != "READY"){
               checkbox = `<input class="form-check-input" type="checkbox" value="${basket.itemId}" disabled>`;
               soldout = `<span style="color:red; font-size:1.1em;">[판매 완료된 상품입니다. 삭제해주세요.]</span>`;
               buttonDisabled = "disabled";
        }else{
            sum = sum + (basket.count * basket.itemPrice);
        }

        result = result + `
            <tr>
                <th scope="row">${checkbox}</th>

                <td>
                    <div style="float:left;">
                        <div>${soldout}</div>
                        <div><a href="/item/${basket.itemId}">${basket.itemTitle}</a></div>
                        <div>판매가격 : <span class="postPrice won">${addComma(basket.itemPrice)}</span></div>
                        <div><span style="float:left">구매수량 : </span>
                            <div class="orderCount input-group">
                                <button class="btn btn-sm btn-outline-secondary" onclick="changeCount('minus', ${basket.itemId}, event)" ${buttonDisabled}>-</button>
                                <div class="form-control">${basket.count}</div>
                                <button class="btn btn-sm btn-outline-secondary" onclick="changeCount('plus', ${basket.itemId}, event)" ${buttonDisabled}>+</button>
                            </div>
                        </div>
                    </div>

                    <div style="float:right; font-size:1em;">
                        <button class="btn btn-sm btn-outline-primary" onclick="deleteBasket(${basket.itemId}, event)">삭제</button>
                    </div>
                </td>

                <td><span class="buyPrice won">${addComma(basket.count * basket.itemPrice)}</span></td>
                <input type="hidden" class="postStock" value="${basket.itemStock}">
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
    var tempPrice = trs.find(".postPrice").text().replace(',', '');
    var postPrice = parseInt(tempPrice);
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
    updateBasket(postId, count);

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
    var trs = $("input[type='checkbox']:checked").parents("tr");

    trs.find(".buyPrice").each(function(){
        sum = sum + parseInt(this.innerHTML.replace(',',''));
    });

    if(initvalue != null){
        sum = initvalue;
    }

    $(".totalPrice").text(addComma(sum));
}


$(document).on("click", "input[type='checkbox']", function(){
    var boxes = document.querySelectorAll("input[type='checkbox']:not(:disabled)");
    var boxesChecked = document.querySelectorAll("input[type='checkbox']:checked");

    if(boxesChecked.length -1 == 0){
        document.querySelector("#checkAll").checked = false;
    }
    if(boxes.length -1 == boxesChecked.length){
        document.querySelector("#checkAll").checked = true;
    }

    changeTotalPrice();
});


function checkboxAll(selectAll){
    var boxes = document.querySelectorAll("input[type='checkbox']:not(:disabled)");

    boxes.forEach(function(box){
        box.checked = selectAll.checked;
    });
}