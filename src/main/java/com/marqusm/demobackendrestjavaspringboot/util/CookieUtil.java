package com.marqusm.demobackendrestjavaspringboot.util;

import com.marqusm.demobackendrestjavaspringboot.constant.CookieName;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/** @author : Marko Mišković */
@Service
public class CookieUtil {

  public static Cookie createAuthCookie(String value, int maxAge) {
    Cookie cookie = new Cookie(CookieName.AUTH_TOKEN, value);
    cookie.setHttpOnly(true);
    cookie.setMaxAge(maxAge);
    return cookie;
  }

  public static Optional<String> extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
    return Optional.ofNullable(WebUtils.getCookie(httpRequest, CookieName.AUTH_TOKEN))
        .map(Cookie::getValue);
  }
}
