package com.granite.identity.application.authenticator.oidc;

public class OIDCAuthenticatorConstants {
    private OIDCAuthenticatorConstants() {
    }

    public static final String AUTHENTICATOR_NAME = "GraniteOpenIDConnectAuthenticator";
    public static final String LOGIN_TYPE = "OIDC";

    public static final String OAUTH_OIDC_SCOPE = "openid";
    public static final String OAUTH2_GRANT_TYPE_CODE = "code";
    public static final String OAUTH2_PARAM_STATE = "state";
    public static final String OAUTH2_ERROR = "error";

    public static final String ACCESS_TOKEN = "access_token";
    public static final String ID_TOKEN = "id_token";

    public static final String CLIENT_ID = "ClientId";
    public static final String CLIENT_SECRET = "ClientSecret";
    public static final String OAUTH2_AUTHZ_URL = "OAuth2AuthzEPUrl";
    public static final String OAUTH2_TOKEN_URL = "OAuth2TokenEPUrl";

    public static class Claim {
        private Claim() {
        }

        public static final String NAME = "name";
    }
}