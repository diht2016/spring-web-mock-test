package ru.sbt.mipt.services.rbc;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RBCService {

    private final RestTemplate restTemplate;

    public RBCService() {
        this.restTemplate = new RestTemplate();
    }

    public RBCService(RestTemplate service) {
        this.restTemplate = service;
    }

    public String getUSDData() {
        String url = "http://export.rbc.ru/free/selt.0/free.fcgi?" +
                "period=DAILY&tickers=USD000000TOD&lastdays=30&separator=TAB&data_format=BROWSER";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        String body = response.getBody().trim();

        addToDB(body);

        return Double.toString(computeMaxIndex(body));
    }

    private void addToDB(String text) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            String[] parts = line.split("\\s");
            String day = parts[1];
            double value = Double.parseDouble(parts[parts.length - 1]);
        }
    }

    public double computeMaxIndex(String text) {
        String[] lines = text.split("\n");
        double max = Double.NEGATIVE_INFINITY;
        for (String line : lines) {
            String[] parts = line.split("\\s");
            double n = Double.parseDouble(parts[parts.length - 1]);
            if (max < n) {
                max = n;
            }
        }
        return max;
    }
}
