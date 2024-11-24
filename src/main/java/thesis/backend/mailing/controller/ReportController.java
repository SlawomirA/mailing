package thesis.backend.mailing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.exception.ReportExceptions.ReportGenerationException;
import thesis.backend.mailing.exception.ReportExceptions.RetrieveReportDataViewNameValidationException;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.GenerateXlsxRequest;
import thesis.backend.mailing.model.Request.SendEmailRequest;
import thesis.backend.mailing.model.Response.Response;
import thesis.backend.mailing.model.Response.SendedEmailResponse;
import thesis.backend.mailing.model.Response.XlsxReportResponse;
import thesis.backend.mailing.service.EmailingService;
import thesis.backend.mailing.service.ReportService;
import thesis.backend.mailing.utils.Consts;

import java.io.IOException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("reportController")
@Scope("request")
public class ReportController {

    @Autowired
    ReportService reportService;



    @PostMapping(value = "/generateXlsx", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Generate xlsx.",
            description = "Generate xlsx from given data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<String>> generateXlsx(@RequestBody GenerateXlsxRequest generateXlsxRequest) throws ReportGenerationException {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        reportService.generateXlsx(generateXlsxRequest)));
    }

    @GetMapping(value = "/retrieveAvailableReports", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve available report views.",
            description = "Retrieve available report views (starting with rap_ and ending with _v )")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<List<String>>> showAllAvailableReports() {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        reportService.showAllAvailableReports()));
    }


    @GetMapping(value = "/retrieveReportData", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Retrieve report data.",
            description = "Retrieve report data by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
    })
    public ResponseEntity<Response<XlsxReportResponse>> retrieveReportData(@RequestParam String viewName) throws RetrieveReportDataViewNameValidationException {
        return ResponseEntity.ok(
                new Response<>(
                        Consts.C200,
                        200,
                        "",
                        reportService.retrieveReportData(viewName)));
    }


}
