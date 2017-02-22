![compileSdkVersion 25](https://img.shields.io/badge/compileSdkVersion-25-yellow.svg?style=true) ![buildToolsVersion 25.0.0](https://img.shields.io/badge/buildToolsVersion-25.0.0-blue.svg?style=true) ![minSdkVersion 15](https://img.shields.io/badge/minSdkVersion-15-red.svg?style=true) ![targetSdkVersion 25](https://img.shields.io/badge/targetSdkVersion-25-green.svg?style=true)

# MyM1yClean
Android clean architecture experiment

<img src="media/icon.png" width="128" height="128" />

## Architecture
- [MVP](https://github.com/kepocnhh/MyM1yClean/tree/master/business/src/main/java/stan/mym1y/clean/contracts) - MVP architecture
- [DAO](https://github.com/kepocnhh/MyM1yClean/tree/master/business/src/main/java/stan/mym1y/clean/dao) - DAO layer
- [TDD](https://github.com/kepocnhh/MyM1yClean/tree/master/implementation/src/test/java/stan/mym1y/clean) - Testing implementation module
- [NOSQL ORM](https://github.com/kepocnhh/MyM1yClean/tree/master/implementation/src/main/java/stan/mym1y/clean/boxes) - painless jump from SQLite to NoSQL Object-Relational Mapping [Boxes](https://github.com/StanleyProjects/Boxes)
- Use [Google Identity Toolkit](https://developers.google.com/identity/toolkit) and [Firebase Realtime Database](https://firebase.google.com/docs/database)