# 📱 Joiefull - Clothing Catalog App
![Android](https://img.shields.io/badge/Android-3DDC84?style=flat&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android%20Studio-3DDC84?style=flat&logo=android-studio&logoColor=white)  
[![Render](https://img.shields.io/badge/Render-46E3B7?style=for-the-badge&logo=render&logoColor=black)](https://render.com)

_Joiefull_ is a Kotlin/Jetpack Compose Android application developed entirely from scratch.
It showcases a modern clothing catalog experience with full support for adaptive layout, accessibility, and multilingual use.

## 🚀 About the Project

This application provide a clothing catalog with home and detail screens allowing users to: 
- Browse the list of available clothes sorted by category
- Open cloth page for detailed information about it, including original and discounted prices, rating, like status, and user review
- Add or remove a like to cloth items
- Share experience on cloth items
- Share cloth item page on social networks.

The app was built entirely in Kotlin using modern Android best practices.

## ✨ Features

### 📱 Adaptive Layouts
- Optimized UI for phones, tablets, and foldables.
- Leverages responsive Compose design for a consistent experience across form factors.

### ♿ Accessibility First
- Full TalkBack support integrated into every screen.
- Semantic descriptions, state announcements, and custom actions to ensure usability for all.

### 🌍 Internationalization
- Available in English and French.
- Easily extendable for more languages.

### 🔌 Custom Shared Android Library
- Uses my [shared-android-library](https://github.com/OlivierMarteaux/shared-android-library.git) deployed on [Render.com](https://render.com/)
  

### ☁️ Custom Backend API
- Powered by the my [custom RESTful Clothes Api](https://github.com/OlivierMarteaux/A050_ClothesApi.git)deployed on [Render.com](https://render.com/)
- RESTful backend delivering catalog data.
- Enables offline-first and reactive UI updates.

## 🧰 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, ViewModel, State Management |
| Navigation | Jetpack Navigation Compose |
| Data Storage | Custom RESTFul API deployed on Render |
| Data Access service | KTor |
| Background Work | Kotlin Coroutines, Flows |
| Image Handling | URI-based storage in internal memory using Coil |
| Accessibility-First | Optimized for talkback users |
| Dependency Injection | Koin |
| Build | Gradle (KTS) |

## 📂 Project Structure

```
Joiefull/
├── ui/                    
│   ├── screens/           # UI screens and viewmodels
│   └── navigation/        # Destinations and NavGraph
├── domain/				   # Data models (Items)
├── data/
│   ├── network/           # Ktor API Service,DTOs,mappers
│   └── repository/        # Abstractions + implementation
└── JoiefullApplication.kt # App-level container & DI
└── Joiefull.kt 		   # Top-level composable

```

## 📸 Screenshots

Visit the following link to browse screenshots of the Joiefull application:  
🔗 [Joiefull App Screenshots](screenshots/)

## 📲 Install

To install the Joiefull application on your physical Android device:

1. **Download the APK from your smartphone**
   - Go to the [Releases](https://github.com/OlivierMarteaux/A049_Joiefull/releases) section of this repository.
   - Download the latest ` Joiefull.apk ` file.

2. **Enable Unknown Sources**
   - On your device, go to `Settings` > `Security`.
   - Enable **Install from unknown sources** (you can disable it again after installation).

3. **Install the APK**
   - Use a file explorer app on your device to locate the APK file.
   - Tap on it and follow the prompts to install the app.

4. **Launch the App**
   - Once installed, open the app from your launcher and start using Joiefull!

> ⚠️ Note: You may need to allow permissions during the first launch.

## ⚙️ Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/OlivierMarteaux/A049_Joiefull.git
   ```

2. Open in **Android Studio**

3. Configure API Key (if needed for exchange rate API)

4. Run on an emulator or physical device (API 26+ recommended)

## 👨‍💼 Author

_Olivier Marteaux_  
Former aerospace engineer turned Android developer.

Read more about my transition on [LinkedIn](https://linkedin.com/in/olivier-marteaux).  
Check out my journey and projects:
- 🔗 [Google Developer Profile](https://g.dev/OlivierMarteaux)
- 💻 [GitHub Projects](https://github.com/OlivierMarteaux)
- 📢 [LinkedIn Post – Career Change](https://www.linkedin.com/posts/olivier-marteaux_androidbasics-careerchange-androiddevelopment-activity-7351370158369628164-FmqZ?utm_source=share&utm_medium=member_desktop&rcm=ACoAACynrz8BkrhJFrStq3CEX6rQIEfnG7goFdg)

## 🤝 Acknowledgments

- [OpenClassrooms Android Pathway](https://openclassrooms.com/fr/paths/527/projects/1644/385-mission---developpez-une-interface-accessible-avec-jetpack-compose)
- [Google Android Basics](https://developer.android.com/courses/android-basics-compose/course)
- JetBrains & Jetpack Compose Community

## 📄 License

This project is for educational and demonstration purposes. Not licensed for commercial use. For inquiries, please contact me.

---