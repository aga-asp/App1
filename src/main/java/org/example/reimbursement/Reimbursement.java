package org.example.reimbursement;

import java.math.BigDecimal;
import java.util.Objects;

public class Reimbursement {
    private String Id;
    private String ownerId;
    private BigDecimal dailyAllowance;
    private BigDecimal carMillage;

    private ReimbursementStatus reimbursementStatus;

    public String getId() {
        return Id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public BigDecimal getDailyAllowance() {
        return dailyAllowance;
    }

    public BigDecimal getCarMillage() {
        return carMillage;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setDailyAllowance(BigDecimal dailyAllowance) {
        this.dailyAllowance = dailyAllowance;
    }

    public void setCarMillage(BigDecimal carMillage) {
        this.carMillage = carMillage;
    }

    public ReimbursementStatus getReimbursementType() {
        return reimbursementStatus;
    }

    public void setReimbursementType(ReimbursementStatus reimbursementStatus) {
        this.reimbursementStatus = reimbursementStatus;
    }

    public Reimbursement(){}

    public Reimbursement(String id, String ownerId, BigDecimal dailyAllowance, BigDecimal carMillage, ReimbursementStatus reimbursementStatus) {
        this.Id = id;
        this.ownerId = ownerId;
        this.dailyAllowance = dailyAllowance;
        this.carMillage = carMillage;
        this.reimbursementStatus = reimbursementStatus;

    }

    public String toStringCustom() {
         return "Reimbursement{" +
                "Id=" + Id  +
                ", ownerId=" + ownerId  +
                ", dailyAllowance=" + dailyAllowance +
                ", carMillage=" + carMillage +
                ", reimbursementType=" + reimbursementStatus +
                '}';
    }

    @Override
    public String toString() {
        return "Reimbursement{" +
                "Id='" + Id + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", dailyAllowance=" + dailyAllowance +
                ", carMillage=" + carMillage +
                ", reimbursementStatus=" + reimbursementStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reimbursement that)) return false;
        return getId().equals(that.getId()) && Objects.equals(getOwnerId(), that.getOwnerId()) && Objects.equals(getDailyAllowance(), that.getDailyAllowance()) && Objects.equals(getCarMillage(), that.getCarMillage()) && reimbursementStatus == that.reimbursementStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerId(), getDailyAllowance(), getCarMillage(), reimbursementStatus);
    }
}
