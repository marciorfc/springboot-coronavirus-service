package com.base.coronavirustracker.model;

import lombok.Data;


@Data
public class LocationStats {

    private String state;
    private String country;
    private Integer latestTotalCases;
}