package com.base.coronavirustracker.enumeration;

public enum CSVColumnEnum {
    DATE("date"),
    STATE("state"),
    TOTAL_CASES("totalCases"),
    NEW_CASES("newCases"),	
    TOTAL_CASES_MS("totalCasesMS"),
    NOT_CONFIRMED_BY_MS("notConfirmedByMS"),
    DEATHS("deaths"),	
    NEW_DEATHS("newDeaths"),
    DEATHS_MS("deathsMS"),	
    URL("URL"),	
    DEATHS_PER_100K("deaths_per_100k_inhabitants"),	
    TOTAL_CASES_100K("totalCases_per_100k_inhabitants"),	
    DEATHS_BY_TOTAL_CASES("deaths_by_totalCases"),	
    RECOVERED("recovered");


    private String codigo;

    CSVColumnEnum(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }    
    
}