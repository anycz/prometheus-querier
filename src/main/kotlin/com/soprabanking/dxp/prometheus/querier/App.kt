package com.soprabanking.dxp.prometheus.querier

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import com.soprabanking.dxp.commons.constants.DxpConstants.DXP_BASE_PACKAGE

@SpringBootApplication(scanBasePackages = [DXP_BASE_PACKAGE])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}