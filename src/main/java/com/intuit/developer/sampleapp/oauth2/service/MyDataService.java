package com.intuit.developer.sampleapp.oauth2.service;

import com.intuit.developer.sampleapp.oauth2.client.OAuth2PlatformClientFactory;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.exception.InvalidTokenException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.util.Config;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.OAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class MyDataService {

    @Autowired
    private OAuth2PlatformClientFactory factory;

    public DataService createDataService(HttpSession session) throws OAuthException, FMSException {
        String realmId = (String) session.getAttribute("realmId");
        String accessToken = (String) session.getAttribute("access_token");

        try {
            Config.setProperty(Config.BASE_URL_QBO, factory.getPropertyValue("IntuitAccountingAPIHost") + "/v3/company/");
            OAuth2Authorizer oauth = new OAuth2Authorizer(accessToken);
            Context context = new Context(oauth, ServiceType.QBO, realmId);
            return new DataService(context);
        } catch (InvalidTokenException e) {
            refreshToken(session);
            OAuth2Authorizer oauth = new OAuth2Authorizer(session.getAttribute("access_token").toString());
            Context context = new Context(oauth, ServiceType.QBO, realmId);
            return new DataService(context);
        } catch (FMSException e) {
            throw new RuntimeException(e);
        }
    }

    private String refreshToken(HttpSession session) throws OAuthException {
        String refreshToken = (String) session.getAttribute("refresh_token");
        OAuth2PlatformClient client = factory.getOAuth2PlatformClient();
        BearerTokenResponse bearerTokenResponse = client.refreshToken(refreshToken);

        session.setAttribute("access_token", bearerTokenResponse.getAccessToken());
        session.setAttribute("refresh_token", bearerTokenResponse.getRefreshToken());

        return bearerTokenResponse.getAccessToken();
    }
}
