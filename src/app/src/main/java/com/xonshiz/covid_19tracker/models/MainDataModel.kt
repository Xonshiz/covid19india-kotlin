package com.xonshiz.covid_19tracker.models

data class MainDataModel(
    val cases_time_series: List<CasesTimeSery>,
    val statewise: List<Statewise>,
    val tested: List<Tested>
)