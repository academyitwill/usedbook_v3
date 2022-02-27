const categories = ["novel", "humanities", "cartoon"];
const cardCount = 10;
const hostAndPort = window.location.host;

$(document).ready(function(){
    saveList();
});

function saveList(){
    $.ajax({
        url: "/api/posts/allCategoryListForIndex?count="+cardCount,
        type: "get",
        success: function(categoryList){
            for(var key in categoryList){
//                console.log(key);
//                console.log(categoryList[key]);
                replaceSlide(key, categoryList[key]);
            }
            initCardSlider();
        },
        error: function(error){
            alert(error.responseText);
            window.location.replace("/");
        }
    });
}

function replaceSlide(category, postList){

    var result = ``;
    for(var i=0; i<cardCount; i++){
        var title = "아직 게시글이 없어요😥";
        var price = "";
        var stock = "";
        var imgsrc = "https://plchldr.co/i/245x180";
        var postlink = "#";

        if(i < postList.length){    //게시글이 10개미만이면 swiper 오류생김! 게시글이 있는 것만 내용 추가해준다
            title = postList[i].title;
            stock = postList[i].stock +"개";
            price = postList[i].price +"원";
            var fileName = postList[i].fileNames;
            if(fileName.length != 0){
                imgsrc = "/api/image/"+fileName[0];
            }
            postlink = "/post/detail/"+postList[i].id;
        }

        result = result + `
        <a class="swiper-slide" href="${postlink}">
            <img class="swiper-lazy" src="${imgsrc}">
            <div class="card-body">
                <span>${title}</span><br>
                <span>${stock}</span><br>
                <span>${price}</span>
            </div>
        </a>
        `;
    }

    $("."+category+" .card-slider .swiper .swiper-wrapper").append(result);
}