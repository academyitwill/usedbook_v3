var cardSliders = new Map();
function initCardSlider(){
    $('.card-slider').each(function(i) {
        var thisID = $(this).attr("id");
        cardSliders.set('cardSlider_'+thisID, createCardSlider(thisID));
    });
}

function createCardSlider(thisID){
    var selector = '#'+thisID+" .swiper";
    var cardSlider = new Swiper(selector, {
        loop: true,
        navigation: {
            nextEl: '#next_'+thisID,
            prevEl: '#prev_'+thisID,
        },
        lazy : {
            loadPrevNext : true, // 이전, 다음 이미지는 미리 로딩
        },
        breakpoints: {
             320: {  slidesPerView: 2,  spaceBetween: 10,  },
             500: {  slidesPerView: 3,  spaceBetween: 10,  },
             768: {  slidesPerView: 4,  spaceBetween: 10,  },
             1024: { slidesPerView: 5,  spaceBetween: 10,  },
             //반복되려면 최소 5개 이상의 슬라이드가 있어야한다!!
             //3개 슬라이드로 5개 테스트하면 next안눌려짐
        },
    });
    return cardSlider;
}

//===================================================================================
//===================================================================================
//===================================================================================

const thumbSlider = {
    id: $(".thumb-slider").attr("id"),
    bottom: null,
    top: null,   //bottom이 있어야 top의 thumbs: 에서 bottom을 삽입할 수 있다
    init:function(){
        var thumbBottom = new Swiper(".thumb-bottom", {
            spaceBetween: 10,
            slidesPerView: 5,
            freeMode: true,
            watchSlidesProgress: true,
        });

        var thumbTop = new Swiper(".thumb-top", {
             spaceBetween: 10,
             navigation: {
                 nextEl: '.swiper-button-next',
                 prevEl: '.swiper-button-prev',
             },
             thumbs: {
               swiper: thumbBottom,
             },
         });

         this.top = thumbTop;
         this.bottom = thumbBottom;
    },
    addSlide:function(blobsrc){
        var lastIndex = files.map.size-1;
        var url = window.location.pathname;
        var removeFileIcon = "";
        if(url.includes("write") || url.includes("edit")){
            removeFileIcon = `<div class="remove-slide" onclick="fileRemove(event)"><i class="fas fa-times"></i></div>`;
        }

        this.top.addSlide(lastIndex, `
            <div class="swiper-slide">
                ${removeFileIcon}
                <img src="${blobsrc}" />
            </div>
        `);

        this.bottom.addSlide(lastIndex, `
            <div class="swiper-slide">
                <img src="${blobsrc}" />
            </div>
        `);

        //추가한 슬라이드로 화면이동
        this.top.slideTo(lastIndex);
        this.bottom.slideTo(lastIndex);
        //https://www.tutorialdocs.com/tutorial/swiper/api-methods-properties.html
    },
    deleteSlide:function(index){
        this.top.removeSlide(index);
        this.bottom.removeSlide(index);
    }
}


//===================================================================================
//===================================================================================
//===================================================================================
const filesMap = new Map();
const removeFilesArr = new Array();
const files = {
    map: filesMap,
    removeArr: removeFilesArr,
    init : function(files){
        for(var i=0; i<files.length; i++){
            var fileName = files[i].fileName;
            var url = "/api/image/"+fileName;
            this.map.set(fileName, "downloadImage");    //없으면 수정할 때 이미지 0개로 처리됨 = 수정 시 무조건 파일을 업로드해야됨
            thumbSlider.addSlide(url);
        }
    },
    cntMin: 1,
    cntMax: 10,
    sizeMaxCheck: function(){
        if(this.map.size >= this.cntMax){
            alert("이미지는 10개까지만 업로드 가능합니다.");
            return false;
        }
    },
    sizeMinCheck: function(){
        if(this.map.size < this.cntMin){
            alert("이미지는 최소 1개 이상 업로드 해야합니다.");
            return false;
        }
    }
}

$(document).ready(function(){
    //files.init();
    thumbSlider.init();
});


function addImgClick(event){
    var check = files.sizeMaxCheck();
    if(check == false){
        return;
    }
    $("#inputFile").click();
}

function fileChange(event){
    //파일 선택 후 실행됨
    //console.log("filechange run");

    var filesArr = Array.prototype.slice.call(event.target.files);

    var blobsrc = "";
    var count = files.map.size;
    filesArr.forEach(function(file){
        //filesArr을 추가했을 때 size가 10개 이하가 되었을때만 map에 추가
        count++;
        if(count <= files.cntMax){
            blobsrc = URL.createObjectURL(file);
            //Blob 반환. Binary Large Object
            //blob객체의 url주소값으로 이미지를 불러올 수 있게된다.
            //이렇게 생성된 주소는 브라우저의 메모리에 올라가있다.

            files.map.set(blobsrc, file);
            thumbSlider.addSlide(blobsrc);
        }
    });
    //console.log(files.map);

    if(count > files.cntMax){
        alert("이미지는 10개까지만 업로드 가능합니다. \n 10개 이후의 이미지는 삭제되었습니다.");
    }

    blobsrc.onload = function(){
        URL.rejectObjectUrl(blobsrc);
        //이미지 로딩 후 URL 메모리에서 해제
    }
}

function fileRemove(event){
    var imgsrc = $(event.target).parents(".swiper-slide").children("img").attr("src");
    imgsrc = imgsrc.replace("/api/image/", "");
    files.map.delete(imgsrc);
    thumbSlider.deleteSlide(thumbSlider.top.activeIndex);

    files.removeArr.push(imgsrc);
//    console.log(files.map);
}