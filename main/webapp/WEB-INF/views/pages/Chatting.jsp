<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="path" value="${pageContext.request.contextPath }" />
<fmt:requestEncoding value="UTF-8" />
<!DOCTYPE html>
<%--


 --%>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="${path}/com/bootstrap.min.css">
<style>
	.input-group-text{width:100%;background-color:linen;
		color:black;font-weight:bolder;}
	.input-group-prepend{width:20%;}
	#chatArea{
		width:80%;height:200px;overflow-y:scroll;text-align:left;
		border:1px solid green;
		padding-right:15px;
	}
</style>
<script src="${path}/com/jquery-3.7.1.js"></script>
<script src="${path}/com/bootstrap.min.js"></script>
<script type="text/javascript">
let socketServer = "${socketServer}";
let wsocket = null;
let mx = 0; // 스크롤 위치 누적

$(document).ready(function() {

	// 입장 버튼 클릭
	$("#enterBtn").click(function(){
	    if(!$("#id").val()){
	        alert("아이디를 입력하세요!");
	        return;
	    }

	    // 이미 접속 중이면 새로 연결하지 않음
	    if(wsocket && wsocket.readyState === WebSocket.OPEN){
	        alert("이미 채팅방에 입장 중입니다.");
	        return;
	    }

	    wsocket = new WebSocket(socketServer);

	    // 접속 성공 시
	    wsocket.onopen = function(evt){
	        console.log("서버 접속 성공:", evt);
	        wsocket.send($("#id").val() + "님: 입장합니다.");
	    };

	    // 서버로부터 메시지 수신
	    wsocket.onmessage = function(evt){
	        console.log("서버 메시지:", evt.data);
	        revMsg(evt.data);
	    };

	    // 서버와 연결 종료
	    wsocket.onclose = function(evt){
	        console.log("서버와 연결 종료:", evt);
	    };

	    // 에러 발생 시
	    wsocket.onerror = function(evt){
	        console.log("에러 발생:", evt);
	    };
	});
    // 메시지 전송 버튼
    $("#sendBtn").click(function(){
        sendMsg();
    });

    // 엔터키로 메시지 전송
    $("#msg").keyup(function(e){
        if(e.keyCode == 13){  
            sendMsg();
        }
    });

    // 퇴장 버튼
    $("#exitBtn").click(function(){
        if(wsocket && wsocket.readyState === WebSocket.OPEN){
            if(confirm($("#id").val() + "님, 채팅방 접속을 종료하시겠습니까?")){
                wsocket.send($("#id").val() + "님: 채팅방 접속을 종료합니다.");
                wsocket.close();
                $("#chatMessageArea").text("");
                $("#id").val("").focus();
            }
        }
    });

});

// 메시지 전송 함수
function sendMsg(){
    if(!wsocket || wsocket.readyState !== WebSocket.OPEN){
        alert("먼저 채팅방에 입장하세요.");
        return;
    }
    if(!$("#msg").val()){
        return;
    }
    wsocket.send($("#id").val() + ":" + $("#msg").val());
    $("#msg").val("");
}

// 메시지 수신/출력 함수
function revMsg(msg){
    var alignOpt = "left";
    var msgArr = msg.split(":");
    var sndId = msgArr[0];

    // 본인이 보낸 메시지일 경우 오른쪽 정렬
    if($("#id").val() === sndId){
        alignOpt = "right";
        msg = msgArr[1];
    }

    var msgObj = $("<div></div>")
                    .text(msg)
                    .attr("align", alignOpt)
                    .css("width", $("#chatArea").width() - 20);
    $("#chatMessageArea").append(msgObj);

    // 자동 스크롤
    var height = parseInt($("#chatMessageArea").height());
    mx += height + 20;
    $("#chatArea").scrollTop(mx);
}
</script>
</head>

<body>
	<div class="jumbotron text-center">
		<h2>채팅</h2>
		<p>받은 메시지</p>
	</div>
	<div class="container">
		<div class="input-group mb-3">
			<div class="input-group-prepend ">
				<span class="input-group-text  justify-content-center">입장할이름</span>
			</div>
			<input id="id" class="form-control" placeholder="접속할 아이디 입력" />
			<input id="enterBtn"  class="btn btn-primary" value="채팅방입장"/>
			<input id="exitBtn"  class="btn btn-danger" value="채팅방퇴장"/>
		</div>
		
		<div class="input-group mb-3">
			<div class="input-group-prepend ">
				<span class="input-group-text  justify-content-center">메시지</span>
			</div>
		
			<div id="chatArea" style="overflow-x:hidden" class="input-group-append">
				<div id="chatMessageArea"></div>
			</div>
		</div>
		<div class="input-group mb-3">
			<div class="input-group-prepend ">
				<span class="input-group-text  justify-content-center">메시지</span>
			</div>
			<input id="msg" class="form-control" placeholder="전송할 메시지 입력" />
			<input id="sendBtn"  class="btn btn-success" value="메시지전송"/>
		</div>
	</div>
</body>
</html>