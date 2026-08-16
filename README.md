# PetCare

> Your digital pet diary. Manage the list of your pets, their profiles, and care plans.

## 🐾 About the App
**PetCare** is a versatile application designed specifically for responsible pet owners. With its user-friendly interface, you can easily keep track of your cats, dogs, birds, and other pets. 

**Key Features:**
- Create detailed profiles including names, ages, and breeds.
- View information in a convenient list or grid format.
- Camera integration for quickly adding pet photos.
- Geolocation services to find useful places nearby (e.g., the nearest veterinary clinics).
- Modern, adaptive design developed with accessibility standards in mind to ensure a comfortable experience across all devices.

---

## 📱 Technical Specifications
- **Minimum OS Version:** Android 7.0 (API 24)
- **Target OS Version:** Android 15 (API 35)

---

## 🔒 Permissions

| Permission | Justification for the User |
| :--- | :--- |
| `CAMERA` | Allow camera access so you can take a photo of your pet for their profile. |
| `ACCESS_FINE_LOCATION` | The app uses precise location data to determine the distance to the nearest veterinary clinics. |
| `ACCESS_COARSE_LOCATION` | Used as a fallback to determine an approximate location when precise geolocation is unavailable. |

---

## 🛠 Build and Signing (Android / Google Play)

### 1. Creating a Release Keystore
1. In Android Studio, go to **Build** → **Generate Signed Bundle / APK**.
2. Select **Android App Bundle**.
3. Click **Create new** under *Key store path*.
4. Create a `.jks` file, and set passwords for the keystore and the key.

### 2. Configuring `build.gradle.kts`
Add the following configurations to your `build.gradle.kts` file:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = "your_store_password"
            keyAlias = "your_key_alias"
            keyPassword = "your_key_password"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 3. Generating an AAB
- Run the following command in the terminal: `./gradlew bundleRelease`
- **Or** via the menu: **Build** → **Build Bundle(s) / APK(s)** → **Build Bundle(s)**
- The resulting `.aab` file can be uploaded to the Google Play Console.

---

## 📸 Screenshots

| Main Screen (Pet List) | Add Pet Form | Pet Profile |
| :---: | :---: | :---: |
| ![Main Screen](screenshots/main_screen.jpg) | ![Add Pet Form](screenshots/add_form.jpg) | ![Pet Profile](screenshots/pet_profile.jpg) |

![Main Screen (Pet List)](screenshots/list.png)
![Add Pet Form](screenshots/add_pet.png)
![Pet Profile](screenshots/profile.png)
-->
