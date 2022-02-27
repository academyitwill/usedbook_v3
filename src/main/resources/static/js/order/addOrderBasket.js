function addBasket(){
    var count = document.querySelector("#basketModal .modalMenu .input-group div").innerHTML;

    $.ajax({
        url: "/api/order/basket/"+postId+"/"+count,
        type: "post",
        success: function(data){
            alert("장바구니에 담겼습니다.");
            $(".modal").modal("hide");
        },
        error: function(error){
            alert(error.responseText);
            $(".modal").modal("hide");
        }
    });
}



function replaceBasket(post){
    document.querySelector(".buyTitle").innerHTML = post.title;
    document.querySelector(".buyPrice").innerHTML = post.price;
}

//orderBasket.js와 유사
function changeCount(text, event){
    var parent = $(event.target).parents(".modalMenu");
    var countDiv = parent.find(".input-group div");
    var count = parseInt(countDiv.html());
    var buttons = parent.find("button");

    var postStock = parseInt($("#stock").text());
    var postPrice = parseInt($("#price").text());
    var buyPrice = parent.find(".buyPrice");

    if(text == "minus" && count > 1){
        countDiv.html(count -1);
    }
    if(text == "plus" && count < postStock){
        countDiv.html(count +1);
    }

    count = parseInt(countDiv.html());
    buyPrice.text(count * postPrice);

    buttons[0].disabled = false;
    buttons[1].disabled = false;
    if(count <= 1 ){
        buttons[0].disabled = true;
    }

    if(count >= postStock){
        buttons[1].disabled = true;
    }
}
