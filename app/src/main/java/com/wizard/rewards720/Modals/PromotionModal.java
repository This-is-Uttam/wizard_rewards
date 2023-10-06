package com.wizard.rewards720.Modals;

public class PromotionModal     {
    String promotionImg;
    String promotionImgLink;

    public PromotionModal(String promotionImg, String promotionImgLink) {
        this.promotionImg = promotionImg;
        this.promotionImgLink = promotionImgLink;
    }

    public void setPromotionImg(String promotionImg) {
        this.promotionImg = promotionImg;
    }

    public String getPromotionImgLink() {
        return promotionImgLink;
    }

    public void setPromotionImgLink(String promotionImgLink) {
        this.promotionImgLink = promotionImgLink;
    }

    public String getPromotionImg() {
        return promotionImg;
    }
}
