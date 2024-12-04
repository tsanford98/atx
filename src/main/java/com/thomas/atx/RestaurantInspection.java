package com.thomas.atx;

public record RestaurantInspection(
    String restaurant_name,
    String inspection_date,
    String score,
    String process_description,
    String zip_code,
    Address address
) {
    public record Address(
        String human_address
    ) {}

    public String getScore() {
        return score;
    }
}





