package com.soprabanking.dxp.prometheus.querier.service

import com.soprabanking.dxp.prometheus.querier.client.PrometheusClient
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.OffsetDateTime

@Service
class MetricService(private val client: PrometheusClient) {

    fun findServices(
            time: OffsetDateTime,
            namespace: String = "psd2"
    ): Flux<String> = client.query(
            "(sum(istio_requests_total{destination_workload_namespace=~\"$namespace\"}) by (destination_workload) or sum(istio_requests_total{source_workload_namespace=~\"$namespace\"}) by (source_workload)) or (sum(istio_tcp_sent_bytes_total{destination_workload_namespace=~\"$namespace\"}) by (destination_workload) or sum(istio_tcp_sent_bytes_total{source_workload_namespace=~\"$namespace\"}) by (source_workload))",
            time
    )
            .flatMapIterable { it.data.result }
            .filter { it.metric["destination_workload"] != null }
            .map { it.metric["destination_workload"] }

    /**
     * @param microservice deployment name
     * @param namespace where we want the measures
     * @param start UTC start time
     * @param end UTC end time
     * @param step seconds period between each measures between start & end
     */
    fun findPercentile(
            microservice: String,
            percent: Float,
            start: OffsetDateTime,
            end: OffsetDateTime,
            step: Int,
            namespace: String = "psd2"
    ) = client.queryRange(
            "histogram_quantile(${percent / 100}, sum(irate(istio_request_duration_seconds_bucket{reporter=\"destination\",destination_workload=~\"$microservice\", destination_workload_namespace=~\"$namespace\"}[1m])) by (le))",
            start, end, step
    )
}