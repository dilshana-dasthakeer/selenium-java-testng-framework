package com.dilshana.qa.data;

/** One row of checkout form test data. */
public record CheckoutData(String description, String firstName, String lastName, String postalCode,
                           String expectedError) {

    @Override
    public String toString() {
        return description;
    }
}
