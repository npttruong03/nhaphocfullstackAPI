package com.NhapHocVKUFullStack.Service;

import org.springframework.stereotype.Service;
import java.io.IOException;

import com.NhapHocVKUFullStack.models.PickupRegistrationData;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PickupRegistrationService {

    private static final String SPREADSHEET_ID = "1ZVs7tFi4qgbBxb08uAniGCZXIdocqk3zHAf8KA6GFKU";

    public void processRegistration(PickupRegistrationData pickupRegistrationData) {
        if (pickupRegistrationData == null) {
            throw new NullPointerException("The pickupRegistrationData object cannot be null.");
        }

        try {
            // Create a Google Sheets service object.
            Sheets sheetsService = createSheetsService();

            // Prepare the data to be written to Google Sheets.
            List<Object> dataRow = new ArrayList<>();
            dataRow.add(pickupRegistrationData.getName());
            dataRow.add(pickupRegistrationData.getEmail());
            dataRow.add(pickupRegistrationData.getPhoneNumber());
            dataRow.add(pickupRegistrationData.getPickupDate());
            dataRow.add(pickupRegistrationData.getPickupTime());

            // Write the data to Google Sheets.
            ValueRange appendBody = new ValueRange().setValues(Arrays.asList(dataRow));
            sheetsService.spreadsheets().values()
                    .append(SPREADSHEET_ID, "Sheet1", appendBody)
                    .setValueInputOption("RAW")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

            System.out.println("Registration data has been successfully saved to Google Sheets.");
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
            System.out.println("An error occurred while saving registration data to Google Sheets.");
        }
    }

    private Sheets createSheetsService() throws IOException, GeneralSecurityException {
        // Load the OAuth 2.0 credentials from the JSON file.
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = GoogleCredential.fromStream(
                getClass().getResourceAsStream("/path/to/your/credentials.json"))
                .createScoped(Arrays.asList(SheetsScopes.SPREADSHEETS));

        // Create the Google Sheets service object.
        return new Sheets.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("Pickup Registration App")
                .build();
    }
}