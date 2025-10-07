package com.uitopic.restockmobile.features.monitoring.data.datasource

import com.uitopic.restockmobile.features.monitoring.domain.models.SalesDish
import com.uitopic.restockmobile.features.monitoring.domain.models.SalesSupply

class MonitoringLocalDataSource {

    fun getDishes(): List<SalesDish> = listOf(
        SalesDish(
            id = "dish_lomo_saltado",
            name = "Lomo Saltado",
            unitPrice = 20.50
        ),
        SalesDish(
            id = "dish_arroz_con_pollo",
            name = "Arroz con Pollo",
            unitPrice = 21.50
        ),
        SalesDish(
            id = "dish_sopa_dieta",
            name = "Sopa dieta",
            unitPrice = 15.50
        ),
        SalesDish(
            id = "dish_causa_limeña",
            name = "Causa limeña",
            unitPrice = 13.20
        ),
        SalesDish(
            id = "dish_tallarin_saltado",
            name = "Tallarin saltado",
            unitPrice = 18.40
        )
    )

    fun getSupplies(): List<SalesSupply> = listOf(
        SalesSupply(
            id = "supply_huevo",
            name = "Huevo",
            measurementHint = "unid."
        ),
        SalesSupply(
            id = "supply_arroz",
            name = "Arroz",
            measurementHint = "gramos"
        ),
        SalesSupply(
            id = "supply_inka_kola",
            name = "Inka cola personal",
            measurementHint = "botellas"
        ),
        SalesSupply(
            id = "supply_aji_amarillo",
            name = "Ají amarillo",
            measurementHint = "gramos"
        ),
        SalesSupply(
            id = "supply_quinua",
            name = "Quinua",
            measurementHint = "gramos"
        )
    )
}

