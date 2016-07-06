package com.nianticlabs.nia.iap;

class PurchasableItemDetails {
    private String description;
    private String itemId;
    private String price;
    private String title;

    PurchasableItemDetails(String itemId, String title, String description, String price) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.price = price;
    }

    String getItemId() {
        return this.itemId;
    }

    String getTitle() {
        return this.title;
    }

    String getDescription() {
        return this.description;
    }

    String getPrice() {
        return this.price;
    }
}
