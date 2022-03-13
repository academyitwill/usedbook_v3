const myNicknameForList = document.querySelector(".loginMemberNickname").value;

document.addEventListener("DOMContentLoaded", function(){
    changeList();
});


function changeList(){
    fetch("/api/chat/room/list", {
        method: "get"
    })
    .then((res) => res.json())
    .then((json) => {
        changeListView(json);
    });
}


function changeListView(list){
    $(".inbox_chat").empty();

    //새로 만들어진 채팅방이라면
    let newRoom = true;

    //active_chat 클래스 추가 : 현재 채팅창
    list.forEach((chat) => {
        chatRoomIdArr.push(chat.roomId);

        let active = "";
        if(roomId == chat.roomId){
            active = "active_chat";
            newRoom = false;
        }
        $(".inbox_chat").append(addList(chat, active));
    });

    //dashboard/myChat 접속 시 dashboard로 리스트 하나 생겨남
    if(roomId == "" && yourNickname == "dashboard"){
        return;
    }

    if(newRoom == true){
        let temp = {
            roomId : roomId,
            otherNickname : decodeURI(yourNickname),
            recentMessage : "",
            recentTime : ""
        }
        $(".inbox_chat").append(addList(temp, "active_chat"));
    }

}


function addList(chat, active){
    let str = `
    <a id="room${chat.roomId}" href="/dashboard/myChat/${chat.roomId}/${chat.otherNickname}">
        <div class="chat_list ${active}">
            <div class="chat_people">
                <div class="chat_img"> <img src="https://ptetutorials.com/images/user-profile.png" alt="sunil"> </div>
                <div class="chat_ib">
                    <h5>${chat.otherNickname}<span class="chat_date">${chat.recentTime}</span></h5>
                    <p class="chat_message">${chat.recentMessage}</p>
                </div>
            </div>
        </div>
    </a>
    `;
    return str;
}


