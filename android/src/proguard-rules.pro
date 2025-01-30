# Keep SDK classes
-keep class com.yourcompany.attribution.** { *; }

# OkHttp rules
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Keep model classes
-keep class com.reddimon.attribution.models.** { *; }

# Keep callback interfaces
-keep interface com.reddimon.attribution.AttributionSDK$TrackingCallback { *; }