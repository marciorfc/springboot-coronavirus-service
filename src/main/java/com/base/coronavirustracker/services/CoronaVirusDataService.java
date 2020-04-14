package com.base.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.base.coronavirustracker.model.LocationStats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<LocationStats>();
   
   
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<LocationStats>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request =  HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();

        HttpResponse<String> httpResponse =  client.send(request, HttpResponse.BodyHandlers.ofString());
        //System.out.println(httpResponse.body());

        StringReader in = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in);
                
        for (CSVRecord record : records) {
            LocationStats location = new LocationStats();
            record.get("Province/State");
            location.setState(record.get("Province/State"));
            location.setCountry(record.get("Country/Region"));
            //System.out.println(record.get(record.size() - 1));
            location.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            System.out.println(location);
            newStats.add(location);
        }
        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public void setAllStats(List<LocationStats> allStats) {
        this.allStats = allStats;
    }

    
}