package com.soprabanking.dxp.prometheus.model.api

data class MeasureDTO(
        val msName: String,
        val cpu: Double,
        val ops: Double,
        val p50ms: Double,
        val p90ms: Double,
        val p99ms: Double,
        val successPercent: Double
)
