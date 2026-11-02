package com.sens.lush.filter;

import com.sens.lush.service.RememberMeService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 세션이 만료/삭제된 상태(브라우저 재시작 등)로 요청이 들어왔을 때,
 * REMEMBER_ME 쿠키가 유효하면 세션을 자동으로 복구해서 로그인 상태를 유지시킨다.
 *
 * RememberMeService.getInstance()로 Spring이 관리하는 싱글턴 빈을 그대로 참조한다.
 * (WebApplicationContextUtils 방식은 이 프로젝트가 서비스/컨트롤러를
 *  DispatcherServlet 전용 컨텍스트에만 등록해서 Filter에서 접근이 안 되므로 사용하지 않음)
 *
 * web.xml 등록 예:
 * <filter>
 *     <filter-name>autoLoginFilter</filter-name>
 *     <filter-class>com.sens.lush.filter.AutoLoginFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *     <filter-name>autoLoginFilter</filter-name>
 *     <url-pattern>/*</url-pattern>
 * </filter-mapping>
 *
 * 주의: RememberMeService.getInstance()는 Spring이 해당 빈을 최초 생성한
 * 이후에만 값이 채워진다. DispatcherServlet의 초기화(컨텍스트 로딩)가
 * 이 필터의 최초 doFilter 호출보다 먼저 끝나 있어야 하며, 일반적인 요청 흐름에서는
 * 항상 그렇게 동작한다(서버 기동 시 컨텍스트가 먼저 올라가고 나서 요청을 받으므로).
 */
public class AutoLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Spring 컨텍스트 조회 없이 RememberMeService.getInstance()를 그때그때 사용하므로
        // 여기서는 별도 초기화가 필요 없다.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession(false);

        // 이미 세션이 살아있으면 자동 로그인 처리 불필요
        if (session == null || session.getAttribute("USER") == null) {
            Cookie[] cookies = request.getCookies();
            String token = null;

            if (cookies != null) {
                for (Cookie c : cookies) {
                    if ("REMEMBER_ME".equals(c.getName())) {
                        token = c.getValue();
                        break;
                    }
                }
            }

            if (token != null) {
                RememberMeService rememberMeService = RememberMeService.getInstance();
                if (rememberMeService != null) {
                    Object mapUser = rememberMeService.validate(token);
                    if (mapUser != null) {
                        HttpSession newSession = request.getSession(true);
                        newSession.setAttribute("USER", mapUser);
                        // 필요하다면 menuJson도 여기서 다시 조회해서 세팅할 것
                    }
                }
            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }
}
