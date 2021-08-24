
![banner](https://github.com/smokelaboratory/freedom/blob/master/banner.svg)

# Freedom

[![License](https://img.shields.io/badge/License-Apache%202.0-2196F3.svg?style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)
[![language](https://img.shields.io/github/languages/top/smokelaboratory/freedom.svg?style=for-the-badge&colorB=f18e33)](https://kotlinlang.org/)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg?style=for-the-badge)](https://www.android.com/)
[![API](https://img.shields.io/badge/API-21%2B-F44336.svg?style=for-the-badge)](https://android-arsenal.com/api?level=21)

Freedom is an Android library to help you with all aspects of runtime permission management.

## Features

* Written in Kotlin
* Based on ActivityResultLauncher
* Built using Kotlin DSL
* No boilerplate code
* Easy initialisation
* Common management for Activities and Fragments
* Identifies permissions with 'Never ask again' flag
* Provides a built-in Material Dialog to navigate user into settings screen
* Manages permission-grant callback from settings screen

## Gradle Dependency

* Add the JitPack repository to your project's build.gradle file

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

* Add the dependency in your app's build.gradle file

```
dependencies {
    implementation 'com.github.smokelaboratory:freedom:2.0.0'
}
```

## Usage

See [Wiki](https://github.com/smokelaboratory/freedom/wiki) on how to get started with **Freedom**.

### Pull Request
To generate a pull request, please consider following [Pull Request Template](https://github.com/smokelaboratory/freedom/blob/master/PULL_REQUEST_TEMPLATE.md).

### Issues
To submit an issue, please check the [Issue Template](https://github.com/smokelaboratory/freedom/blob/master/ISSUE_TEMPLATE.md).

Code of Conduct
---
[Code of Conduct](https://github.com/smokelaboratory/freedom/blob/master/CODE_OF_CONDUCT.md)

## Contribution

You are most welcome to contribute to this project!

Please have a look at [Contributing Guidelines](https://github.com/smokelaboratory/freedom/blob/master/CONTRIBUTING.md) before contributing and proposing a change.

# License

```
   Copyright © 2021 smokelaboratory

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
