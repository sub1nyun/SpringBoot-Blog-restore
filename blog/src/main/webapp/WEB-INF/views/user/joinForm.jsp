<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>

<div class="container">
	<form> 
	  <div class="form-group">
	    <label for="username">Username</label>
	    <input type="text" class="form-control" placeholder="Enter username" id="username">
	  </div>
	  
	    <div class="form-group">
	    <label for="password">Password</label>
	    <input type="password" class="form-control" placeholder="Enter password" id="password">
	  </div>
	  
	    <div class="form-group">
	   	 <label for="email">Email</label>
	  	 <input type="email" class="form-control" placeholder="Enter Email" id="email">
	 	</div>
	 	
	
	  	  
	
	</form>
	  <button id="btn-save" class="btn btn-primary">회원가입완료</button>

</div>

<script src="/js/user.js">
<%-- 
$("#btn-save").on("click", function(){
	alert("save 호출");
});

$.ajax({
	//type:,
	//url
	//data { id:$("#username").val(), pw:$("#password").val ~ email}
	//data : JSON.stringify({
	//	"id": ~
	//})
	//dataType: "json"
	
	///success : function(result) {
		//if(result) 
			//~~
			//else { }
	//}, error: ~~
});
--%>
</script>
<%@ include file="../layout/footer.jsp" %>

    