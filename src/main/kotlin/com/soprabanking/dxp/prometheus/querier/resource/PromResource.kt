package com.soprabanking.dxp.prometheus.querier.resource

import com.soprabanking.dxp.commons.page.Range
import com.soprabanking.dxp.prometheus.querier.client.PrometheusClient
import com.soprabanking.dxp.prometheus.model.api.MeasureDTO
import com.soprabanking.dxp.prometheus.querier.service.MetricService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.core.publisher.zip
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * REST resource publishing [Metrics] endpoints
 */
@RestController
@RequestMapping("api/metrics")
class PromResource(private val client: PrometheusClient, private val service: MetricService) {

    companion object {
        const val URI = "/api/metrics"
    }

    @GetMapping
    fun findAll(range: Range) : Mono<String> {
        val start = OffsetDateTime.now().minus(1, ChronoUnit.MINUTES)
        // TODO input NS or list NS ??
        return service.findServices(start)
                .flatMap { it.toMeasure(start) }
                .sort(compareBy {it.msName })
                .map { "|${it.msName}|${it.cpu}|2|${it.ops}|${it.p50ms}|${it.p90ms}|${it.p99ms}|${it.successPercent}| | |\n" }
                .reduce("||Microservice||vCPU||Instances||Op/s||P50(ms)||P90(ms)||P99(ms)||Success(%)||DB size(Mo)||Comments||\n", String::plus)
                .doOnSuccess { println(it) }
    }

    private fun String.toMeasure(start: OffsetDateTime) =
            listOf(
                    this.findPercentile(50f, start),
                    this.findPercentile(90f, start),
                    this.findPercentile(99f, start),
                    this.findOps(start),
                    this.findCPU(start),
                    this.findSuccess(start)
            ).zip { (p50, p90, p99, ops, cpu, success) -> MeasureDTO(this, cpu, ops, p50, p90, p99, success) }

    private fun String.findPercentile(percent: Float, start: OffsetDateTime) =
            service.findPercentile(this, percent, start, start.plus(20, ChronoUnit.MINUTES), 5)
                    .map { it.data.result }
                    .flatMapIterable { it }
                    .map { it.values }
                    .flatMapIterable { it }
                    .map { it[1] * 1000 } // retrieve duration in milliseconds, 0 is time of measure, rest is empty
                    .collectList()
                    .map { it.average() }

    private fun String.findOps(start: OffsetDateTime) =
            // TODO
            Mono.just(100.0)

    private fun String.findCPU(start: OffsetDateTime) =
            // TODO
            Mono.just(100.0)

    private fun String.findSuccess(start: OffsetDateTime) =
            // TODO
            Mono.just(100.0)

    operator fun <T> List<T>.component6() = this[5]
}