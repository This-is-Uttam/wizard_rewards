package com.wizard.rewards720.Modals;

public class TrendingModal {
    String daysLeftText, spotLeftText, totalSpotText, trendItemName, pricePerSpot, shortDescription, details, trendItemId;
    String trendItemImg;
    boolean isSpotFull;

    public String getTrentItemMrp() {
        return trentItemMrp;
    }

    public void setTrentItemMrp(String trentItemMrp) {
        this.trentItemMrp = trentItemMrp;
    }

    String trentItemMrp;


    public TrendingModal(String daysLeftText, String spotLeftText, String trendItemName, String pricePerSpot, String trendItemImg,
                         String trentItemMrp, String totalSpotText) {
        this.daysLeftText = daysLeftText;
        this.spotLeftText = spotLeftText;
        this.trendItemName = trendItemName;
        this.pricePerSpot = pricePerSpot;
        this.trendItemImg = trendItemImg;
        this.trentItemMrp = trentItemMrp;
        this.totalSpotText = totalSpotText;
    }

    public String getTrendItemId() {
        return trendItemId;
    }

    public void setTrendItemId(String trendItemId) {
        this.trendItemId = trendItemId;
    }

    public String getDaysLeftText() {
        return daysLeftText;
    }

    public void setDaysLeftText(String daysLeftText) {
        this.daysLeftText = daysLeftText;
    }

    public String getSpotLeftText() {
        return spotLeftText;
    }

    public void setSpotLeftText(String spotLeftText) {
        this.spotLeftText = spotLeftText;
    }

    public String getTrendItemName() {
        return trendItemName;
    }

    public void setTrendItemName(String trendItemName) {
        this.trendItemName = trendItemName;
    }

    public String getPricePerSpot() {
        return pricePerSpot;
    }

    public void setPricePerSpot(String pricePerSpot) {
        this.pricePerSpot = pricePerSpot;
    }

    public String getTrendItemImg() {
        return trendItemImg;
    }

    public void setTrendItemImg(String trendItemImg) {
        this.trendItemImg = trendItemImg;
    }

    public String getTotalSpotText() {
        return totalSpotText;
    }

    public void setTotalSpotText(String totalSpotText) {
        this.totalSpotText = totalSpotText;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isSpotFull() {
        return isSpotFull;
    }

    public void setSpotFull(boolean spotFull) {
        isSpotFull = spotFull;
    }
}
