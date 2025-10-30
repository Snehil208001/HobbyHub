🎨 HobbyHub

HobbyHub is a modern, feature-rich social networking app for hobby enthusiasts — designed to connect users with similar interests, groups, and events.
Whether you love music, painting, fitness, or gaming, HobbyHub helps you discover, connect, and grow your passion community.

Built entirely in Kotlin 🧠, the app leverages Jetpack Compose for its declarative UI and follows the MVVM (Model–View–ViewModel) architecture powered by Hilt for dependency injection.

✨ Features
🔐 Authentication

🎬 Splash & Onboarding — Beautiful, multi-page intro screens for first-time users.

✉️ Email & Password — Secure sign-up/sign-in with Firebase Authentication.

🔑 Google Sign-In — One-tap authentication using Google credentials.

🔁 Password Reset — “Forgot Password” feature that sends a reset email.

💾 Remember Me — Saves credentials with SharedPreferences for quick logins.

🧭 Core App Navigation

HobbyHub features a bottom navigation bar and a side navigation drawer for effortless movement between major sections.

🏠 Home Screen — Location-aware dashboard showing hobby recommendations and trending activities.

🔎 Explore — Search and filter hobbies by category (Sports, Music, Art, etc.).

👥 Groups — Discover and join groups (e.g., Urban Painters, City Strummers).

🗺️ Map — Interactive Google Map showing nearby hobby spots and events with category filters.

🧑‍🏫 Workshops — Browse popular and upcoming hobby workshops.

👤 User & Profile Management

🪪 Profile Creation — Add full name, email, and profile picture during registration.

📄 View Profile — Displays profile image, bio, hobbies, and location.

✏️ Edit Profile — Update:

Profile picture (via image picker).

Bio, location, and website.

Privacy mode (public/private).

Select or remove hobbies from a predefined list.

Fetch and set current GPS location.

📂 Side Drawer Features

💬 Messages — Chat with users and groups directly within the app.

🗓️ Calendar — View upcoming workshops and events.

🔖 Bookmarks — Save favorite hobbies, groups, or workshops.

⚙️ Settings — Toggle notifications, dark mode, and other preferences.

🆘 Help & Contact — FAQ and “Contact Us” support.

🔒 Account Management — Secure “Sign Out” and “Delete Account” options (with re-authentication).

🛠️ Tech Stack & Architecture
Component	Technology Used
Language	Kotlin
UI	Jetpack Compose
Architecture	MVVM (Model–View–ViewModel)
Dependency Injection	Hilt (Dagger-Hilt)
Navigation	Jetpack Navigation for Compose
Async Operations	Kotlin Coroutines & Flow
Backend	Firebase Authentication, Firestore, Firebase Storage
Maps	Google Maps SDK for Compose
Location	FusedLocationProviderClient (Google Play Services)
Image Loading	Coil
Helpers	Accompanist (Flow Layout), SharedPreferences
🚀 How to Build
1️⃣ Clone the Repository
git clone https://github.com/your-username/HobbyHub.git
cd HobbyHub/HobbyHub

2️⃣ Configure Firebase

Go to the Firebase Console
.

Create a new Android project.

Register the app with the package name:

com.example.hobbyhub


Enable:

✅ Email/Password Authentication

✅ Google Sign-In

✅ Firestore Database

✅ Firebase Storage

Download the google-services.json file and place it in:

HobbyHub/app/google-services.json

3️⃣ Add Google Maps API Key

Visit the Google Cloud Console
.

Create an API Key and enable:

Maps SDK for Android

Open your Android Manifest file at:

app/src/main/AndroidManifest.xml


Replace the API key value in:

<meta-data
    android:name="com.google.android.geo.API
