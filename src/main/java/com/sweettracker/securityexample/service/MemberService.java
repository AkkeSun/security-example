package com.sweettracker.securityexample.service;

import com.sweettracker.securityexample.entity.Member;
import com.sweettracker.securityexample.entity.Role;
import com.sweettracker.securityexample.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
    1. Authentication Provider 가 UserDetailsService 를 호출
    2. DB에 저장된 사용자 정보 조회
    3. 사용자 정보가 있다면 UserDetails 생성 후 리턴
 */
@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).orElseThrow(
            () -> new UsernameNotFoundException("USER IS NOT EXISTS"));
        return new User(member.getUsername(), member.getPassword(), getAuthorities(member));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Member member) {
        return List.of(new SimpleGrantedAuthority(member.getRole().name()));
    }

    @PostConstruct
    public void init() {
        if (memberRepository.findByUsername("user").isEmpty()) {
            // 시큐리티 계정은 항상 password 가 암호화 되어있어야 합니다.
            Member member = new Member();
            member.setUsername("user");
            member.setPassword("1234");
            member.setRole(Role.ROLE_USER);
            member.setPassword(passwordEncoder.encode("1234"));
            memberRepository.save(member);
        }
    }
}
