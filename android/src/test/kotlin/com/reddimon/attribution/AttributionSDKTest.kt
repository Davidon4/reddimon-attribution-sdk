package com.reddimon.attribution

import android.content.Context
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import com.reddimon.attribution.models.AttributionEvent
import org.junit.Assert.*

class AttributionSDKTest {
    @Mock
    private lateinit var mockContext: Context

    private lateinit var sdk: AttributionSDK

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        
        val config = AttributionConfig(
            publisherId = "test_publisher",
            appId = "test_app",
            baseUrl = "https://test-api.reddimon.com",
            apiKey = "test_key",
            enableDebugLogs = true
        )
        
        sdk = AttributionSDK.initialize(mockContext, config)
    }

    @Test
    fun `test SDK initialization`() {
        assertNotNull(AttributionSDK.getInstance())
    }

    @Test
    fun `test event tracking`() {
        var successCalled = false
        
        sdk.setTrackingCallback(object : AttributionSDK.TrackingCallback {
            override fun onSuccess(eventType: String, data: JSONObject) {
                successCalled = true
            }

            override fun onError(eventType: String, error: String) {
                fail("Should not call onError")
            }
        })

        sdk.trackEvent(AttributionEvent.Installation(
            referrerUrl = "test_referrer",
            installTime = System.currentTimeMillis(),
            clickTime = null,
            campaignId = "test_campaign"
        ))

        // Allow for async operation
        Thread.sleep(1000)
        assertTrue(successCalled)
    }
} 