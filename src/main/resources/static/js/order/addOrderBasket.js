function addBasket(){
    var count = document.querySelector("#basketModal .modalMenu .input-group div").innerHTML;

    $.ajax({
        url: "/api/basket/item/"+id+"/"+count,
        type: "post",
        success: function(data){
            alert("장바구니에 담겼습니다.");
            $(".modal").modal("hide");
        },
        error: function(error){
            alert(error.responseJSON.message);
            $(".modal").modal("hide");
        }
    });
}


let itemPrice;
function replaceBasket(post){
    document.querySelector(".buyTitle").innerHTML = post.title;
    document.querySelector(".buyPrice").innerHTML = addComma(post.price);
    itemPrice = post.price;
}

//orderBasket.js와 유사
function changeCount(text, event){
    var parent = $(event.target).parents(".modalMenu");
    var countDiv = parent.find(".input-group div");
    var count = parseInt(countDiv.html());
    var buttons = parent.find("button");

    var postStock = parseInt($("#stock").text());
    var buyPrice = parent.find(".buyPrice");

    if(text == "minus" && count > 1){
        countDiv.html(count -1);
    }
    if(text == "plus" && count < postStock){
        countDiv.html(count +1);
    }

    count = parseInt(countDiv.html());
    buyPrice.text(addComma(count * itemPrice));

    buttons[0].disabled = false;
    buttons[1].disabled = false;
    if(count <= 1 ){
        buttons[0].disabled = true;
    }

    if(count >= postStock){
        buttons[1].disabled = true;
    }
}
