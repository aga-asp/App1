package org.example.reimbursement;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InMemoryReimbursementRepository {

    private static Map<String, Reimbursement> reimbursementDataBase;
    private static InMemoryReimbursementRepository instance;

    public Map<String, Reimbursement> getReimbursementDataBaseValues() {
        return reimbursementDataBase;
    }

    private InMemoryReimbursementRepository(Map<String, Reimbursement> reimbursementDataBase) {
        InMemoryReimbursementRepository.reimbursementDataBase = reimbursementDataBase;
    }

    private static Map<String, Reimbursement> reimbursementBasicData() {
        HashMap<String, Reimbursement> ReimbursementData = new HashMap<>();
        Reimbursement reimbursement1 = new Reimbursement("0001", "2", new BigDecimal(12), new BigDecimal(7), ReimbursementStatus.PENDING);
        ReimbursementData.put(reimbursement1.getId(), reimbursement1);
        Reimbursement reimbursement2 = new Reimbursement("0002", "1", new BigDecimal(13), new BigDecimal(8), ReimbursementStatus.PENDING);
        ReimbursementData.put(reimbursement2.getId(), reimbursement2);
        Reimbursement reimbursement3 = new Reimbursement("0003", "2", new BigDecimal(14), new BigDecimal(9), ReimbursementStatus.APPROVED);
        ReimbursementData.put(reimbursement3.getId(), reimbursement3);
        Reimbursement reimbursement4 = new Reimbursement("0004", "5", new BigDecimal(15), new BigDecimal(2), ReimbursementStatus.PENDING);
        ReimbursementData.put(reimbursement4.getId(), reimbursement4);
        Reimbursement reimbursement5 = new Reimbursement("0006", "2", new BigDecimal(14), new BigDecimal(9), ReimbursementStatus.DENIED);
        ReimbursementData.put(reimbursement5.getId(), reimbursement5);
        Reimbursement reimbursement6 = new Reimbursement("0006", "5", new BigDecimal(14), new BigDecimal(9), ReimbursementStatus.RECALLED);
        ReimbursementData.put(reimbursement6.getId(), reimbursement6);
        return ReimbursementData;
    }

    public static InMemoryReimbursementRepository getDatabase() {
        if (instance == null) {
            instance = new InMemoryReimbursementRepository(reimbursementBasicData());
        }
        return instance;
    }


}
