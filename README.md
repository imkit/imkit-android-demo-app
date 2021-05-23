# IMKit iOS Framework v3.0

# About IMKit
IMKit is a live chat platform solution, more detail please visit: https://imkit.io

## Features
- [x] Room list / Chat room
- [x] Support text, sticker, image, video, audio, location message
- [x] Support reply message
- [x] URL detection and preview
- [x] Typing indicator

## Requirements
- Android SDK Version 16+
- Android Studio
- Gradle 3.1.4+

## Gradle Settings
Add to your project root build.gradle
```
buildscript {
  dependencies {
    classpath "io.realm:realm-gradle-plugin:5.7.1"
    classpath 'com.google.gms:google-services:3.2.0'
  }
}
```

Add to your application modules' build.gradle
```
apply plugin: 'com.android.application'
apply plugin: 'realm-android'
apply plugin: 'maven'

repositories {
    maven {
        name = "IMKit"
        url "https://codebase.funtek.io/api/v4/projects/80/packages/maven"
        credentials(HttpHeaderCredentials) {
            name = "Private-Token"
            value = "QeY8AazVwMz95zfb7aEQ"
        }
        authentication {
            header(HttpHeaderAuthentication)
        }
    }
}

dependencies {
  ...

  implementation project(':imkit')
  implementation 'com.android.support:appcompat-v7:28.0.0'
  implementation 'com.android.support:recyclerview-v7:28.0.0'
  implementation 'com.android.support:support-v4:28.0.0'
  implementation 'com.github.bumptech.glide:glide:4.6.1'
  implementation 'com.github.bumptech.glide:okhttp3-integration:4.6.1@aar'
  annotationProcessor 'com.github.bumptech.glide:compiler:4.6.1'
  implementation 'com.google.android.exoplayer:exoplayer:r2.4.0'
  implementation 'com.google.firebase:firebase-messaging:12.0.0'
  implementation 'org.jsoup:jsoup:1.11.3'
  implementation "com.tonyodev.fetch2:fetch2:2.2.0-RC16"

  ...
}

apply plugin: 'com.google.gms.google-services'
```

## Init SDK
Call `IMKIT.init(getApplicationContext())` at app module for init IMKit SDK

## Usage
### IMKIT
```
IMKIT.init(Context context)

// Login IMKit with name
IMKIT.login(Activity activity, String name, final IIMKIT.Login callback)

// Login IMKit with accessToken and userInfomation
IMKIT.login(String accessToken, String userDisplayName, String userAvatarUrl, String userDescription, final IIMKIT.Login callback)

// Update user's accessToken
IMKIT.refreshToken(String accessToken)

// Logut IMKit
IMKIT.logout(IIMKIT.Logout callback)

// Uptaer userInformation
IMKIT.updateUser(String userDisplayName, String userAvatarUrl, String userDescription, final IIMKIT.UpdateUser callback) 

// Intent to roomList
IMKIT.showRoomList(final Activity activity, final FragmentManager fragmentManager, final int fragmentContainerId, int requestCode)

// Intent to chatRoom
IMKIT.showChat(Activity activity, String roomId, String title, int requestCode)

// Intent to roomInfo
IMKIT.showRoomInfo(final Activity activity, final String roomId, final String title, final int requestCode, final IIMKIT.RoomInfo callback)

// Private chat
IMKIT.createRoomWithUsers(Context context, ArrayList<String> userIds, final IIMKIT.CreateRoom callback)

// Group chat
IMKIT.createRoomWithUser(Context context, String userId, final IIMKIT.CreateRoom callback)

// Get badge information
IMKIT.getBadge(final IIMKIT.Badge callback)
```

### Customize UI
user can customize roomlist / chatroom / roomInfo at imkit module
```
- roomlist : com.imkit.CustomRoomListFragment
- chatroom : com.imkit.CustomChatFragment
- roomInfo : com.imkit.CustomRoomInfoFragment
```
