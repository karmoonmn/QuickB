package com.intuit.developer.sampleapp.oauth2.service;

import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface InvoiceService {

    CompanyInfo getCompanyInfo(HttpSession session) throws FMSException;

    List<Invoice> getInvoices(HttpSession session, int page, int size) throws InvalidTokenException;

    List<Invoice> getInvoiceById(HttpSession session, String id) throws InvalidTokenException;
}
