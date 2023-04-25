let index = {
	init: function() {
		$("#btn-save").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
			this.save(); 
		});
//		$("#btn-login").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
//			this.login(); 
//		});
	$("#btn-update").on("click", ()=>{ //function(){} 을 사용하지 않고 ()=> 한 이유는 this를 바인딩하기 위해서임
			this.update(); 
		});
	},
	
	save: function(){
		let  data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		}
		
		//console.log(data);
		
		// ajax 호출 시 default가 비동기 호출임 
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경 하여 insert 요청
		// ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동하면 자동으로 오브젝트로 변환을 해주는듯? 
		$.ajax({ //Object가 들어와야 하기때문에 {} 사용
			//회원가입 수행 요청
			type: "POST", //insert 할거니까 -> post 방식 사용함
			url: "/auth/joinProc",
			//9번줄 data가 자바스크립트 오브젝트기 때문에 자바에 그냥 던지면 이해불가 -> JSON
			data: JSON.stringify(data), // http body 데이터 -> MIME 타입이 필요함
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			if(resp.status === 500) {
				alert("회원가입이 실패하였습니다.");
			}else {
				alert("회원가입이 완료되었다 이녀석아");
				location.href = "/";
			}
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	},
	
	/*
		login: function(){
		let  data = {
			username: $("#username").val(),
			password: $("#password").val(),
		}
		
		$.ajax({ //Object가 들어와야 하기때문에 {} 사용
			//회원가입 수행 요청
			type: "POST", //insert 할거니까 -> post 방식 사용함
			url: "/api/user/login",
			data: JSON.stringify(data), // http body 데이터 -> MIME 타입이 필요함
			contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
			dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
			//응답 데이터가 json이라면 -> javascript 오브젝트
		}).done(function(resp){ //응답의 결과가 정상이면
			alert("로그인 완료됐다 이말이야");
			location.href = "/";
		}).fail(function(error){ //응답의 결과가 실패면
			alert(JSON.stringify(error));
		}); 
		
	}
	*/

	update: function(){
			let  data = { //username은 수정하지 않음
				id: $("#id").val(),
				username: $("#username").val(),
				password: $("#password").val(),
				email: $("#email").val()
			}
			
			$.ajax({ //Object가 들어와야 하기때문에 {} 사용

				type: "PUT", 
				url: "/user",
				//9번줄 data가 자바스크립트 오브젝트기 때문에 자바에 그냥 던지면 이해불가 -> JSON
				data: JSON.stringify(data), // http body 데이터 -> MIME 타입이 필요함
				contentType: "application/json; charset=utf-8", //body 데이터가 어떤 타입인지 명시
				dataType: "json" //요청 서버로 응답이 왔을 때 -> 버퍼로 오기때문에 문자열(String)
				//응답 데이터가 json이라면 -> javascript 오브젝트
			}).done(function(resp){ //응답의 결과가 정상이면
				alert("회원수정 완료했다 이말이야");
				location.href = "/";
			}).fail(function(error){ //응답의 결과가 실패면
				alert(JSON.stringify(error));
			}); 
			
		}

}

index.init();