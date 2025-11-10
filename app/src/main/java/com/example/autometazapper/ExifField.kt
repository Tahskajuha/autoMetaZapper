package com.example.autometazapper.ui

// Enum class to list EXIF metadata fields you want to remove or toggle
enum class ExifField(val displayName: String) {
    LOCATION("Location"),
    CAMERA_MAKE("Camera Make"),
    CAMERA_MODEL("Camera Model"),
    DATE_TIME("Date & Time"),
    FLASH("Flash"),
    FOCAL_LENGTH("Focal Length")
}
