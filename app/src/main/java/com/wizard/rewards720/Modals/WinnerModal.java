package com.wizard.rewards720.Modals;

public class WinnerModal  {
    String winnerCount;
    String winnerFirstLetter;
    String winnerDescription;

    String winnerProductName;
    int winnerProductImg, winnerImg;

    String winVoucherName, winVoucherImg;

    public WinnerModal(String winnerCount, String winnerFirstLetter, String winnerDescription, String winnerProductName, int winnerProductImg) {
        this.winnerCount = winnerCount;
        this.winnerFirstLetter = winnerFirstLetter;
        this.winnerDescription = winnerDescription;
        this.winnerProductName = winnerProductName;
        this.winnerProductImg = winnerProductImg;
    }

    public String getWinVoucherName() {
        return winVoucherName;
    }

    public void setWinVoucherName(String winVoucherName) {
        this.winVoucherName = winVoucherName;
    }

    public String getWinVoucherImg() {
        return winVoucherImg;
    }

    public void setWinVoucherImg(String winVoucherImg) {
        this.winVoucherImg = winVoucherImg;
    }

    public String getWinnerCount() {
        return winnerCount;
    }

    public void setWinnerCount(String winnerCount) {
        this.winnerCount = winnerCount;
    }

    public String getWinnerFirstLetter() {
        return winnerFirstLetter;
    }

    public void setWinnerFirstLetter(String winnerFirstLetter) {
        this.winnerFirstLetter = winnerFirstLetter;
    }

    public String getWinnerDescription() {
        return winnerDescription;
    }

    public void setWinnerDescription(String winnerDescription) {
        this.winnerDescription = winnerDescription;
    }

    public int getWinnerProductImg() {
        return winnerProductImg;
    }

    public void setWinnerProductImg(int winnerProductImg) {
        this.winnerProductImg = winnerProductImg;
    }

    public int getWinnerImg() {
        return winnerImg;
    }

    public void setWinnerImg(int winnerImg) {
        this.winnerImg = winnerImg;

    }


    public String getWinnerProductName() {
        return winnerProductName;
    }

    public void setWinnerProductName(String winnerProductName) {
        this.winnerProductName = winnerProductName;
    }

}
