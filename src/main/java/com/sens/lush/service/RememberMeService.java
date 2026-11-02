package com.sens.lush.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 로그인 상태 유지(Remember-Me) 토큰 관리 서비스
 *
 * Spring 빈(싱글턴)으로 등록되면서, 동시에 static getInstance()로도 접근 가능하게 만들었다.
 * 이유: Filter는 서블릿 컨테이너가 직접 생성하므로 @Autowired가 동작하지 않고,
 * WebApplicationContextUtils로 가져오려 해도 이 프로젝트는 컨트롤러/서비스가
 * DispatcherServlet 전용(자식) 컨텍스트에만 등록되어 있어 Filter에서 접근이 안 된다.
 * Spring 빈은 기본 싱글턴 스코프이므로, 생성자에서 자기 자신을 static 필드에 저장해두면
 * Spring이 관리하는 것과 Filter가 참조하는 것이 항상 동일한 인스턴스가 된다.
 *
 * 데모용으로 메모리(Map) 기반 저장소를 사용한다.
 * 실제 서비스에서는 DB 테이블(예: remember_me_token(token, user_map, expire_at))로
 * 교체하는 것을 권장한다 (서버 재시작 시 토큰이 모두 날아가지 않도록).
 */
@Service
public class RememberMeService {

    private static final long EXPIRE_MILLIS = 30L * 24 * 60 * 60 * 1000; // 30일

    private static volatile RememberMeService instance;

    public RememberMeService() {
        instance = this;
    }

    /** Filter 등 Spring 컨테이너 밖에서 이 서비스가 필요할 때 사용 */
    public static RememberMeService getInstance() {
        return instance;
    }

    private static class TokenEntry {
        Object mapUser;   // 세션에 넣던 것과 동일한 사용자 정보(HashMap)
        long expireAt;
    }

    private final Map<String, TokenEntry> store = new ConcurrentHashMap<>();

    /**
     * 새 토큰을 발급하고 사용자 정보를 함께 저장한다.
     * @return 발급된 토큰 문자열 (쿠키 값으로 사용)
     */
    public String issueToken(Object mapUser) {
        String token = UUID.randomUUID().toString();
        TokenEntry entry = new TokenEntry();
        entry.mapUser = mapUser;
        entry.expireAt = System.currentTimeMillis() + EXPIRE_MILLIS;
        store.put(token, entry);
        return token;
    }

    /**
     * 토큰이 유효하면 저장된 사용자 정보를 반환하고, 아니면 null.
     * (만료된 토큰은 조회 시점에 정리한다)
     */
    public Object validate(String token) {
        if (token == null) return null;
        TokenEntry entry = store.get(token);
        if (entry == null) return null;
        if (entry.expireAt < System.currentTimeMillis()) {
            store.remove(token);
            return null;
        }
        return entry.mapUser;
    }

    public void revoke(String token) {
        if (token != null) {
            store.remove(token);
        }
    }

    public long getExpireSeconds() {
        return EXPIRE_MILLIS / 1000;
    }
}
