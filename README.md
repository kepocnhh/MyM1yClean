![compileSdkVersion 25](https://img.shields.io/badge/compileSdkVersion-25-yellow.svg?style=true) ![buildToolsVersion 25.0.0](https://img.shields.io/badge/buildToolsVersion-25.0.0-blue.svg?style=true) ![minSdkVersion 15](https://img.shields.io/badge/minSdkVersion-15-red.svg?style=true) ![targetSdkVersion 25](https://img.shields.io/badge/targetSdkVersion-25-green.svg?style=true)

<h1 align="center">MyM1yClean</h1>

<div align="center">
  <img src="media/icon.png"/>
</div>
<div align="center">
  <strong>Android clean architecture experiment</strong>
</div>

## :wrench: Architecture

- [MVP](https://github.com/kepocnhh/MyM1yClean/tree/master/business/src/main/java/stan/mym1y/clean/contracts) architecture
- [DAO](https://github.com/kepocnhh/MyM1yClean/tree/master/business/src/main/java/stan/mym1y/clean/data/local) layer
- flexible [Dependency Injection](https://github.com/kepocnhh/MyM1yClean/tree/master/implementation/src/main/java/stan/mym1y/clean/components) system
- painless jump from [SQLite](https://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html) to [NoSQL](https://github.com/kepocnhh/MyM1yClean/tree/master/implementation/src/main/java/stan/mym1y/clean/boxes) Object-Relational Mapping [Boxes](https://github.com/StanleyProjects/Boxes)
- also painless jump from [SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences.html) to Cases feature of [Boxes](https://github.com/StanleyProjects/Boxes) lib
- use [Google Identity Toolkit](https://developers.google.com/identity/toolkit) and [Firebase Realtime Database](https://firebase.google.com/docs/database)
- also use [Google oauth2 api](https://www.googleapis.com/oauth2/v4/token) for authorized with [Google account](https://accounts.google.com)