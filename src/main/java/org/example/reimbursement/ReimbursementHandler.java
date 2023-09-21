package org.example.reimbursement;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.example.common_classes.JsonCustomReader;
import org.example.common_classes.QueryParametersMapperMultipleValues;
import org.example.custom_errors.NoMatchingRecordsException;
import org.example.custom_errors.UserDoesNotExistException;
import org.example.response.Response;
import org.example.common_classes.QueryParametersMapper;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class ReimbursementHandler implements HttpHandler {

    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "GET" -> handleGetRequest(httpExchange);
            case "POST" -> handlePostRequest(httpExchange);
            case "PUT" -> handlePutRequest(httpExchange);
            case "DELETE" -> handleDeleteRequest(httpExchange);
            default -> {
            }
        }
    }

    //Get users based on Query -TODO- Need common user/admin check (User should only be able to get own rows, admin can get everything)
    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        try {
            List<Reimbursement> matchingRecords = ReimbursementService.serveGetRequest(QueryParametersMapper.mapQueryParameters(query));
            if (matchingRecords.size() > 0) {
                String showResponse = matchingRecords.stream().map(Reimbursement::toStringCustom).collect(Collectors.joining(",\n"));
                Response.sendResponse(httpExchange, 200, showResponse);
            }
        } catch (NoMatchingRecordsException e) {
            Response.sendResponse(httpExchange, 404, "No such records to show");
        }

    }


    //Add user based on user Id (TBD) -TODO- Need common user/admin check (User should only be able to add own rows, admin can add row for every user)
    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        String requestBody = JsonCustomReader.readJson(httpExchange);
        try {
            if (requestBody != null) {
                ReimbursementService.servePostRequest(requestBody);
                Response.sendResponse(httpExchange, 200, "Users added!");
            }
        } catch (UserDoesNotExistException e) {
            Response.sendResponse(httpExchange, 404, "No user under this Id exists");
        }
    }

    //Change Reimbursement fields based on reimbursement id - TODO - Need common user/admin check (Only admin can update status, user can only update daily allowance and car millage on Pending or Waiting for correction)
    private void handlePutRequest(HttpExchange httpExchange) throws IOException{
        String requestBody = JsonCustomReader.readJson(httpExchange); //JSON_FIELDS String // make key value
        try {
            if (requestBody != null) {
                ReimbursementService.servePutRequest(requestBody);
                Response.sendResponse(httpExchange, 200, "User updated!");
            }
        } catch (UserDoesNotExistException e){
            Response.sendResponse(httpExchange, 404, "No such record to update in database");
        }
    }

    //Delete row based on user Id -TODO- (User should only be able to delete Pending, admin can get delete everything)
    private void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        try {
            if (query != null) {
                ReimbursementService.serveDeleteRequest(QueryParametersMapperMultipleValues.mapQueryParameters(query));
                Response.sendResponse(httpExchange, 200, "Records deleted");
            }
        } catch (NoMatchingRecordsException e) {
            Response.sendResponse(httpExchange, 404, "No such user record to delete in database");
        }
    }
}