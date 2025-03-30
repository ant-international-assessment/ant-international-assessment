package com.ant.assessment.simulation.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() throws Exception {
        String json = """
                {
                   "type": "service_account",
                   "project_id": "ant-car-racing-system",
                   "private_key_id": "fedce4705f91cfb5d8962197c1f507a7843f7f48",
                   "private_key": "-----BEGIN PRIVATE KEY-----\\nMIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC9ywWY3vcl/51A\\npuh265iZQXDPJbJ9XFG4OAD3tKtl7i0e8nwA88IjmZDnqgxn7k9ayD3lAr8lxGBw\\n7BfwixVtliao+1vIl+x2Lmz5j8Esh1RDPj7nB6sGRikBN1ybWV2ycT5L0dOls8tm\\n09x1q7mSTQGEtVpQWTuo4Vit9x6a4MqwyZ9zXDU2NlXdzc36Wan6QbJ98IGPrAze\\nAFPvO3LJ2jRrZthvW+Sk1glJ6hANmzqCDi3NUCOz3nrWCNyUGZ0o9E+UIFWdSIjO\\npEjy/ajC7J0040GSKMyDAiHZZws14HbSJFkSIpWn6yY4sPVYREC8NYjXOtbvns5W\\neCZqgrvbAgMBAAECggEAXpjHhGd1dhoOrf/DFCrfV1u3t3fJnBQcox4+1TfWyipJ\\nkQg5aFwWopnTELTAFDlCkjQGLZQN4+waSY5TbFBWcNWLFWSbXcLjWBBRvp25IdGd\\n3Ro0TgCTbMAhhfqrDL8hJWSuMSo3PHr94ZWtZ3UdPF9nZ1bg2fVv9//BrIvkVDrk\\nubIAHUVFnJS5bDIfL+AcO7+6DCXsz1Eyn/jDvaFDkwKjDpa9fAzKN8a1U0jk2qyW\\nk59IIZEjD2D8wcQobxe5div32bJOCiFgrXsSrZcUWVzfh8xnGbuqLj3RMjrxBaMe\\niytvgoisM/zL9/nzr+1P6XcQmKvIgDv2Kz8M5MhIWQKBgQDmYejEjw8yci5MR8uh\\nwSnDlT4/mojt4yckldCc+WvlmfQFxDglzTdPgAWUPQfgEpd1QXGusxipV27sfice\\nuoSaTgJFAYysZIGSL4kpFOlVrrEi9WdzeTv4FVukbMLucdX2TDUBBrk/mRPskh8U\\nfSgsS0Jif9fGhv096b2Vvym5VQKBgQDS5bDCkjApybnyvckXhomMUM9FTvoxvTpW\\nZHOuOf5oe9DZWhcReODUFlTzdp7VgRLC0DpYnYyfn2NWPTp21/FgEimdP0ZRg1Qe\\nl42Ln3ZsI4NUR5Z1v49jaOSqlOI4PP2LvGEyTRnUm1pLNSbFVzXxk1bedhL/oxmw\\nR/KHbD3gbwKBgBEKzR09N/KTX7lk1jtw/pXpRGGQHPD8DLk4G+aAzOp/KP4ENeMV\\n+zF2VvNQUHTS60FRRgeWWAyu2xXDf0FWqth/TMuuAan/BVa7DXpsgZZFBfWAEdcI\\n8Na5Pb48DW92HE3L9V/OD4YU3NRzOrlZSM9+Absnmd6lvbYGxG9/ZKbBAoGAURv/\\niEVa+qdXuZDeJwsaGXn2zUwbk0eYiSNnDinFu39rCjzIxHJ93XOdRmlnmutiQMR+\\nvBgTvkw5kzBHLO/OKEmz8YMlUzI0BvP4fytcCyj5WE+UAUMkLXJpB2XtPv3N8ZEI\\nvKTEe20oI+aUO69X0sn8zeARXHno5lA1aXnqrkcCgYB3BdwFUpU4fv1Wkl09FNQp\\nZE5z78s6k9l2Jz9pQrODREFdmAd+cpmxqfcRUQbFf37AKYwFflMesWuLxI/zBjvG\\nOzHmQPZIEybehnfPgz1Kziv9I3XSc3ifkzV/V5JxNJJ5xG3agSF9FUBNvCtTDALo\\nu85tWMBfB2DXydsN639wXA==\\n-----END PRIVATE KEY-----\\n",
                   "client_email": "firebase-adminsdk-fbsvc@ant-car-racing-system.iam.gserviceaccount.com",
                   "client_id": "109741468973161575299",
                   "auth_uri": "https://accounts.google.com/o/oauth2/auth",
                   "token_uri": "https://oauth2.googleapis.com/token",
                   "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
                   "client_x509_cert_url": "https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40ant-car-racing-system.iam.gserviceaccount.com",
                   "universe_domain": "googleapis.com"
                 }
        """;

        InputStream serviceAccount = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
    }
}
