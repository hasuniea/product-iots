/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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