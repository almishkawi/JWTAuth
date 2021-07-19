package com.mo.JWTAuth.security;

import java.util.Date;

public class WebSecurityConstants {
    public static final String SIGN_UP_URL = "/api/users";
    public static final String LOGIN_URL = "/api/login";
    public static final long EXPIRATION_TIME = 864_000_00; // 1 day
    public static final String SECRET_TOKEN = "7hs7D2H6UCd79D$qb!6*Msg!nW84BJ";
    public static final String AUTH_HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
