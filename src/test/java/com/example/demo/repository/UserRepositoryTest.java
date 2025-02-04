//package com.example.demo.repository;
//
//import com.example.demo.config.WebSecurityConfig;
//import com.example.demo.entity.UserEntity;
//import com.example.demo.service.UserService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//@Import(WebSecurityConfig.class) // 보안 설정 Import
//class UserRepositoryTest {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    @DisplayName("1. 유저 데이터 생성하기")
//    @Test
//    void test_1() {
//        String encPassword = passwordEncoder.encode("1234");
//        UserEntity userEntity = UserEntity.builder()
//                .userId("1234")
//                .userPw(encPassword)
//                .userName("테스트유저")
//                .build();
//
//        UserEntity savedUser = userRepository.save(userEntity);
//        assertThat(userEntity.getUserId()).isEqualTo(savedUser.getUserId());
//    }
//
//    /* 1. 유저 데이터 생성하기 .............. */
//
//    @DisplayName("2. 유저정보 검색 후 비밀번호 비교")
//    @Test
//    void test_2(){
//        /*
//        String encPassword = passwordEncoder.encode("test_password");
//        UserEntity user = userRepository.findByUserId("test_user")
//                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
//        assertThat(user.getUserPw()).isEqualTo(encPassword);
//        */
//
//        String userId = "test";
//        String userPw = "test_password";
//        UserDetails user = userService.loadUserByUsername(userId);
//
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, userPw);
//        authenticationManager.authenticate(authenticationToken);
//
//        assertThat(authenticationToken.getCredentials()).isEqualTo(userPw);
//
//        System.out.println("getCredentials: " + authenticationToken.getCredentials());
//        System.out.println("userPw: " + userPw);
//    }
//}
//
