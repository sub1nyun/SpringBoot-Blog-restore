<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!-- 스프링 시큐리티 -->
<!-- isAuthenticated() -> 권한에 관계없이 로그인 인증을 받은 경우 -->
<sec:authorize access="isAuthenticated()"> 
	<sec:authentication property="principal" var="principal"/> <!-- PrincipalDetail Class를 뜻함 -->
</sec:authorize>

<!--<sec:authentication property="principal" var="principal"/> -->

<!DOCTYPE html>
<html lang="en">
<head>
  <title>블로그 연습하기</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.1/dist/jquery.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-bs4.min.js"></script>
</head>
<body>
<!-- 시큐리티 라이브러리를 사용하면 자동적으로 프레젝트 잠굼을 시작하는데
user라는  username을 제공 콘솔에 제공해주는 password를 사용하면 
스프링이 세션을 만들어서 저장을해줌 그 값이 principal에 있기때문에 해당 값으로
로그인이 가능함 -->
<nav class="navbar navbar-expand-md bg-dark navbar-dark">
  <a class="navbar-brand" href="/">Home</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsibleNavbar">
    <span class="navbar-toggler-icon"></span>
  </button>
  <div class="collapse navbar-collapse" id="collapsibleNavbar">
  
  <c:choose>
  	<c:when test="${ empty principal }">
  		<ul class="navbar-nav">
      		<li class="nav-item">
        		<a class="nav-link" href="/auth/loginForm">로그인</a>
      		</li>
      		<li class="nav-item">
		        <a class="nav-link" href="/auth/joinForm">회원가입</a>
	      </li>
    	</ul>
  	</c:when>
  	<c:otherwise>
  		<ul class="navbar-nav">
      		<li class="nav-item">
		        <a class="nav-link" href="/board/saveForm">글쓰기</a>
      		</li>
      		<li class="nav-item">
		        <a class="nav-link" href="/user/updateForm">회원정보</a>
      		</li>
      		<li class="nav-item">
		        <a class="nav-link" href="/logout">로그아웃</a>
      		</li>
    	</ul>
  	</c:otherwise>
  </c:choose>

    
    
    
    
    
    
  </div>  
</nav>
<br/>