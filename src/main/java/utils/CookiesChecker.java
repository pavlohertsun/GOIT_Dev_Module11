package utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class CookiesChecker {
    public static boolean checkIfContainsCookie(HttpServletRequest req){
        Optional<Cookie[]> optionalCookies = Optional.ofNullable(req.getCookies());

        if(optionalCookies.isPresent())
            return true;

        return false;
    }
}
