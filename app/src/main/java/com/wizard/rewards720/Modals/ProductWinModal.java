package com.wizard.rewards720.Modals;

public class ProductWinModal {
    String userName, userImage, productName, productImage, mrp;
    int productWinnerCount;

    public ProductWinModal(String userName, String userImage, String productName, String productImage, String mrp) {
        this.userName = userName;
        this.userImage = userImage;
        this.productName = productName;
        this.productImage = productImage;
        this.mrp = mrp;
    }

    public int getProductWinnerCount() {
        return productWinnerCount;
    }

    public void setProductWinnerCount(int productWinnerCount) {
        this.productWinnerCount = productWinnerCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }
}
