package com.intuit.developer.sampleapp.oauth2.controller;

import javax.servlet.http.HttpSession;

import com.intuit.developer.sampleapp.oauth2.service.InvoiceService;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.exception.InvalidTokenException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intuit.developer.sampleapp.oauth2.client.OAuth2PlatformClientFactory;
import com.intuit.ipp.core.Context;
import com.intuit.ipp.core.ServiceType;
import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.security.OAuth2Authorizer;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.ipp.util.Config;
import com.intuit.oauth2.client.OAuth2PlatformClient;
import com.intuit.oauth2.data.BearerTokenResponse;
import com.intuit.oauth2.exception.OAuthException;

import java.util.List;

/**
 * @author dderose
 */
@RestController
@RequestMapping("/api")
public class QBOController {

    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/company")
    public ResponseEntity<CompanyInfo> getCompanyInfo(HttpSession session) throws FMSException {
        CompanyInfo companyInfo = invoiceService.getCompanyInfo(session);
        return ResponseEntity.ok(companyInfo);
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getInvoices(
            HttpSession session,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws InvalidTokenException {
        List<Invoice> invoices = invoiceService.getInvoices(session, page, size);
        return ResponseEntity.ok(invoices);
    }

}
