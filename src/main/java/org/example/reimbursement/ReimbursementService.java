package org.example.reimbursement;

import org.example.customexceptions.NoMatchingRecordsException;
import org.example.customexceptions.UserDoesNotExistException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReimbursementService {
    private static Map<String, Reimbursement> reimbursementDataBase = InMemoryReimbursementRepository.getDatabase().getReimbursementDataBaseValues();


    List<Reimbursement> serveGetRequest(Map<String, String> params) {
        return getResults(params);
    }

    void servePostRequest(String requestBody) {
        splitRequest(requestBody);
    }

    void servePutRequest(String requestBody) {
        splitRequest(requestBody);
    }

    void serveDeleteRequest(Map<String, String[]> params) {
        deleteMatchingRecords(params);
    }

    private void splitRequest(String requestBody) {
        Pattern pattern = Pattern.compile("\\{[^}]*?\\}");
        Matcher matcher = pattern.matcher(requestBody);
        while (matcher.find()) {
            String matchedObject = matcher.group();
            extractValues(matchedObject);
        }
    }

    private void extractValues(String matchedObject) {
        String jsonInput = matchedObject.replace("{", "").replace("}", "");//TODO check if  there is need for this line
        String id = extractSingleValue(jsonInput, "id");
        String ownerId = extractSingleValue(jsonInput, "ownerId");
        String dailyAllowance = extractSingleValue(jsonInput, "dailyAllowance");
        String carMillage = extractSingleValue(jsonInput, "carMillage");
        String reimbursementStatus = extractSingleValue(jsonInput, "reimbursementStatus");
        if (checkIfUserIdExist(id)) {
            addRecordsToDatabase(id, ownerId, dailyAllowance, carMillage, reimbursementStatus);
        } else {
            throw new UserDoesNotExistException();
        }
    }

    private String extractSingleValue(String value, String field) {
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
    private boolean checkIfUserIdExist(String ownerId) {
        return false;
    }

    private void addRecordsToDatabase(String Id, String ownerId, String dailyAllowance, String carMillage, String reimbursementStatus) {
        Reimbursement reimbursementNewValue
                = new Reimbursement(Id, ownerId, new BigDecimal(dailyAllowance), new BigDecimal(carMillage), ReimbursementStatus.valueOf(reimbursementStatus));
        if (reimbursementDataBase.containsKey(Id)) {
            updateObjectFields(reimbursementDataBase.get(Id), reimbursementNewValue);
        } else {
            reimbursementDataBase.put(reimbursementNewValue.getId(), reimbursementNewValue);
        }
    }

    //SERIO NIE MA NIC INNEGO POZA GETTERAMI I SETTERAMI?
    private void updateObjectFields(Reimbursement reimbursement, Reimbursement reimbursementNewValue) {
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

    private List<Reimbursement> getResults(Map<String, String> params) {
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

    private Optional<Reimbursement> getMatchingRecords(Map<String, String> params, Reimbursement reimbursement) {
        String reimbursementString = reimbursement.toStringCustom();
        for (Map.Entry<String, String> param : params.entrySet()) {
            String paramAndValue = param.getKey() + "=" + param.getValue();
            if (!(reimbursementString.contains(paramAndValue))) {
                return Optional.empty();
            }
        }
        return Optional.of(reimbursement);
    }

    //--TODO check if record even exists
    private void deleteMatchingRecords(Map<String, String[]> matchingRecords) {
        if (matchingRecords.containsKey("id")) {
            String[] arrayToDelete = matchingRecords.get("id");
            for (String id : arrayToDelete) {
                if (!checkIfUserIdExist(id)) {
                    throw new UserDoesNotExistException();
                }
            }
            removeFromDatabase(arrayToDelete);
        }
    }

    private void removeFromDatabase(String[] recordsToDelete) {
        for (String id : recordsToDelete) {
            reimbursementDataBase.remove(id);
        }
    }
}
