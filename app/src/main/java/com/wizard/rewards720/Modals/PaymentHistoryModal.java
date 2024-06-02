package com.wizard.rewards720.Modals;

public class PaymentHistoryModal {
    String purchaseAmt, purchaseCoin, purchaseDateTime, purchaseTxnId;
    int purchaseStatus;

    public PaymentHistoryModal(String purchaseAmt, String purchaseCoin, String purchaseDateTime, String purchaseTxnId) {
        this.purchaseAmt = purchaseAmt;
        this.purchaseCoin = purchaseCoin;
        this.purchaseDateTime = purchaseDateTime;
        this.purchaseTxnId = purchaseTxnId;
    }

    public int getPurchaseStatus() {
        return purchaseStatus;
    }

    public void setPurchaseStatus(int purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    public PaymentHistoryModal() {
    }

    public String getPurchaseAmt() {
        return purchaseAmt;
    }

    public void setPurchaseAmt(String purchaseAmt) {
        this.purchaseAmt = purchaseAmt;
    }

    public String getPurchaseCoin() {
        return purchaseCoin;
    }

    public void setPurchaseCoin(String purchaseCoin) {
        this.purchaseCoin = purchaseCoin;
    }

    public String getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(String purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }

    public String getPurchaseTxnId() {
        return purchaseTxnId;
    }

    public void setPurchaseTxnId(String purchaseTxnId) {
        this.purchaseTxnId = purchaseTxnId;
    }
}
