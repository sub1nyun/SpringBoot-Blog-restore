package com.cos.blog.controller;

import java.io.IOException;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.persistence.EntityManager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.model.KaKaoProfile;
import com.cos.blog.model.OAuthToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안 된 사용자들이 출입할 수 있는 경로를 /auth/** 허용
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js/** /css/**, /image/** 허용
// 인증이 필요 없는 곳에는 /auth를 붙힘

@Controller
public class UserController {

	@GetMapping("/auth/joinForm")
	public String joinForm() {	
		return "user/joinForm";
	}
	
	@GetMapping("/auth/kakao/callback")
	public @ResponseBody String kakaoCallback(String code) { //@ResponseBody -> Data를 리턴해주는 컨트롤러
		// 코드 값을 받았기 때문에 인증은 완료됨 -> 엑세스 토큰 부여 가능 -> 카카오 리소스 서버에 내 개인정보를 응답받기 위해
		// POST 방식으로 key=value 데이터를 요청 (카카오쪽으로) -> 필요한 라이브러리 RestTemplate
		//HttpsURLConnection - 이전 사용했지만 불편함
		// Retrofit2 - 안드에서 많이 사용
		// OkHttp

		RestTemplate rt = new RestTemplate();
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders(); // key=value 타입임을 알려주기 ▼
		headers.add("Content-tpye", "application/x-www-form-urlencoded;charset=utf-8");

		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "5c71f1bc11317b665c056498019a0f2b"); // 변수화 하는 것이 좋음
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback"); // 변수화 하는 것이 좋음
		params.add("code", code); // get에서 받은 코드 넘기기
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기 이유는 exchange()가 HttpEntity를 받게 되어있음
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<MultiValueMap<String,String>>(params, headers);

		// Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답을 받음
		ResponseEntity<String> response = rt.exchange(
				"https://kauth.kakao.com/oauth/token",  //토큰 발급 주소 
				HttpMethod.POST, // 요청 메서드 타입
				kakaoTokenRequest, // 데이터와 헤더 값을 한번에
				String.class // 응답을 받을 클래스와 타입
				);
		
		// Gson, Json Simple, ObjectMapper 각종 라이브러리가 존재함
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken = null;
		
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println("토큰 :" + oauthToken.getAccess_token());
		
		RestTemplate rt2 = new RestTemplate();
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders(); // key=value 타입임을 알려주기 ▼
		headers2.add("Authorization", "Bearer "+oauthToken.getAccess_token()); //한 칸 비워둬야함
		headers2.add("Content-tpye", "application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기 이유는 exchange()가 HttpEntity를 받게 되어있음
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2); // 헤더만 넣어도됨

		// Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답을 받음
		ResponseEntity<String> response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me",  
				HttpMethod.POST, // 요청 메서드 타입
				kakaoProfileRequest2, // 데이터와 헤더 값을 한번에
				String.class // 응답을 받을 클래스와 타입
				);
		System.out.println(response2.getBody());
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KaKaoProfile kakaoProfile = null;
		
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KaKaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		System.out.println(kakaoProfile.getId());
		System.out.println(kakaoProfile.getKakao_account().getEmail());

		return response2.getBody();
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {	
		return "user/loginForm";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		// @AuthenticationPrincipal PrincipalDetail principal
		// principal -> 시큐리티컨텍스트홀더안의 Authentication 을 가져옴
		return "user/updateForm";
	}
	
	
	
}
