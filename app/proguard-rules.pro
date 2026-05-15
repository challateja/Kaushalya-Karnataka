# Firebase Firestore needs to keep model classes for deserialization
-keep class com.example.kaushalya_karnataka.models.** { *; }

# Keep members with Firestore annotations
-keepattributes *Annotation*
-keepclassmembers class * {
    @com.google.firebase.firestore.* <fields>;
    @com.google.firebase.firestore.* <methods>;
}

# Credentials Manager and Google ID rules
-keep class com.google.android.libraries.identity.googleid.** { *; }
-keep class androidx.credentials.** { *; }
