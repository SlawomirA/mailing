package thesis.backend.mailing.service;


import jakarta.mail.MessagingException;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.exception.ReportExceptions.ReportGenerationException;
import thesis.backend.mailing.exception.ReportExceptions.RetrieveReportDataViewNameValidationException;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.GenerateXlsxRequest;
import thesis.backend.mailing.model.Request.SendEmailRequest;
import thesis.backend.mailing.model.Response.SendedEmailResponse;
import thesis.backend.mailing.model.Response.XlsxReportResponse;

import java.io.IOException;
import java.util.List;

public interface ReportService {

    String generateXlsx(GenerateXlsxRequest generateXlsxRequest) throws ReportGenerationException;
    List<String> showAllAvailableReports() ;
     XlsxReportResponse retrieveReportData(String viewName) throws RetrieveReportDataViewNameValidationException;
}
