package com.granite.identity.application.authenticator.oidc.internal;

import com.granite.identity.application.authenticator.oidc.OpenIDConnectAuthenticator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.wso2.carbon.identity.application.authentication.framework.ApplicationAuthenticator;
import org.wso2.carbon.user.core.service.RealmService;

/**
 * @scr.component name="identity.application.authenticator.oidc.component" immediate="true"
 * @scr.reference name="realm.service"
 * interface="org.wso2.carbon.user.core.service.RealmService"cardinality="1..1"
 * policy="dynamic" bind="setRealmService" unbind="unsetRealmService"
 */
public class OpenIDConnectAuthenticatorServiceComponent {

    private static Log log = LogFactory.getLog(OpenIDConnectAuthenticatorServiceComponent.class);
    private static RealmService realmService;

    protected void activate(ComponentContext ctxt) {
        try {
            OpenIDConnectAuthenticator openIDConnectAuthenticator = new OpenIDConnectAuthenticator();

            ctxt.getBundleContext().registerService(ApplicationAuthenticator.class.getName(),
                                                    openIDConnectAuthenticator, null);
            if (log.isDebugEnabled()) {
                log.debug("Granite OpenID Connect Authenticator bundle is activated");
            }
        } catch (Throwable e) {
            log.fatal(" Error while activating oidc authenticator ", e);
        }
    }

    protected void deactivate(ComponentContext ctxt) {
        if (log.isDebugEnabled()) {
            log.debug("Granite OpenID Connect Authenticator bundle is deactivated");
        }
    }

    protected void setRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("Setting the Realm Service");
        }
        OpenIDConnectAuthenticatorServiceComponent.realmService = realmService;
    }

    protected void unsetRealmService(RealmService realmService) {
        if (log.isDebugEnabled()) {
            log.debug("UnSetting the Realm Service");
        }
        OpenIDConnectAuthenticatorServiceComponent.realmService = null;
    }

    public static RealmService getRealmService() {
        return realmService;
    }
}