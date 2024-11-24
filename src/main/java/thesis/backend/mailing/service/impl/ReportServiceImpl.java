package thesis.backend.mailing.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import thesis.backend.mailing.dto.CreateEmailTemplateDTO;
import thesis.backend.mailing.dto.SendEmailDTO;
import thesis.backend.mailing.exception.AuthenticationExceptions.EmailTypeNotFoundException;
import thesis.backend.mailing.exception.AuthenticationExceptions.MalformedEmailTemplateException;
import thesis.backend.mailing.exception.AuthenticationExceptions.SendEmailException;
import thesis.backend.mailing.exception.ReportExceptions.ReportGenerationException;
import thesis.backend.mailing.exception.ReportExceptions.RetrieveReportDataViewNameValidationException;
import thesis.backend.mailing.model.MySQL.Attachment;
import thesis.backend.mailing.model.MySQL.EmailTemplate;
import thesis.backend.mailing.model.Request.GenerateXlsxRequest;
import thesis.backend.mailing.model.Request.SendEmailRequest;
import thesis.backend.mailing.model.Response.SendedEmailResponse;
import thesis.backend.mailing.model.Response.XlsxReportResponse;
import thesis.backend.mailing.repository.AttachmentRepository;
import thesis.backend.mailing.repository.EmailTemplateRepository;
import thesis.backend.mailing.service.EmailingService;
import thesis.backend.mailing.service.ReportService;
import thesis.backend.mailing.validator.CreateEmailTemplateValidator;
import thesis.backend.mailing.validator.SendEmailValidator;
import thesis.backend.mailing.validator.ViewNameValidator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Implementation of EmailingService interface providing methods for JWT token generation, extraction, and validation.
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ViewNameValidator viewNameValidator;

    /**
     * Generates xlsx report from the given headers and rows
     * @param generateXlsxRequest
     * @return
     * @throws ReportGenerationException
     */
    @Override
    public String generateXlsx(GenerateXlsxRequest generateXlsxRequest) throws ReportGenerationException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Sheet1");

            //Get headers and rows data
            List<String> headers = generateXlsxRequest.getHeaders();
            List<List<String>> rows = generateXlsxRequest.getRows();

            // Create header row
            Row headerRow = sheet.createRow(0);
            IntStream.range(0, headers.size())
                    .forEach(i -> headerRow.createCell(i).setCellValue(headers.get(i)));

            // Create data rows
            IntStream.range(0, rows.size())
                    .forEach(i -> {
                        Row row = sheet.createRow(i + 1);
                        List<String> dataRow = rows.get(i);
                        IntStream.range(0, dataRow.size())
                                .forEach(j -> row.createCell(j).setCellValue(dataRow.get(j)));
                    });

            // Write to ByteArrayOutputStream
            workbook.write(baos);

            // Encode to Base64 and return
            byte[] xlsxBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(xlsxBytes);
            }
        catch (IOException e) {
            throw new ReportGenerationException(e.getMessage());
        }

    }


    private String capitalizeWords(String str) {
        String[] words = str.split(" ");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }
    @Override
    public List<String> showAllAvailableReports() {
        String query = "SELECT table_name FROM information_schema.views WHERE table_name LIKE 'rap_%_v'";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            String tableName = rs.getString("table_name");
            // Remove 'rap_' and '_v'
            tableName = tableName.replace("rap_", "").replace("_v", "")
                                 .replace("_", " ");
            // Convert the first letter of each word to uppercase
            tableName = capitalizeWords(tableName);
            return tableName;
        });
    }

    /**
     * Adds proper sufixes and prefixes, replaces spaces with underscores to match view pattern
     * @param viewName
     * @return
     */
    private String transformToOriginalViewName(String viewName) {
        return "rap_" +
                Arrays.stream(viewName.split(" "))      // Split by spaces
                        .map(word -> word.substring(0, 1).toLowerCase() + word.substring(1)) //Decapitalize first letter of each word
                        .collect(Collectors.joining("_")) // Join by underscores
                + "_v";
    }

    /**
     * Retrieves rows and headers from the given view
     * @param viewName
     * @return
     * @throws RetrieveReportDataViewNameValidationException
     */
    @Override
    public XlsxReportResponse retrieveReportData(String viewName) throws RetrieveReportDataViewNameValidationException {
        viewName = transformToOriginalViewName(viewName);

        //Validate given viewName, preventing SQL injection
        if(!viewNameValidator.validate(viewName))
            throw new RetrieveReportDataViewNameValidationException(viewNameValidator.describe());

        // Initialize headers and rows lists
        List<String> headers = new ArrayList<>();
        List<List<String>> rows = new ArrayList<>();

        jdbcTemplate.query("SELECT * FROM " + viewName, resultSet -> {
            // Retrieve headers
            if (resultSet.isFirst()) {
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++)
                    headers.add(metaData.getColumnName(i));

            }

            // Retrieve rows
            List<String> row = new ArrayList<>();
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++)
                row.add(resultSet.getString(i));

            rows.add(row);
        });

        return XlsxReportResponse.builder()
                .headers(headers)
                .rows(rows)
                .build();
    }
}
