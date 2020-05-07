package com.base.coronavirustracker.model;

import lombok.Data;

@Data
public class EstadosTotalTO {

    private String date;
    private String state;
    private String newCases;
    private String totalCases;	
    private String totalCasesMS;
    private String notConfirmedByMS;
    private String deaths;	
    private String newDeaths;
    private String deathsMS;	
    private String url;	
    private String deathsPer100kInhabitants;	
    private String totalCasesPer100kInhabitants;	
    private String deathsByTotalCases;	
    private String recovered;


    
}