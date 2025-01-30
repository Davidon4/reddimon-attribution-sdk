# Reddimon Attribution SDK for Android

The Reddimon Attribution SDK helps track app installations, subscriptions, and user activations across different Android development platforms including Native Android (Kotlin/Java), React Native (CLI and Expo).

## Platform Support & Installation

### 1. Native Android (Kotlin/Java)

#### Gradle Setup

Add to your app's `build.gradle`:

gradle
dependencies {
implementation 'com.reddimon:attribution-sdk:1.0.0'
}

#### Basic Implementation

kotlin
// Initialize SDK
val config = AttributionConfig(
publisherId = "your_publisher_id",
appId = "your_app_id",
baseUrl = "https://api.yourservice.com",
apiKey = "your_api_key",
enableDebugLogs = BuildConfig.DEBUG
)
// Initialize in Application class or MainActivity
AttributionSDK.initialize(context, config)
// Optional: Set tracking callback
AttributionSDK.getInstance().setTrackingCallback(object : AttributionSDK.TrackingCallback {
override fun onSuccess(eventType: String, data: JSONObject) {
Log.d("Attribution", "Successfully tracked $eventType")
}
override fun onError(eventType: String, error: String) {
Log.e("Attribution", "Failed to track $eventType: $error")
}
})

### 2. React Native CLI

#### Installation

bash
npm install @reddimon/react-native-attribution
or
yarn add @reddimon/react-native-attribution

#### Usage

typescript
import Attribution from '@reddimon/react-native-attribution';

// Initialize
await Attribution.initialize({
publisherId: 'your_publisher_id',
appId: 'your_app_id',
baseUrl: 'https://api.yourservice.com',
apiKey: 'your_api_key',
enableDebugLogs: DEV
});

// Track events
try {
await Attribution.trackEvent('installation', {
referrerUrl: 'utm_source=google-play&utm_medium=organic',
installTime: Date.now(),
campaignId: 'summer_campaign'
});
} catch (error) {
console.error('Attribution tracking failed:', error);
}

### 3. React Native Expo

#### Installation

bash
expo install @reddimon/expo-attribution

#### Configuration

Add to your `app.config.js` or `app.json`:

json
{
"expo": {
"plugins": [
"@reddimon/expo-attribution"
]
}
}

#### Usage

Same as React Native CLI usage.

## Tracking Events

### 1. Installation Tracking

kotlin
// Kotlin
AttributionSDK.getInstance().trackEvent(
AttributionEvent.Installation(
referrerUrl = "utm_source=google-play&utm_medium=organic",
installTime = System.currentTimeMillis(),
clickTime = null,
campaignId = "summer_campaign"
)
)

// React Native
await Attribution.trackEvent('installation', {
referrerUrl: 'utm_source=google-play&utm_medium=organic',
installTime: Date.now(),
campaignId: 'summer_campaign'
});

### 2. Subscription Tracking

kotlin
// Kotlin
AttributionSDK.getInstance().trackEvent(
AttributionEvent.Subscription(
subscriptionId = "premium_monthly",
subscriptionType = "monthly",
price = 9.99,
currency = "USD",
startDate = System.currentTimeMillis(),
endDate = null,
trialPeriod = true
)
)

// React Native
await Attribution.trackEvent('subscription', {
subscriptionId: 'premium_monthly',
subscriptionType: 'monthly',
price: 9.99,
currency: 'USD',
startDate: Date.now(),
trialPeriod: true
});

### 3. Activation Tracking

kotlin
// Kotlin
AttributionSDK.getInstance().trackEvent(
AttributionEvent.Activation(
isFirstLaunch = false,
sessionId = UUID.randomUUID().toString(),
customData = mapOf(
"source" to "deep_link",
"campaign" to "winter_sale",
"medium" to "notification"
)
)
)

// React Native
await Attribution.trackEvent('activation', {
isFirstLaunch: false,
customData: {
source: 'deep_link',
campaign: 'winter_sale',
medium: 'notification'
}
});

## Configuration Options

### AttributionConfig Parameters

| Parameter                | Type    | Required | Default | Description                     |
| ------------------------ | ------- | -------- | ------- | ------------------------------- |
| publisherId              | String  | Yes      | -       | Your publisher identifier       |
| appId                    | String  | Yes      | -       | Your application identifier     |
| baseUrl                  | String  | Yes      | -       | API endpoint URL                |
| apiKey                   | String  | Yes      | -       | Authentication key              |
| enableDebugLogs          | Boolean | No       | false   | Enable detailed logging         |
| connectionTimeoutSeconds | Long    | No       | 30      | Network timeout in seconds      |
| readTimeoutSeconds       | Long    | No       | 30      | Network read timeout in seconds |
| retryCount               | Int     | No       | 3       | Number of retry attempts        |

## Error Handling

### Native Android

kotlin
AttributionSDK.getInstance().setTrackingCallback(object : AttributionSDK.TrackingCallback {
override fun onSuccess(eventType: String, data: JSONObject) {
when (eventType) {
"installation" -> handleSuccessfulInstallation(data)
"subscription" -> handleSuccessfulSubscription(data)
"activation" -> handleSuccessfulActivation(data)
}
}
override fun onError(eventType: String, error: String) {
Log.e("Attribution", "Failed to track $eventType: $error")
}
})

### React Native

typescript
try {
await Attribution.trackEvent('installation', eventData);
} catch (error) {
console.error('Attribution tracking failed:', error);
}

## Best Practices

1. **Initialization**

   - Initialize SDK as early as possible (Application class recommended)
   - Keep API keys secure using BuildConfig or environment variables

2. **Error Handling**

   - Always implement error handling for tracking calls
   - Log errors appropriately for debugging

3. **Performance**

   - Use appropriate timeout values for your network conditions
   - Enable debug logs only in development builds

4. **Security**

   - Store sensitive keys securely
   - Use HTTPS for API endpoints
   - Validate all user inputs

5. **Data Collection**
   - Include relevant custom data for better attribution
   - Follow data privacy regulations (GDPR, CCPA)

## ProGuard Configuration

Add to your `proguard-rules.pro`:
proguard

Keep SDK classes
-keep class com.reddimon.attribution. { ; }
OkHttp rules
-dontwarn okhttp3.
-dontwarn okio.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
The improved documentation now includes:
Clear platform-specific installation and usage instructions
Detailed configuration options with a table
Comprehensive error handling examples
Platform-specific code examples
Troubleshooting section
Best practices with detailed explanations
Better formatting and organization
Would you like me to expand on any particular section?

## Troubleshooting

Common issues and solutions:

1. **Installation Tracking Not Working**

   - Verify INSTALL_REFERRER permission is added
   - Check Google Play Store is updated
   - Verify network connectivity

2. **Network Timeouts**

   - Adjust timeout configuration
   - Check network quality
   - Verify API endpoint is correct

3. **React Native Integration Issues**
   - Verify native module is linked properly
   - Check JavaScript bundle is updated
   - Verify TypeScript types are imported correctly

## Support

For issues and feature requests:

- Email: support@reddimon.com
- Documentation: https://docs.reddimon.com
- GitHub Issues: https://github.com/Davidon4/reddimon-attribution-sdk/issues

## License

This SDK is available under the MIT License. See the LICENSE file for more info.
