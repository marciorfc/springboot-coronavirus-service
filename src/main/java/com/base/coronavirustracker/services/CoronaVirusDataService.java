package com.base.coronavirustracker.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.base.coronavirustracker.enumeration.CSVColumnEnum;
import com.base.coronavirustracker.enumeration.EstadoEnum;
import com.base.coronavirustracker.model.EstadosTotalTO;
import com.base.coronavirustracker.model.LocationStats;
import com.base.coronavirustracker.util.DateUtils;
import com.base.coronavirustracker.util.HttpConnectionUtil;

import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";
    private static String VIRUS_LETAL_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_deaths_global.csv";

    private static String VIRUS_BRASIL_DATA_URL = "https://raw.githubusercontent.com/wcota/covid19br/master/cases-brazil-total.csv";
    private static String VIRUS_BRASIL_HISTORY_DATA_URL = "https://raw.githubusercontent.com/wcota/covid19br/master/cases-brazil-states.csv";


    private List<LocationStats> allStats = new ArrayList<LocationStats>();
    private List<EstadosTotalTO> allStatesTotal = new ArrayList<EstadosTotalTO>();
    private EstadosTotalTO countryLocalSummary = new EstadosTotalTO();
    private List<EstadosTotalTO> allStatesHistory = new ArrayList<EstadosTotalTO>();

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        fetchVirusAllWorldData();
        fetchVirusAllWorldLethalData();
        fetchBrasilStatesTotalVirusData();
        fetchBrasilStatesHistoricalVirusData();
    }

    public List<EstadosTotalTO> getLocalCountryHistory() {
        return allStatesHistory.stream().filter(estadoTO -> estadoTO.getState().equals("TOTAL") && estadoTO.getDate().compareTo("2020-03-10") > 0).collect(Collectors.toList());
    }

    public List<EstadosTotalTO> getAllStatesHistory() {
        return allStatesHistory;
    }

    public void setAllStatesHistory(List<EstadosTotalTO> allStatesHistory) {
        this.allStatesHistory = allStatesHistory;
    }

    public void fetchVirusAllWorldData() throws IOException, InterruptedException {
        final List<LocationStats> newStats = new ArrayList<LocationStats>();
        final Map<String, LocationStats> mapa = new HashMap<String, LocationStats>();

        Iterable<CSVRecord> records = HttpConnectionUtil.retrieveCSVData(VIRUS_DATA_URL);

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
        this.allStats = newStats;
    }

    

    private void fetchVirusAllWorldLethalData() throws IOException, InterruptedException {
        Iterable<CSVRecord> records = HttpConnectionUtil.retrieveCSVData(VIRUS_LETAL_DATA_URL);

        Map<String, Integer> mapCountryLethalData = new HashMap<String, Integer>();
        for (CSVRecord record : records) {
            mapCountryLethalData.put(
                        record.get("Province/State") + record.get("Country/Region"),
                        Integer.parseInt(record.get(record.size() - 1)));
        }

        allStats.forEach(loc -> {
            loc.setLatestTotalDeaths(mapCountryLethalData.get(loc.getState() + loc.getCountry()));
        });

    }

    public void fetchBrasilStatesTotalVirusData() throws IOException, InterruptedException {
        List<EstadosTotalTO> newStatesTotal = new ArrayList<EstadosTotalTO>();
        Iterable<CSVRecord> records = HttpConnectionUtil.retrieveCSVData(VIRUS_BRASIL_DATA_URL);

        for (CSVRecord record : records) {
            final EstadosTotalTO stateTotal = new EstadosTotalTO();
            
            String sigla = record.get(CSVColumnEnum.STATE.getCodigo());
            stateTotal.setState(EstadoEnum.fromCodigo(sigla) != null ? EstadoEnum.fromCodigo(sigla).getNome() : sigla);
            stateTotal.setSigla(sigla);
            stateTotal.setTotalCases(record.get(CSVColumnEnum.TOTAL_CASES.getCodigo()));	
            stateTotal.setTotalCasesMS(record.get(CSVColumnEnum.TOTAL_CASES_MS.getCodigo()));
            stateTotal.setNotConfirmedByMS(record.get(CSVColumnEnum.NOT_CONFIRMED_BY_MS.getCodigo()));
            stateTotal.setDeaths(record.get(CSVColumnEnum.DEATHS.getCodigo()));
            stateTotal.setDeathsMS(record.get(CSVColumnEnum.DEATHS_MS.getCodigo()));
            stateTotal.setUrl(record.get(CSVColumnEnum.URL.getCodigo()));
            stateTotal.setDeathsPer100kInhabitants(record.get(CSVColumnEnum.DEATHS_PER_100K.getCodigo()));
            stateTotal.setTotalCasesPer100kInhabitants(record.get(CSVColumnEnum.TOTAL_CASES_100K.getCodigo()));
            stateTotal.setDeathsByTotalCases(record.get(CSVColumnEnum.DEATHS_BY_TOTAL_CASES.getCodigo()));
            stateTotal.setRecovered(record.get(CSVColumnEnum.RECOVERED.getCodigo()));

            
            newStatesTotal.add(stateTotal);
           // System.out.println(stateTotal);
           
        }

        //The first element has the Total summary of the country
        this.countryLocalSummary = newStatesTotal.get(0);

        newStatesTotal = newStatesTotal.subList(1, newStatesTotal.size());
        //order by number of cases ms desc
        Comparator<EstadosTotalTO> compareByTotalCasesMS = (EstadosTotalTO state1, EstadosTotalTO state2) -> 
                Integer.valueOf(state2.getTotalCasesMS()) - Integer.valueOf(state1.getTotalCasesMS());
        Comparator<EstadosTotalTO> compareByTotalCasesPer100k = (EstadosTotalTO state1, EstadosTotalTO state2) -> 
                Double.valueOf(state2.getTotalCasesPer100kInhabitants()).intValue() - Double.valueOf(state1.getTotalCasesPer100kInhabitants()).intValue();        
        Collections.sort(newStatesTotal, compareByTotalCasesMS);
           

        this.allStatesTotal = newStatesTotal;

    }


    public void fetchBrasilStatesHistoricalVirusData() throws IOException, InterruptedException {
        List<EstadosTotalTO> newHistoryTotal = new ArrayList<EstadosTotalTO>();
        Iterable<CSVRecord> records = HttpConnectionUtil.retrieveCSVData(VIRUS_BRASIL_HISTORY_DATA_URL);

        for (CSVRecord record : records) {
            final EstadosTotalTO stateTotal = new EstadosTotalTO();
            
            String sigla = record.get(CSVColumnEnum.STATE.getCodigo());
            stateTotal.setSigla(sigla);
            stateTotal.setState(EstadoEnum.fromCodigo(sigla) != null ? EstadoEnum.fromCodigo(sigla).getNome() : sigla);
           
            stateTotal.setDate(record.get(CSVColumnEnum.DATE.getCodigo()));	
            stateTotal.setNewCases(record.get(CSVColumnEnum.NEW_CASES.getCodigo()));
            stateTotal.setTotalCases(record.get(CSVColumnEnum.TOTAL_CASES.getCodigo()));	
            stateTotal.setTotalCasesMS(record.get(CSVColumnEnum.TOTAL_CASES_MS.getCodigo()));
            stateTotal.setDeaths(record.get(CSVColumnEnum.DEATHS.getCodigo()));
            stateTotal.setNewDeaths(record.get(CSVColumnEnum.NEW_DEATHS.getCodigo()));
            stateTotal.setDeathsMS(record.get(CSVColumnEnum.DEATHS_MS.getCodigo()));
            stateTotal.setDeathsPer100kInhabitants(record.get(CSVColumnEnum.DEATHS_PER_100K.getCodigo()));
            stateTotal.setTotalCasesPer100kInhabitants(record.get(CSVColumnEnum.TOTAL_CASES_100K.getCodigo()));
            stateTotal.setDeathsByTotalCases(record.get(CSVColumnEnum.DEATHS_BY_TOTAL_CASES.getCodigo()));
            stateTotal.setRecovered(record.get(CSVColumnEnum.RECOVERED.getCodigo()));

            
            newHistoryTotal.add(stateTotal);
            //System.out.println(stateTotal);
           
        }

        
        this.allStatesHistory = newHistoryTotal;

    }
    
    public List<LocationStats> getAllStats() {
        return allStats;
    }

    public void setAllStats(final List<LocationStats> allStats) {
        this.allStats = allStats;
    }

    public List<EstadosTotalTO> getAllStatesTotal() {
        return allStatesTotal;
    }

    public EstadosTotalTO getCountryLocalSummary() {
        return countryLocalSummary;
    }

    
}