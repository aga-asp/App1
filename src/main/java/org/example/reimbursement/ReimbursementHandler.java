package org.example.reimbursement;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.customexceptions.IncorrectQuery;
import org.example.login.LoginService;
import org.example.mappersandreaders.JsonCustomReader;
import org.example.mappersandreaders.QueryParametersMapperMultipleValues;
import org.example.customexceptions.NoMatchingRecordsException;
import org.example.customexceptions.UserDoesNotExistException;
import org.example.response.ResponseSender;
import org.example.mappersandreaders.QueryParametersMapper;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ReimbursementHandler implements HttpHandler {

    ReimbursementService reimbursementService;

    public ReimbursementHandler(ReimbursementService reimbursementService) {
        this.reimbursementService = reimbursementService;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        String methodString = httpExchange.getRequestMethod();
        String queryString = httpExchange.getRequestURI().toString();
        //Pattern pattern = Pattern.compile("/login\\?username=([^&]+)&password=([^&]+)");
        Matcher matcher;//pattern.matcher(queryString);
        switch (methodString) {
            case "GET" -> {
                Pattern pattern = Pattern.compile("/reimbursement\\?(\\w+=[^&]+)(&\\w+=[^&]+)*$");
                matcher = pattern.matcher(queryString);
                if (matcher.find()) {
                    handleGetRequest(httpExchange);
                } else {
                    ResponseSender.sendResponse(httpExchange, 404, "Not found");
                }
            }
            case "POST" -> {
                Pattern pattern = Pattern.compile("^/reimbursement$");
                matcher = pattern.matcher(queryString);
                if (matcher.find()) {
                    handlePostRequest(httpExchange);
                } else {
                    ResponseSender.sendResponse(httpExchange, 404, "Not found");
                }
            }
            case "PUT" -> {
                Pattern pattern = Pattern.compile("^/reimbursement$");
                matcher = pattern.matcher(queryString);
                if (matcher.find()) {
                    handlePutRequest(httpExchange);
                } else {
                    ResponseSender.sendResponse(httpExchange, 404, "Not found");
                }
            }
            case "DELETE" -> {
                Pattern pattern = Pattern.compile("/reimbursement\\?id=\\d+(,\\d+)*$");
                matcher = pattern.matcher(queryString);
                if (matcher.find()) {
                    handleDeleteRequest(httpExchange);
                } else {
                    ResponseSender.sendResponse(httpExchange, 404, "Not found");
                }
            }
            default -> ResponseSender.sendResponse(httpExchange, 404, "Operation not supported by the server");
        }
    }

    //Get users based on Query -TODO- Need common user/admin check (User should only be able to get own rows, admin can get everything)
    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        try {
            List<Reimbursement> matchingRecords = reimbursementService.serveGetRequest(QueryParametersMapper.mapToQueryParameters(query));
            if (matchingRecords.size() > 0) {
                String showResponse = matchingRecords.stream().map(Reimbursement::toStringCustom).collect(Collectors.joining(",\n"));
                ResponseSender.sendResponse(httpExchange, 200, showResponse);
            }
        } catch (NoMatchingRecordsException e) {
            ResponseSender.sendResponse(httpExchange, 404, "No such records to show");
        }

    }


    //Add user based on user Id (TBD) -TODO- Need common user/admin check (User should only be able to add own rows, admin can add row for every user)
    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        String requestBody = JsonCustomReader.readJson(httpExchange);
        try {
            reimbursementService.servePostRequest(requestBody);
            ResponseSender.sendResponse(httpExchange, 200, "Users added!");
        } catch (UserDoesNotExistException e) {
            ResponseSender.sendResponse(httpExchange, 404, "No user under this Id exists");
        }
    }

    //Change Reimbursement fields based on reimbursement id - TODO - Need common user/admin check (Only admin can update status, user can only update daily allowance and car millage on Pending or Waiting for correction)
    private void handlePutRequest(HttpExchange httpExchange) throws IOException {
        String requestBody = JsonCustomReader.readJson(httpExchange); //JSON_FIELDS String // make key value
        try {
            reimbursementService.servePutRequest(requestBody);
            ResponseSender.sendResponse(httpExchange, 200, "User updated!");
        } catch (UserDoesNotExistException e) {
            ResponseSender.sendResponse(httpExchange, 404, "No such record to update in database");
        }
    }

    //Delete row based on user Id -TODO- (User should only be able to delete Pending, admin can get delete everything)
    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (query != null) {
                reimbursementService.serveDeleteRequest(QueryParametersMapperMultipleValues.mapToQueryParameters(query));
                ResponseSender.sendResponse(httpExchange, 200, "Records deleted");
            }
        } catch (UserDoesNotExistException e) {
            ResponseSender.sendResponse(httpExchange, 404, "No such user record to delete in database");
        }
    }
}