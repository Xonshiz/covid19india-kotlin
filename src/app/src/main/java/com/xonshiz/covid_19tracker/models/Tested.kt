package com.xonshiz.covid_19tracker.models

data class Tested(
    val _cyevm: String,
    val positivecasesfromsamplesreported: String,
    val samplereportedtoday: String,
    val source: String,
    val testsconductedbyprivatelabs: String,
    val totalindividualstested: String,
    val totalpositivecases: String,
    val totalsamplestested: String,
    val updatetimestamp: String
)