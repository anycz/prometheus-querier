package com.soprabanking.dxp.prometheus.model.client.prometheus

/**
 * When ResultType = vector, value is valued. When ResultType = matrix, values is valued instead.
 */
data class Result(val values: List<List<String>>?, val value: List<Float>?, val metric: Map<String, String>)
