let index = {
	init: function() {
		$("#btn-save").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
			this.save(); 
		});
			$("#btn-delete").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
			this.deleteById(); 
		});
		$("#btn-update").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
			this.update(); 
		});
		$("#btn-reply-save").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
			this.replySave(); 
		});
	},
	
	save: function(){
		let  data = {
			title: $("#title").val(),
			content: $("#content").val()
		}
		
		$.ajax({ 
			type: "POST",  // 글 쓰기 요청 -> POST
			url: "/api/board",
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("글쓰기가 완료됐다 임마");
			location.href = "/";
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	},
	
	deleteById: function(){
		let id = $("#id").text();
		
		$.ajax({ 
			type: "DELETE",  
			url: "/api/board/" + id,
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("게시글 삭제 완료");
			location.href = "/";
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	},
	
		update: function(){
			let id = $("#id").val();
			
		let  data = {
			title: $("#title").val(),
			content: $("#content").val()
		}
		
		$.ajax({ 
			type: "PUT",  
			url: "/api/board/"+id,
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("수정 완료");
			location.href = "/";
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	},
	
		replySave: function(){
		let  data = {
			content: $("#reply-content").val()
		}
		let boardId = $("#boardId").val();
		
		$.ajax({ 
			type: "POST",  // 글 쓰기 요청 -> POST
			url: `/api/board/${boardId}/reply`, // ` (백틱)을 사용하는 이유는 url 뒷부분의 게시물을 동적으로 받기위함
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("댓글작성이 완료되었습니다.");
			location.href = `/board/${boardId}`;
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	},
	
		replyDelete: function(boardId, replyId){
			alert(boardId);
			alert(replyId);
		$.ajax({ 
			type: "DELETE",  
			url: `/api/board/${boardId}/reply/${replyId}`, // ` (백틱)을 사용하는 이유는 url 뒷부분의 게시물을 동적으로 받기위함
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("댓글삭제 성공.");
			location.href = `/board/${boardId}`;
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	}
	
		/* 백틱 사용 안 하는 방식 - 테스트 완료
		$.ajax({ 
			type: "POST",  // 글 쓰기 요청 -> POST
			url: "/api/board/"+boardId+"/reply",  //(백틱)을 사용하는 이유는 url 뒷부분의 게시물을 동적으로 받기위함
			data: JSON.stringify(data), 
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("댓글작성이 완료되었습니다.");
			location.href = "/board/" +boardId;
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	}
	
	*/
	
	
}

index.init();