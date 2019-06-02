package pl.edu.agh.im.remotepatientmonitor.auth;

public class SecurityConstants {
    static final String SECRET = "SecretKeyToGenJWTs";
    static final long EXPIRATION_TIME = 1_200_000; // 20 minutes
    static final long REFRESH_TOKEN_EXPIRATION_TIME = 2_592_000_000L; // 1 month
    public static final long MAIL_ACTIVATION_EXPIRATION_TIME = 3_600_000; // 1 hour
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/users/sign-up";
    static final String LOGOUT_URL = "/pages/logout";
}
