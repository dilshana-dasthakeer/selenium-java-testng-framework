package com.dilshana.qa.data;

/** One row of login test data. */
public record LoginData(String description, String username, String password, String expectedError) {

    @Override
    public String toString() {
        return description;
    }
}
