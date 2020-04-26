package com.xonshiz.covid_19tracker.models

data class Statewise(
    val active: String,
    var confirmed: String,
    var deaths: String,
    val deltaconfirmed: String,
    val deltadeaths: String,
    val deltarecovered: String,
    val lastupdatedtime: String,
    var recovered: String,
    var state: String,
    val statecode: String
)