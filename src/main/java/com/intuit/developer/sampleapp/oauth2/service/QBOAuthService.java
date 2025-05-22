package com.intuit.developer.sampleapp.oauth2.service;

import com.intuit.ipp.exception.InvalidTokenException;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;

@Service
public class QBOAuthService {

    public void validateSession(HttpSession session) throws InvalidTokenException {
        String realmId = (String) session.getAttribute("realmId");
        String accessToken = (String) session.getAttribute("access_token");

        if (StringUtils.isEmpty(realmId) || StringUtils.isEmpty(accessToken)) {
            throw new InvalidTokenException("Invalid realmId or accessToken");
        }
    }
}
