package com.kristianm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class DeviceContinuityInterceptor implements HandlerInterceptor {
    static int counter = 1;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUrl = request.getRequestURL().toString();
        System.out.println("A" + String.format(" |%4d| ", counter++) + requestUrl);
        // if (requestUrl.contains("images")) {
        //     response.setStatus(404);
        //     return false;
        // }

        if (requestUrl.contains("/validate_cookie") || requestUrl.contains("/create_cookie")) {
            System.out.println("Not intercepting " + requestUrl);
            return true;
        }

        boolean hasCookie = false;
        boolean validCookie = false;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("dc_cookie")) {
                    hasCookie = true;
                    if (!"true".equals(cookie.getValue())) {
                        System.out.println("invalid dc cookie");
                        response.setStatus(307);
                        response.setHeader("Location", "/validate_cookie?continue_url=" + requestUrl);
                        response.setHeader("device-continuity-initialize",
                                "supported-algorithms=rsa_pkcs1_sha256,ecdsa_secp256r1_sha256");
                        return false;
                    }
                    break;
                }
            }
        }

        if (!hasCookie) {
            System.out.println("No dc cookie");
            response.setStatus(307);
            response.setHeader("Location", "/create_cookie?continue_url=" + requestUrl);
            response.setHeader("device-continuity-initialize",
                    "supported-algorithms=rsa_pkcs1_sha256,ecdsa_secp256r1_sha256");
            return false;
        }

        System.out.println("Has valid cookie");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        System.out.println("B" + String.format(" |%4d| ", counter++) + request.getRequestURL());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        System.out.println("C" + String.format(" |%4d| ", counter++) + request.getRequestURL());
    }
}
