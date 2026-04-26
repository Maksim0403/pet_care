# Lab 9 - Network Layer Integration - Implementation Summary

## Project: Pet Care App
**Framework:** Jetpack Compose (Android)  
**API:** API Ninjas Animals API (https://api-ninjas.com/api/animals)  
**Date:** 2026-04-20

---

## Overview
Lab 9 focuses on integrating network operations into the Pet Care App, allowing users to view animals from an external API alongside their local pet database. The implementation includes:
- Network layer setup with error handling
- API data models and serialization
- List and detail screens for API data
- Add/Delete operations (demo mode due to read-only API)
- Offline support with caching
- Comprehensive error handling and retry mechanisms

---

## ✅ Task 1 - Network Layer Setup & API Interface

### What Was Done:
1. **Retrofit Configuration** (`ApiClient.kt`)
   - Base URL: `https://api.api-ninjas.com/v1/`
   - API Key interceptor for authentication
   - Logging interceptor for debugging
   - Gson converter for JSON serialization

2. **API Service Interface** (`PetApiService.kt`)
   - `getAllPets()` - GET request to fetch all animals
   - `getAllPets(name: String)` - GET with search query parameter
   - `getPetById(name: String)` - GET to fetch specific animal by name
   - `createPet(request: AnimalApiRequest)` - POST for creating animals
   - `deletePet(petId: String)` - DELETE for removing animals

### Technical Details:
```kotlin
interface PetApiService {
    @GET("animals")
    suspend fun getAllPets(@Query("name") name: String? = null): Response<List<AnimalApiResponse>>
    
    @POST("animals")
    suspend fun createPet(@Body request: AnimalApiRequest): Response<AnimalApiResponse>
    
    @DELETE("animals/{id}")
    suspend fun deletePet(@Path("id") petId: String): Response<Unit>
}
```

### Dependencies Added:
- Retrofit 2.9.0
- OkHttp 4.12.0
- Gson Converter

---

## ✅ Task 2 - Data Models & Serialization

### API Response Model (`AnimalApiResponse.kt`)
**Supports both serialization and deserialization:**

```kotlin
data class AnimalApiResponse(
    @SerializedName("name") val name: String,
    @SerializedName("scientific_name") val scientificName: String? = null,
    @SerializedName("habitat") val habitat: List<String>? = null,
    @SerializedName("diet") val diet: List<String>? = null,
    @SerializedName("lifespan") val lifespan: Double? = null,
    @SerializedName("weight") val weight: Double? = null,
    // ... other fields
)
```

**Deserialization:**
- `toPet()` method converts API response to domain Pet model
- Maps API fields to Dog, Cat, or Parrot subtypes based on name
- Generates summary description from API data

**Serialization:**
- `AnimalApiRequest` class for POST requests
- `fromPet()` companion function converts domain Pet to API request format

### Type Mapping:
- Contains "dog" → `Dog` model
- Contains "cat" → `Cat` model
- Contains "bird/parrot/eagle/owl" → `Parrot` model

---

## ✅ Task 3 - Fetch List & Details from API

### Implementation:

**ApiRepository.kt - Enhanced with Error Handling:**
```kotlin
sealed class ApiResult<T> {
    data class Success<T>(val data: T, val isCached: Boolean = false) : ApiResult<T>()
    data class Error<T>(val exception: Exception, val cachedData: T? = null) : ApiResult<T>()
}
```

**Flow-Based Retrieval:**

1. **getAllPets()** - Fetches list of animals
   - Emits `ApiResult.Success` on successful network call
   - Returns cached data on network error
   - Caches results for offline support

2. **getPetById(identifier: String)** - Fetches single animal
   - Checks in-memory cache first
   - Returns cached result with `isCached = true` flag
   - Falls back to cached data if network unavailable

3. **searchPets(name: String)** - Searches animals by name
   - Uses query parameter for API filtering
   - Caches search results

### Screens:

**ApiPetListScreen** - Tab 3 (API tab)
- Displays list of animals from API
- Search functionality with query parameter
- Loading state with spinner
- Empty state message

**ApiPetProfileScreen** - Detailed view
- Separate route from local pet details
- Shows animal statistics and information
- Retry button on error
- Supports back navigation

---

## ✅ Task 4 - Add & Delete Operations

### Add New Animal (`ApiPetAddScreen` + `ApiPetAddViewModel`)

**Form Fields:**
- Pet Name (required)
- Pet Age (required, in years)
- Pet Weight (required, in kg)
- Pet Type (Dog, Cat, Bird)
- Pet Breed (optional)

**Workflow:**
1. User fills form on dedicated screen (separate route)
2. ViewModel validates fields
3. Creates Pet object (Dog/Cat/Parrot based on selection)
4. Attempts POST request to API
5. Since API Ninjas is read-only, simulates success
6. Shows success state with "Demo" notification
7. Navigates back to list

**Key Features:**
- Form validation
- Loading state during submission
- Error messages with retry
- Demo mode disclaimer banner

### Delete Animal (`ApiPetListScreen` + `ApiPetListViewModel`)

**Implementation:**
- Delete button on each animal card (icon + text)
- Shows loading spinner during operation
- Buttons disabled while deleting
- Removes from list after deletion
- Shows demo message (API doesn't support DELETE)

**Workflow:**
1. User taps delete button
2. ViewModel shows loading spinner on button
3. Attempts DELETE request
4. Receives error (expected - read-only API)
5. Shows demo message notification
6. Still removes from local list for UX
7. List auto-updates

---

## ✅ Task 5 - Error Handling, Retries & Caching

### Error Handling Strategy:

**Network State Manager** (`NetworkState.kt`)
```kotlin
object NetworkState {
    val isOnline: Flow<Boolean> = _isOnline.asStateFlow()
    fun updateNetworkState(context: Context)
}
```

**Three Error Scenarios:**

1. **No Internet Connection**
   - Displays offline banner at top of list
   - Shows cached data from last successful load
   - Indicates data is cached with visual badge
   - Allows retry when connection restored

2. **HTTP Errors (4xx, 5xx)**
   - Shows error screen with description
   - Displays specific HTTP status code
   - Provides "Retry" button
   - Offers cached data fallback

3. **Failed Add/Delete Operations**
   - Shows inline error message
   - For DELETE: Still removes from list (demo behavior)
   - For ADD: Returns to form with error
   - Buttons remain disabled until retry

### Caching Implementation:

**In-Memory Cache:**
```kotlin
class ApiRepository {
    private var cachedPets: List<Pet>? = null
    private var cachedPetByName: Map<String, Pet> = emptyMap()
}
```

**Cache Behavior:**
- Stores last successful GET response
- Returned when network error occurs
- `isCached` flag indicates source
- Separate cache for individual pets vs. list
- Search results also cached

### UI Indicators:

**Offline Banner:**
```
[⚠️] Offline Mode - Showing cached data
```
- Appears at top when no internet
- Colored with error container color
- Automatically dismissed on reconnect

**Cached Data Indicator:**
```
[💾] Showing cached data from last successful load
```
- Shows when displaying cached results
- Different from offline indicator

**Error Screen:**
- Shows error message
- Displays "Retry" button
- Falls back to cached data if available

### Retry Mechanism:

**Automatic Retries:**
- Search: Retries automatically on error
- List load: Shows retry button
- Details: Provides retry callback

**User-Triggered Retries:**
- "Retry" button on error screen
- Clears errors on successful retry
- Updates cached data if successful

---

## Navigation Structure

**Tab Navigation (4 tabs):**
1. **List** - Local pets in list view
2. **Grid** - Local pets in grid view
3. **API** ← NEW - Animals from API
4. **Profile** - User settings

**Routes:**
- `api_pet_list` - Main API list screen
- `api_pet_detail/{petIdentifier}` - Detail screen for API animal
- `api_pet_add` - Form to add new animal

**No changes** to existing Lab 8 routes or screens.

---

## File Structure

```
app/src/main/java/com/petcare/app/api/
├── ApiClient.kt ..................... Network client with Retrofit
├── ApiRepository.kt ................. Repository with caching & error handling
├── PetApiService.kt ................. API service interface
├── AnimalApiResponse.kt ............. Data models & serialization
├── NetworkState.kt .................. NEW: Network state manager
├── ApiPetListViewModel.kt ........... List screen ViewModel
├── ApiPetListScreen.kt .............. List screen with UI
├── ApiPetProfileViewModel.kt ........ Detail screen ViewModel
├── ApiPetProfileScreen.kt ........... Detail screen with UI
├── ApiPetAddViewModel.kt ............ Add screen ViewModel
└── ApiPetAddScreen.kt ............... Add screen form UI
```

---

## Key Improvements Over Basic Implementation

1. **Error Wrapping**
   - API calls wrapped in `ApiResult<T>` sealed class
   - Provides both data and error information
   - Enables graceful degradation

2. **Caching Strategy**
   - In-memory cache for fast access
   - Automatic fallback on network error
   - Visual indication of cached vs. fresh data

3. **User Feedback**
   - Loading spinners during operations
   - Error messages with retry options
   - Offline indicators
   - Demo notices for read-only API

4. **Offline Support**
   - Works without internet connection
   - Shows cached data automatically
   - Retries when online restored
   - Clear visual feedback

5. **Separation of Concerns**
   - API screens completely separate from Lab 8
   - Independent navigation routes
   - Own ViewModels and data flows
   - Doesn't affect existing functionality

---

## Known Limitations (API Ninjas)

✓ **Read-Only API**
- No POST/PUT/DELETE support
- Add operations simulated locally
- Delete operations also simulated
- Demo disclaimers shown

✓ **Search Limitations**
- Only searches by name
- No filtering or advanced queries

✓ **Rate Limiting**
- Free tier limited to 100 requests/day
- Should add rate limit handling

---

## Testing Recommendations

1. **Online Scenarios:**
   - Load list → should fetch from API
   - Search animals → should show results
   - Navigate to detail → should fetch fresh data
   - Add animal → should show demo success

2. **Offline Scenarios:**
   - Disconnect internet
   - Reopen app → shows cached list
   - Try add/delete → should work locally
   - Reconnect → should show refresh option

3. **Error Scenarios:**
   - Simulate network timeout
   - Simulate HTTP 404
   - Verify retry button works
   - Check error message clarity

---

## Summary

**All 5 Lab 9 tasks completed:**

✅ **Task 1** - API interface with 4 endpoints (GET list, GET detail, POST, DELETE)  
✅ **Task 2** - Data models with full serialization/deserialization support  
✅ **Task 3** - Network list and detail screens with Flow-based data loading  
✅ **Task 4** - Add and Delete operations with demo mode for read-only API  
✅ **Task 5** - Comprehensive error handling, retries, caching, and offline support  

**Fully integrated without breaking existing Lab 8 functionality.**

**Status:** Ready for testing and demonstration.

