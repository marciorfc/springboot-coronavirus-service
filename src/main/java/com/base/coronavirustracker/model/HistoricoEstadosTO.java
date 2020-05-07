package com.base.coronavirustracker.model;

import lombok.Data;

@Data
public class HistoricoEstadosTO {

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


    public enum EstadosTotalEnum {
        STATE("state"),
        TOTAL_CASES("totalCases"),	
        TOTAL_CASES_MS("totalCasesMS"),
        NOT_CONFIRMED_BY_MS("notConfirmedByMS"),
        DEATHS("deaths"),	
        DEATHS_MS("deathsMS"),	
        URL("URL"),	
        DEATHS_PER_100K("deaths_per_100k_inhabitants"),	
        TOTAL_CASES_100K("totalCases_per_100k_inhabitants"),	
        DEATHS_BY_TOTAL_CASES("deaths_by_totalCases"),	
        RECOVERED("recovered");


        private String codigo;

        EstadosTotalEnum(String codigo) {
            this.codigo = codigo;
        }

        public String getCodigo() {
            return codigo;
        }
    }
}