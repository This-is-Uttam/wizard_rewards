package com.wizard.rewards720.Modals;

public class GiftModal {
    String giftCoins, giftRuppees;
    int giftImg;

    public GiftModal(String giftCoins, String giftRuppees, int giftImg) {
        this.giftCoins = giftCoins;
        this.giftRuppees = giftRuppees;
        this.giftImg = giftImg;
    }

    public String getGiftCoins() {
        return giftCoins;
    }

    public void setGiftCoins(String giftCoins) {
        this.giftCoins = giftCoins;
    }

    public String getGiftRuppees() {
        return giftRuppees;
    }

    public void setGiftRuppees(String giftRuppees) {
        this.giftRuppees = giftRuppees;
    }

    public int getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(int giftImg) {
        this.giftImg = giftImg;
    }
}
