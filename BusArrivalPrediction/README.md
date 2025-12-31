# Bus Arrival Prediction System

## Project Information

**Student Name:** Nasra Amjad Albalushi  
**Student ID:** 21F21654  
**Session:** B  
**Course:** COMP 30040 - Mobile Application Development  
**Institution:** Middle East College

## Project Overview

The Bus Arrival Prediction System is a mobile application designed to provide real-time bus arrival predictions to passengers. The application helps reduce waiting time and uncertainty for public transport users by displaying expected arrival times based on schedules and prediction algorithms.

## Features

### User Features
- **User Authentication**: Secure login and registration system
- **Bus Route Selection**: Browse and select from available bus routes
- **Bus Stop Selection**: Choose specific bus stops along selected routes
- **Arrival Predictions**: View predicted arrival times for buses
- **Real-time Updates**: Dynamic data fetched from Firebase backend

### Admin Features
- **Route Management**: Add, edit, and delete bus routes
- **Stop Management**: Manage bus stops with location data
- **Schedule Management**: Configure bus schedules and frequencies
- **User Management**: View registered users

## Technical Stack

### Platform
- **Android**: Native Android application
- **Language**: Java
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

### Architecture
- **Pattern**: MVVM (Model-View-ViewModel)
- **UI**: Material Design Components
- **Backend**: Firebase Realtime Database
- **Authentication**: Firebase Authentication

### Key Libraries
- Firebase Authentication (Email/Password)
- Firebase Realtime Database
- Material Design Components
- RecyclerView for list displays
- Retrofit for HTTP requests (API integration ready)

## Project Structure

```
BusArrivalPrediction/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/busarrival/app/
│   │   │   │   ├── activities/
│   │   │   │   │   ├── SplashActivity.java
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── RegisterActivity.java
│   │   │   │   │   ├── MainActivity.java
│   │   │   │   │   └── AdminActivity.java
│   │   │   │   ├── adapters/
│   │   │   │   │   └── BusArrivalAdapter.java
│   │   │   │   ├── models/
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── BusRoute.java
│   │   │   │   │   ├── BusStop.java
│   │   │   │   │   └── BusSchedule.java
│   │   │   │   └── utils/
│   │   │   │       ├── FirebaseHelper.java
│   │   │   │       └── ValidationHelper.java
│   │   │   ├── res/
│   │   │   │   ├── layout/ (All XML layouts)
│   │   │   │   ├── values/ (Strings, Colors, Styles)
│   │   │   │   └── drawable/
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   └── google-services.json
└── build.gradle
```

## Database Schema

### Users Collection
```json
{
  "userId": "string",
  "name": "string",
  "email": "string",
  "phone": "string",
  "role": "user|admin",
  "createdAt": "timestamp"
}
```

### Bus Routes Collection
```json
{
  "routeId": "string",
  "routeNumber": "string",
  "routeName": "string",
  "startPoint": "string",
  "endPoint": "string",
  "active": "boolean"
}
```

### Bus Stops Collection
```json
{
  "stopId": "string",
  "stopName": "string",
  "latitude": "double",
  "longitude": "double",
  "routeId": "string"
}
```

### Bus Schedules Collection
```json
{
  "scheduleId": "string",
  "routeId": "string",
  "stopId": "string",
  "arrivalTime": "string (HH:mm)",
  "departureTime": "string (HH:mm)",
  "frequency": "integer (minutes)",
  "daysOfWeek": ["Mon", "Tue", "Wed", "Thu", "Fri"]
}
```

## Installation & Setup

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK 24 or higher
- Firebase account

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd BusArrivalPrediction
   ```

2. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app to your Firebase project
   - Download the `google-services.json` file
   - Replace the placeholder `google-services.json` in `app/` directory
   - Enable Email/Password authentication in Firebase Console
   - Create a Realtime Database in Firebase Console

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the project directory
   - Wait for Gradle sync to complete

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Click "Run" button or press Shift+F10
   - The app will install and launch on your device

## Usage

### For Users

1. **Registration**
   - Open the app
   - Click "Register Here"
   - Fill in your details (Name, Email, Phone, Password)
   - Click "Register"

2. **Login**
   - Enter your email and password
   - Click "Login"

3. **View Bus Arrivals**
   - Select a bus route from the dropdown
   - Select a bus stop from the dropdown
   - Click "Get Arrival Times"
   - View predicted arrival times

### For Admins

1. **Login as Admin**
   - Use admin credentials to login
   - You'll be redirected to Admin Dashboard

2. **Manage Routes**
   - Click "Manage Routes" card
   - Add route details (Route Number, Name, Start/End Points)
   - Click "Save"

3. **Manage Stops**
   - Click "Manage Stops" card
   - Add stop details (Stop Name, Coordinates, Route ID)
   - Click "Save"

4. **Manage Schedules**
   - Click "Manage Schedules" card
   - Add schedule details (Route ID, Stop ID, Arrival Time, Frequency)
   - Click "Save"

## Prediction Algorithm

The bus arrival prediction is calculated using the following logic:

1. Retrieve the base arrival time from the schedule
2. Get the current time
3. Calculate time difference
4. If the bus has already passed, add frequency intervals until next arrival
5. Display the result in minutes

**Formula:**
```
Next Arrival = Base Time + (Frequency × n)
where n is the smallest integer that makes Next Arrival > Current Time
```

## Input Validation

The application implements comprehensive input validation:

- **Email**: Valid email format (example@domain.com)
- **Password**: Minimum 6 characters
- **Phone**: 8-15 digits, optional country code
- **Name**: Minimum 2 characters, letters and spaces only
- **Time**: HH:mm format (24-hour)
- **Empty Fields**: All required fields must be filled

## Security Features

- Firebase Authentication for secure user management
- Password encryption handled by Firebase
- Role-based access control (User/Admin)
- Input validation to prevent injection attacks
- Secure communication with Firebase backend

## Future Enhancements

1. **Real-time GPS Integration**: Track buses in real-time using GPS
2. **Push Notifications**: Notify users of bus arrivals and delays
3. **Route Planning**: Multi-stop journey planning
4. **Payment Integration**: In-app ticket purchasing
5. **Offline Mode**: Cache data for offline access
6. **Multi-language Support**: Arabic and English languages
7. **Accessibility Features**: Voice commands and screen reader support
8. **Analytics Dashboard**: Usage statistics for administrators

## Testing

### Test Cases

1. **Authentication Tests**
   - Valid login credentials
   - Invalid login credentials
   - Registration with valid data
   - Registration with duplicate email
   - Password validation

2. **Functionality Tests**
   - Route selection
   - Stop selection
   - Arrival prediction calculation
   - Admin CRUD operations

3. **UI Tests**
   - Screen navigation
   - Input validation messages
   - Progress indicators
   - Empty state displays

## Known Issues

1. The `google-services.json` file is a placeholder and must be replaced with actual Firebase configuration
2. Sample data is created automatically for testing purposes
3. Admin functionality requires manual role assignment in Firebase Database

## Version Control

This project uses Git for version control and is hosted on GitHub.

### Git Commands Used

```bash
# Initialize repository
git init

# Add files
git add .

# Commit changes
git commit -m "Initial commit: Bus Arrival Prediction System"

# Add remote repository
git remote add origin <repository-url>

# Push to GitHub
git push -u origin main
```

## License

This project is developed as part of academic coursework at Middle East College.

## Contact

**Student:** Nasra Amjad Albalushi  
**Email:** [Student Email]  
**Institution:** Middle East College  
**Course:** COMP 30040 - Mobile Application Development

## Acknowledgments

- Middle East College faculty for guidance
- Firebase for backend services
- Material Design for UI components
- Android Developer Documentation

---

**Note:** This project is submitted as part of the Mobile Application Development course (COMP 30040) mini-project requirement at Middle East College.
