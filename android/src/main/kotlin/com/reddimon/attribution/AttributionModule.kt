/ AttributionModule.kt
package com.reddimon.attribution

import com.facebook.react.bridge.*
import com.reddimon.attribution.models.AttributionEvent
import org.json.JSONObject
import java.util.*

class AttributionModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {
    
    override fun getName() = "AttributionModule"
    
    @ReactMethod
    fun initialize(config: ReadableMap, promise: Promise) {
        try {
            val attributionConfig = AttributionConfig(
                publisherId = config.getString("publisherId") ?: throw IllegalArgumentException("publisherId is required"),
                appId = config.getString("appId") ?: throw IllegalArgumentException("appId is required"),
                baseUrl = config.getString("baseUrl") ?: throw IllegalArgumentException("baseUrl is required"),
                apiKey = config.getString("apiKey") ?: throw IllegalArgumentException("apiKey is required"),
                enableDebugLogs = config.getBoolean("enableDebugLogs"),
                connectionTimeoutSeconds = config.getDouble("connectionTimeoutSeconds").toLong(),
                readTimeoutSeconds = config.getDouble("readTimeoutSeconds").toLong(),
                retryCount = config.getInt("retryCount")
            )
            
            AttributionSDK.initialize(reactApplicationContext, attributionConfig)
            promise.resolve(null)
        } catch (e: Exception) {
            promise.reject("INIT_ERROR", e)
        }
    }

    @ReactMethod
    fun trackEvent(eventType: String, eventData: ReadableMap, promise: Promise) {
        try {
            val event = when (eventType) {
                "installation" -> createInstallationEvent(eventData)
                "subscription" -> createSubscriptionEvent(eventData)
                "activation" -> createActivationEvent(eventData)
                else -> throw IllegalArgumentException("Unknown event type: $eventType")
            }
            
            AttributionSDK.getInstance().trackEvent(event)
            promise.resolve(null)
        } catch (e: Exception) {
            promise.reject("TRACK_ERROR", e)
        }
    }

    private fun createInstallationEvent(data: ReadableMap): AttributionEvent.Installation {
        return AttributionEvent.Installation(
            referrerUrl = data.getString("referrerUrl"),
            installTime = data.getDouble("installTime").toLong(),
            clickTime = if (data.hasKey("clickTime")) data.getDouble("clickTime").toLong() else null,
            campaignId = data.getString("campaignId")
        )
    }

    private fun createSubscriptionEvent(data: ReadableMap): AttributionEvent.Subscription {
        return AttributionEvent.Subscription(
            subscriptionId = data.getString("subscriptionId") ?: throw IllegalArgumentException("subscriptionId is required"),
            subscriptionType = data.getString("subscriptionType") ?: throw IllegalArgumentException("subscriptionType is required"),
            price = data.getDouble("price"),
            currency = data.getString("currency") ?: throw IllegalArgumentException("currency is required"),
            startDate = data.getDouble("startDate").toLong(),
            endDate = if (data.hasKey("endDate")) data.getDouble("endDate").toLong() else null,
            trialPeriod = if (data.hasKey("trialPeriod")) data.getBoolean("trialPeriod") else false
        )
    }

    private fun createActivationEvent(data: ReadableMap): AttributionEvent.Activation {
        val customData = if (data.hasKey("customData")) {
            convertMapToCustomData(data.getMap("customData")!!)
        } else null

        return AttributionEvent.Activation(
            isFirstLaunch = data.getBoolean("isFirstLaunch"),
            sessionId = data.getString("sessionId") ?: UUID.randomUUID().toString(),
            customData = customData
        )
    }

    private fun convertMapToCustomData(readableMap: ReadableMap): Map<String, Any> {
        val result = mutableMapOf<String, Any>()
        val iterator = readableMap.keySetIterator()
        while (iterator.hasNextKey()) {
            val key = iterator.nextKey()
            when (readableMap.getType(key)) {
                ReadableType.Boolean -> result[key] = readableMap.getBoolean(key)
                ReadableType.Number -> result[key] = readableMap.getDouble(key)
                ReadableType.String -> result[key] = readableMap.getString(key)!!
                else -> {} // Ignore other types
            }
        }
        return result
    }
}
