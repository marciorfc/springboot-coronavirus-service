package com.base.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.base.coronavirustracker.model.LocationStats;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String VIRUS_LETAL_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private List<LocationStats> allStats = new ArrayList<LocationStats>();
   
   
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        final List<LocationStats> newStats = new ArrayList<LocationStats>();
        final Map<String, LocationStats> mapa = new HashMap<String, LocationStats>();

        final HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(VIRUS_DATA_URL)).build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        // System.out.println(httpResponse.body());

        StringReader in = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in);

        for (CSVRecord record : records) {
            final LocationStats location = new LocationStats();
            record.get("Province/State");
            location.setState(record.get("Province/State"));
            location.setCountry(record.get("Country/Region"));
            // System.out.println(record.get(record.size() - 1));
            location.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            System.out.println(location);
            newStats.add(location);
            mapa.put(location.getState()+location.getCountry(), location);
        }
        
        in.close();
        
        request = HttpRequest.newBuilder().uri(URI.create(VIRUS_LETAL_DATA_URL)).build();
        httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        // System.out.println(httpResponse.body());

        in = new StringReader(httpResponse.body());
        records = CSVFormat.DEFAULT.withHeader().parse(in);

        for (CSVRecord record : records) {
            LocationStats loc = mapa.get(record.get("Province/State") + record.get("Country/Region"));
            if (loc != null) {
                loc.setLatestTotalDeaths(Integer.parseInt(record.get(record.size() - 1)));
            }
        }
                
        
        this.allStats = newStats;
    }

    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public void setAllStats(final List<LocationStats> allStats) {
        this.allStats = allStats;
    }

    
}