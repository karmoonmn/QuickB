package com.intuit.developer.sampleapp.oauth2.service;

import com.intuit.ipp.data.CompanyInfo;
import com.intuit.ipp.data.Invoice;
import com.intuit.ipp.exception.FMSException;
import com.intuit.ipp.exception.InvalidTokenException;
import com.intuit.ipp.services.DataService;
import com.intuit.ipp.services.QueryResult;
import com.intuit.oauth2.exception.OAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class InvoiceServiceImp implements InvoiceService {

    @Autowired
    private QBOAuthService authService;

    @Autowired
    private MyDataService dataService;

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    @Override
    public CompanyInfo getCompanyInfo(HttpSession session) throws InvalidTokenException {
        authService.validateSession(session);

        try {
            DataService service = dataService.createDataService(session);
            String sql = "select * from CompanyInfo";
            QueryResult queryResult = service.executeQuery(sql);
            return (CompanyInfo) queryResult.getEntities().get(0);
        } catch (FMSException e) {
            throw new RuntimeException(e);
        } catch (OAuthException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Invoice> getInvoices(HttpSession session, int page, int size) throws InvalidTokenException {
        authService.validateSession(session);

        size = Math.min(size, MAX_PAGE_SIZE);
        page =  Math.max(page, 1);

        try {
            DataService service = dataService.createDataService(session);
            String sql = "SELECT * FROM Invoice ORDERBY Metadata.LastUpdatedTime DESC"
                    + " STARTPOSITION " + ((page - 1) * size) + " MAXRESULTS " + size;
            QueryResult queryResult = service.executeQuery(sql);
            return (List<Invoice>) queryResult.getEntities();

        } catch (OAuthException e) {
            throw new RuntimeException(e);
        } catch (FMSException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Invoice> getInvoiceById(HttpSession session, String id) throws InvalidTokenException {
        return null;
    }


}
