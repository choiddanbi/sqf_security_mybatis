package com.study.SpringSecurityMybatis.service;

import com.study.SpringSecurityMybatis.dto.request.ReqOAuth2MergeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class OAuth2Service implements OAuth2UserService {


    @Autowired
    private DefaultOAuth2UserService defaultOAuth2UserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

//        System.out.println(userRequest.getClientRegistration());
//        System.out.println(oAuth2User.getAttributes());
//        System.out.println(oAuth2User.getAuthorities());
//        System.out.println(oAuth2User.getName()); // 이게 중요!! 이걸로 계정마다 연동시킬거야


        // 네이버가 아니면 바로 getName 으로 id 가져올 수 있음
        // 네이버는 oAuth2User.getAttribute() 가 {resultcode=00, message=success, response={id=8ESMET 어쩌구}}
        // 구글은 oAuth2User.getAttribute() 가 {sub=어쩌구우우우}
        // 카카오는 oAuth2User.getAttribute() 가 {id=어쩌구구구}
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Map<String, Object> oAuth2Attributes = new HashMap<>();

        // userRequest.getClientRegistration().getClientName() 는 Google 또는 Naver 또는 Kakao
        // provider 키값에 userRequest.getClientRegistration().getClientName() 값 넣기
        oAuth2Attributes.put("provider", userRequest.getClientRegistration().getClientName());

        switch (userRequest.getClientRegistration().getClientName()) {
            case "Google":
                oAuth2Attributes.put("id", attributes.get("sub").toString());
                break;

            case "Naver":
                attributes = (Map<String, Object>) attributes.get("response"); // response만 꺼낸다 -> 네이버는 id 가 response={id=어쩌구어쩌구} 형태라서, 다른애들은 바로 {id있음}
                oAuth2Attributes.put("id", attributes.get("id").toString());
                break;

            case "Kakao":
        }

        return new DefaultOAuth2User(new HashSet<>(), oAuth2Attributes, "id");
    }

        public void merge(com.study.SpringSecurityMybatis.entity.OAuth2User oAuth2User) {

        }

}
