package com.reddimon.attribution.models

sealed class AttributionEvent {
    data class Installation(
        val referrerUrl: String?,
        val installTime: Long,
        val clickTime: Long?,
        val campaignId: String?
    ) : AttributionEvent()

    data class Subscription(
        val subscriptionId: String,
        val subscriptionType: String,
        val price: Double,
        val currency: String,
        val startDate: Long,
        val endDate: Long?,
        val trialPeriod: Boolean = false
    ) : AttributionEvent()

    data class Activation(
        val isFirstLaunch: Boolean,
        val sessionId: String,
        val customData: Map<String, Any>?
    ) : AttributionEvent()
} 