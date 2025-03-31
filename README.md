# Art Gallery App

A mobile application built with Android and Retrofit to explore and curate art collections from the MET and Artic APIs, allowing users to browse artworks, create personal galleries, and view full-size images.

## Features
- **Search Artworks**: Browse artworks from the Metropolitan Museum of Art (MET) and Art Institute of Chicago (Artic) by keyword (e.g., "cats").
- **Pagination**: Navigate through search results with a "Next Page" button.
- **Personal Galleries**: Create custom galleries and add artworks to them.
- **Full Image View**: Tap any artwork to view it in full size.
- **Responsive UI**: Grid layout for artwork previews with a bottom navigation bar for easy access to Browse and Galleries sections.

## Tech Stack
- **Frontend**: Android (Java), RecyclerView, Picasso for image loading
- **Backend**: Spring Boot REST API (running locally at `http://localhost:8080`)
- **Networking**: Retrofit with Gson for API calls
- **APIs**: MET and Artic public art APIs

## Prerequisites
- Android Studio (latest version recommended)
- Java 8 or higher
- A running instance of the Spring Boot backend (see Backend Setup)

## Setup Instructions

### Backend
1. Clone or set up the backend Spring Boot project (not included in this repo).
2. Run the backend locally on `http://localhost:8080`.
    - The Android app connects to `10.0.2.2:8080` (Android emulator’s alias for `localhost`).
3. Verify endpoints like `/api/artworks/galleries` are accessible via:
   ```bash
   curl http://localhost:8080/api/artworks/galleries
   ```

### Android App
#### Clone this repository:
```bash
git clone https://github.com/filip-byte/artgalleryapp.git
```

#### Open the project in Android Studio:
```
File > Open > Select artgalleryapp folder
```

#### Sync Gradle and build the project:
Click "Sync Project with Gradle Files" or run:
```bash
./gradlew build
```

#### Run the app on an emulator or device:
Select an emulator (e.g., Pixel 4 API 30) and click "Run" (or Run > Run 'app').

**Note:** Use `10.0.2.2:8080` in `ApiClient.java` for emulator-to-localhost communication.

---

## Configuration Details

### Android Manifest
**Location:** `app/src/main/AndroidManifest.xml`
#### Key Permissions:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```
Required for network requests to the MET/Artic APIs and local backend.

### Network Security Config
**Location:** `app/src/main/res/xml/network_security_config.xml`
#### Purpose: Allows cleartext HTTP traffic to `10.0.2.2:8080` for local development.
```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```
#### Linked in `AndroidManifest.xml`:
```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

---

## Build Gradle Dependencies
**Location:** `app/build.gradle`
#### Key Dependencies:
```gradle
dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.picasso:picasso:2.71828'
}
```
- **AppCompat & Material**: Core Android UI components.
- **RecyclerView**: For artwork grid display.
- **Retrofit & Gson**: For API calls and JSON parsing.
- **Picasso**: For image loading and caching.

---

## Connecting to Local Backend
The app uses `10.0.2.2` to connect to `localhost` on the host machine from an Android emulator.
#### Defined in `ApiClient.java`:
```java
private static final String BASE_URL = "http://10.0.2.2:8080/";
```
**Why `10.0.2.2`?** Android emulators treat `10.0.2.2` as the host machine’s `localhost`. If running on a physical device, replace with your machine’s IP (e.g., `192.168.1.x:8080`).

---

## Usage

### **Browse**
- Select MET or Artic, enter a keyword (e.g., "cats"), and hit Search.
- Tap an image to view it full-size or use the "+" button to add it to a gallery.

### **Galleries**
- View your saved galleries, tap one to see its artworks, and click an image for a full view.

### **Navigation**
- Use the bottom bar to switch between Browse and Galleries.

