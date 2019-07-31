package com.soprabanking.dxp.prometheus.querier.client

import com.soprabanking.dxp.commons.client.config.RetrofitClient
import com.soprabanking.dxp.prometheus.client.PrometheusURLConstants
import com.soprabanking.dxp.prometheus.model.client.prometheus.Response
import reactor.core.publisher.Mono
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.OffsetDateTime

@RetrofitClient(url="https://prometheus.perfs.dxp.delivery", value="")
interface PrometheusClient {

    @GET(PrometheusURLConstants.QUERY)
    fun query( @Query("query") query: String, @Query("time") time: OffsetDateTime): Mono<Response>

    @GET(PrometheusURLConstants.QUERY_RANGE)
    fun queryRange(
            @Query("query") query: String, @Query("start") start: OffsetDateTime, @Query("end") end: OffsetDateTime, @Query("step") step: Int
    ): Mono<Response>
}