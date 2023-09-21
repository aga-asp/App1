package org.example.reimbursement;

import org.example.custom_errors.NoMatchingRecordsException;
import org.example.custom_errors.UserDoesNotExistException;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReimbursementService {
    private static Map<String, Reimbursement> reimbursementDataBase = ReimbursementInMemoryDatabase.createSingleton().getReimbursementDataBase();


    static List<Reimbursement> serveGetRequest(Map<String, String> params) throws NoMatchingRecordsException {
        return getResults(params);
    }

    static void servePostRequest(String requestBody) throws UserDoesNotExistException {
        splitRequest(requestBody);
    }

    static void servePutRequest(String requestBody) throws UserDoesNotExistException {
        splitRequest(requestBody);
    }

    public static void serveDeleteRequest(Map<String, String[]> params) throws NoMatchingRecordsException {
        deleteMatchingRecords(params);
        System.out.println();
    }

    static private void splitRequest(String requestBody) throws UserDoesNotExistException {
        Pattern pattern = Pattern.compile("\\{[^}]*?\\}");
        Matcher matcher = pattern.matcher(requestBody);
        while (matcher.find()) {
            String matchedObject = matcher.group();
            extractValues(matchedObject);
        }
    }

    static private void extractValues(String matchedObject) throws UserDoesNotExistException {
        String jsonInput = matchedObject.replace("{", "").replace("}", "");//TODO check if  there is need for this line
        String id = extractSingleValue(jsonInput, "id");
        String ownerId = extractSingleValue(jsonInput, "ownerId");
        String dailyAllowance = extractSingleValue(jsonInput, "dailyAllowance");
        String carMillage = extractSingleValue(jsonInput, "carMillage");
        String reimbursementStatus = extractSingleValue(jsonInput, "reimbursementStatus");
        createNewRowInDatabase(id, ownerId, dailyAllowance, carMillage, reimbursementStatus);
    }

    static private void createNewRowInDatabase(String id, String ownerId, String dailyAllowance, String carMillage, String reimbursementStatus) throws UserDoesNotExistException {
        if (checkIfUserIdExist()) {
            addRecordsToDatabase(id, ownerId, dailyAllowance, carMillage, reimbursementStatus);
        } else {
            throw new UserDoesNotExistException();
        }
    }

    private static String extractSingleValue(String value, String field) {
        String fieldPattern = "\"" + field + "\": \"(.*?)\"";
        Pattern pattern = Pattern.compile(fieldPattern);
        Matcher matcher = pattern.matcher(value);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    //TODO - WARUNEK DO ZROBIENIA CHECKIFUSEREXISTS
    //TODO - DODATKOWY WARUNEK SPRAWDZIĆ ZALOGOWANEGO UŻYTKOWNIKA NA POLU CURRENT USER NAME
    private static boolean checkIfUserIdExist(/*String ownerId*/) {
        return true;
    }

    static private void addRecordsToDatabase(String Id, String ownerId, String dailyAllowance, String carMillage, String reimbursementStatus) {
        Reimbursement reimbursementNewValue
                = new Reimbursement(Id, ownerId, new BigDecimal(dailyAllowance), new BigDecimal(carMillage), ReimbursementStatus.valueOf(reimbursementStatus));
        if (reimbursementDataBase.containsKey(Id)) {
            updateObjectFields(reimbursementDataBase.get(Id), reimbursementNewValue);
        } else {
            reimbursementDataBase.put(reimbursementNewValue.getId(), reimbursementNewValue);
        }
    }

    //SERIO NIE MA NIC INNEGO POZA GETTERAMI I SETTERAMI?
    private static void updateObjectFields(Reimbursement reimbursement, Reimbursement reimbursementNewValue) {
        if (!reimbursement.equals(reimbursementNewValue)) {
            if (!reimbursement.getDailyAllowance().equals(reimbursementNewValue.getDailyAllowance())) {
                reimbursement.setDailyAllowance(reimbursementNewValue.getDailyAllowance());
            }
            if (!reimbursement.getCarMillage().equals(reimbursementNewValue.getCarMillage())) {
                reimbursement.setCarMillage(reimbursementNewValue.getCarMillage());
            }
            if (!reimbursement.getReimbursementType().equals(reimbursementNewValue.getReimbursementType())) {
                reimbursement.setReimbursementType(reimbursementNewValue.getReimbursementType());
            }
        }
    }

    private static List<Reimbursement> getResults(Map<String, String> params) throws NoMatchingRecordsException {
        List<Reimbursement> output = new ArrayList<>();
        for (Map.Entry<String, Reimbursement> entry : reimbursementDataBase.entrySet()) {
            Optional<Reimbursement> optionalReimbursement = getMatchingRecords(params, entry.getValue());
            optionalReimbursement.ifPresent(output::add);
        }
        if (output.size() > 0) {
            return output;
        }
        throw new NoMatchingRecordsException();
    }

    private static Optional<Reimbursement> getMatchingRecords(Map<String, String> params, Reimbursement reimbursement) {
        String reimbursementString = reimbursement.toStringCustom();
        for (Map.Entry<String, String> param : params.entrySet()) {
            String paramAndValue = param.getKey() + "=" + param.getValue();
            if (!(reimbursementString.contains(paramAndValue))) {
                return Optional.empty();
            }
        }
        return Optional.of(reimbursement);
    }

    private static void deleteMatchingRecords(Map<String, String[]> matchingRecords) throws NoMatchingRecordsException {
        if (matchingRecords.containsKey("id")) {
            String[] arrayToDelete = matchingRecords.get("id");
            for (String id : arrayToDelete) {
                removeFromDatabase(id);
            }
        }
    }
    private static void removeFromDatabase(String id) throws NoMatchingRecordsException {
        if (reimbursementDataBase.containsKey(id)) {
            reimbursementDataBase.remove(id);
        } else {
            throw new NoMatchingRecordsException();
        }
    }
}
