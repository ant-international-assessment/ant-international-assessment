package com.ant.assessment.simulation.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws IOException {
        String base64 = "ewogICJ0eXBlIjogInNlcnZpY2VfYWNjb3VudCIsCiAgInByb2plY3RfaWQiOiAiYW50LWNhci1yYWNpbmctc3lzdGVtIiwKICAicHJpdmF0ZV9rZXlfaWQiOiAiMDE1NzMyZTBkYTVlNWRlZjY3ZjViZTg0OTEwOWM2NmQ1ZmM2NDU2YyIsCiAgInByaXZhdGVfa2V5IjogIi0tLS0tQkVHSU4gUFJJVkFURSBLRVktLS0tLVxuTUlJRXZRSUJBREFOQmdrcWhraUc5dzBCQVFFRkFBU0NCS2N3Z2dTakFnRUFBb0lCQVFEU2xiNTZiS1FPdWQxOFxuOVdGU2lPcWl4blVyaW9RVjU3R0lUL1RDdTdvTGJ1NGlPODZUWGVjMjQzN3F1Mmg0Q1JmSDhzMmh6NVJVYWtJMVxucXFMeEFMbWJab2ZEVkMxTGhUZ1lqTzJRM2ZxZEV3TXpveG91S0FLUW1oYlVaeENqbmI0ZDhiaXZWeG43UUp4Z1xuQjdoMENCb0R2bSsvMExqYWFTRFZ3am12Y2I5d1pvRGovb3liVDJlMEc5VUhnMG5YV01sanc5UGJpK0J5S2U0eVxuc3ZyV29adWw5dzRXMW43bTNneE9rV2pVZEo5ZmxuTmFIeUpLSVoyQ3NiRmFxaXdoS3N4NlJzQmxwTE9XVExWalxudlNSQXZ3YysrVlQ2VHlaemk3Wjhrd0s2aWVPQkN1QlFRbVREN0U2bzN0VE12Tyt6elNWb0Z5dnd3NmxtaXcvV1xuUHg4TUh5Y0xBZ01CQUFFQ2dnRUFCNmM3alNpc2JPYjcrRklJUmd2U25NNmlCazU2TzZKZW5zYWQ3WjBOWU9QTFxueTJDMlFxWUcwVjdjYmNNWXFrVzc3VktUR0p0Z2ZoR3lqUnhiZWFKa2d1QzRkR2ZkeE1FUTUzSHBWZnF4c1REcFxuZitCVHo5b0ZtUXFRbHdwaVJEbWFaZFBNN1JjNUYrZnZiVjA2ZDRIaGVDZ3AyTDNJL1VWN29jK1BDeVBmd2tHOFxuNk9xQmNESTlicWtwSG9QK2tabyt3WWd3cnZMZjNxcklxSHoranVIdWdNNlhRS3dNVm9jN0RkdTZNbWNTd1FUY1xuYzFOVWFIV0RPcWZpMkhNQXBhbXhLemdzQnNhN3hxaFJUMDRabGNnK005VWhhaGdPL3daU3pkMDRDUnZQOXhaTFxua0s0R2x6ZFVEbElOd3JKbUlNOWs5Qmp0ZS9INjB3cnJNRFN2T2ZyRmtRS0JnUUQ0bVJ6eHhoRW1IOTVkTi83eVxubHpMQzNFa3BTbTNZQVlickVCaVRNL0o5UFRNdEI2UHgrSk5FcENXbTBMU01ZeWI4RDlBN1ZEL2NBNDlVRkdORVxuQmkwY0xyOWtqemJaOVdORE5uRzBPWmFVWGV2anNWck1DSTg3WlZaeUR2bEd0Tks0MVc5c3E4azJUbDNLUnpHbFxubDhEOWZ6aFlJSE91WlVzWE5DRkZBRzgxQ1FLQmdRRFkydUkyTFJjaE4xcnJqMnlVQ3VhTkhzdzR4WktyajMvd1xuTUd0dUtYL2ZFT3ZGRzN1SGp5eVVXQ2dKMTJyVEJrZDBMTHM2dTZLaUVvVzFweWZtQ09YSUE5aEVISGZ2NE5UOFxuS05nSkdEa2NMUjBoT3QzV09sbEJobDdHemthK1BFQjN1blJLcUVIZXduTFlSTHlwOWZiYlgyOS84dG1sZCsvMVxuWDdwaWVlQzBjd0tCZ0JRSk14NEJXcXg1OFJBT1RVdjhCNmxRMjh5cUpRUTl5MFB5dmloUERZSXVob3RnakVRYVxuUnJSSGpvbE5WTi9qdGVhbTE1K1FnTDBZeEZxQWNpZXZYNzlmdlNrN3FhNFRhdmFsMHJBMTIvOEZPdGZWR1g1TVxuU2s3OU1lUnJkSWZRVnhOeWtGSnYzZjIwZnphb1drWUFYeHVQUDFsSzlFZmYvaGRXcWovSGN5OFJBb0dBVTVBVVxuOTFkc3dFbjZhU2M5YWRiQmZNUFBZbGtQNmJoRVRxUDU0ZHR3QU44ZVd6ZjU1QVRSNHVBTm5hMDY1dEpXbW5mYVxuTmdtejQ4eExaZkpodDR6YUxyYjhrVkovRFJxUU9CTHRIS1FPN2NLY3BQOXdHR3NubGM1WG16aEZubXpOVXRXd1xubGt3OEJjRGNUR1pBWlNYNDJCUHdmVmR4Y3dmQ21jVW5wMmJicGUwQ2dZRUEyUllxUkVLb1VDZ2tZL1JHQTZMNFxuYlgzOU5hUEQ4akdrZTVCMk1pSVhMdE1KOURyM2hISStDZmlwcXZ5QmR0dDJUMUtoYkhTcDkxalRVUElNQUwzblxuV0dzLy9KMFJ1bFdqY0ZYK3BZWFlEL0hHTjNBZ3Zrdko2UC9URHZ3UzRJNXk4Yk9QWG9QeDJxc1FQQ1U2K3lCalxuOFRleGxZbmR0T3J4RjBFbHpTdkJFYlE9XG4tLS0tLUVORCBQUklWQVRFIEtFWS0tLS0tXG4iLAogICJjbGllbnRfZW1haWwiOiAiZmlyZWJhc2UtYWRtaW5zZGstZmJzdmNAYW50LWNhci1yYWNpbmctc3lzdGVtLmlhbS5nc2VydmljZWFjY291bnQuY29tIiwKICAiY2xpZW50X2lkIjogIjEwOTc0MTQ2ODk3MzE2MTU3NTI5OSIsCiAgImF1dGhfdXJpIjogImh0dHBzOi8vYWNjb3VudHMuZ29vZ2xlLmNvbS9vL29hdXRoMi9hdXRoIiwKICAidG9rZW5fdXJpIjogImh0dHBzOi8vb2F1dGgyLmdvb2dsZWFwaXMuY29tL3Rva2VuIiwKICAiYXV0aF9wcm92aWRlcl94NTA5X2NlcnRfdXJsIjogImh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL29hdXRoMi92MS9jZXJ0cyIsCiAgImNsaWVudF94NTA5X2NlcnRfdXJsIjogImh0dHBzOi8vd3d3Lmdvb2dsZWFwaXMuY29tL3JvYm90L3YxL21ldGFkYXRhL3g1MDkvZmlyZWJhc2UtYWRtaW5zZGstZmJzdmMlNDBhbnQtY2FyLXJhY2luZy1zeXN0ZW0uaWFtLmdzZXJ2aWNlYWNjb3VudC5jb20iLAogICJ1bml2ZXJzZV9kb21haW4iOiAiZ29vZ2xlYXBpcy5jb20iCn0K";

        if (base64 == null || base64.isBlank()) {
            throw new IllegalStateException("FIREBASE_CONFIG_BASE64 is not set");
        }

        byte[] decoded = Base64.getDecoder().decode(base64);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ByteArrayInputStream(decoded)))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}

