function popover(event, popover2){
    let nickname = $(event.target).text();
    $(event.target).append(`
        <div class="popover ${popover2} p-1" style="cursor:pointer;" onclick="goChat('${nickname}')">
            채팅하기
        </div>
    `);

    $(".popover").fadeOut(5000, "swing");
    setTimeout(function(){
        $(".popover").remove();
    },5000);
}


function goChat(nickname){
    let myNickname = document.querySelector(".loginMemberNickname").value;

    if(myNickname == ""){
        alert("회원만 채팅기능을 이용할 수 있습니다.");
        return;
    }

    if(nickname == myNickname){
        alert("자기자신과는 채팅할 수 없습니다.");
        return;
    }

    let roomId = 0;
    fetch("/api/chat/"+nickname, {
        method: "post"
    })
    .then((res) => res.json())
    .then((json) => {
        roomId = json;
        window.location.replace("/dashboard/myChat/"+roomId+"/"+nickname);
    });
}