package com.wizard.rewards720.Modals;

public class TrakierMainListModal {
    String adTitle;
    String adDesc;
    String adRewardCoin;
    String adClaimBtn;
    String adPosterImg, adIcon;
    int adId, totalTasks;

    public TrakierMainListModal(String adPosterImg, String adIcon, String adTitle, String adDesc, String adRewardCoin, String adClaimBtn) {
        this.adPosterImg = adPosterImg;
        this.adIcon = adIcon;
        this.adTitle = adTitle;
        this.adDesc = adDesc;
        this.adRewardCoin = adRewardCoin;
        this.adClaimBtn = adClaimBtn;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public String getAdPosterImg() {
        return adPosterImg;
    }

    public String getAdIcon() {
        return adIcon;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public void setAdDesc(String adDesc) {
        this.adDesc = adDesc;
    }

    public void setAdRewardCoin(String adRewardCoin) {
        this.adRewardCoin = adRewardCoin;
    }

    public void setAdClaimBtn(String adClaimBtn) {
        this.adClaimBtn = adClaimBtn;
    }

    public void setAdPosterImg(String adPosterImg) {
        this.adPosterImg = adPosterImg;
    }
    public String getAdTitle() {
        return adTitle;
    }

    public String getAdDesc() {
        return adDesc;
    }

    public String getAdRewardCoin() {
        return adRewardCoin;
    }

    public String getAdClaimBtn() {
        return adClaimBtn;
    }

    public void setAdIcon(String adIcon) {
        this.adIcon = adIcon;
    }
}
