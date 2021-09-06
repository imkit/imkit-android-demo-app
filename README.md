# IMKIT Android samples

![Languages](https://img.shields.io/badge/language-JAVA-orange.svg)
![Platform](https://img.shields.io/badge/platform-ANDROID-orange.svg)


# About IMKIT
Through IMKIT Android SDK, you will be able to integrate chat into your app easily and efficiently. Follow the simple integration process below to build the chat with feature-rich experience.


## Requirements
- Android 6.0 (API level 23) or higher
- Android Studio 3.1+ 
- Java 8 or higher
- Android Gradle plugin 4.2.2 or higher


# Get started
This tutorial provides you a step-by-step guide to install IMKIT Android SDK to your new project or an existing project you own with minimum efforts. Please check out the details in the complete guide and documents. You can find solutions to related issues you may face during the installations. Let’s get started.

## Step 1 - Create a Project or install on existed Project

## Step 2 - Install SDK through Gradle

### Add to your project root build.gradle
```
buildscript {
  ext.imkit_version = "3.1.350"
  dependencies {
     classpath "io.realm:realm-gradle-plugin:10.8.0"
     classpath 'com.google.gms:google-services:4.3.10'
  }
}
```

### Add to your application modules' build.gradle
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
    
    implementation("com.imkit:imkit-sdk-v3:${rootProject.ext.imkit_version}@aar")
    implementation("com.imkit:imkit-widget-v3:${rootProject.ext.imkit_version}@aar")

    implementation "androidx.arch.core:core-runtime:2.1.0"

    implementation "androidx.lifecycle:lifecycle-runtime:2.3.1"
    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    implementation "androidx.lifecycle:lifecycle-process:2.3.1"
    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation "androidx.lifecycle:lifecycle-common-java8:2.3.1
    
    implementation 'androidx.multidex:multidex:2.0.1'
    // appcompat:1.3.1 has error java.lang.NoClassDefFoundError: Failed resolution of: Landroidx/lifecycle/ViewTreeLifecycleOwner;
    implementation 'androidx.appcompat:appcompat:1.2.0'

    implementation 'com.google.android.material:material:1.4.0'

    implementation 'androidx.recyclerview:recyclerview:1.2.1'

    implementation 'com.github.bumptech.glide:okhttp3-integration:4.12.0@aar'
    implementation 'com.github.bumptech.glide:glide:4.12.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.12.0'
    annotationProcessor 'androidx.annotation:annotation:1.2.0'

    implementation 'com.squareup.okhttp3:okhttp:4.9.0'
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:retrofit-adapters:2.8.1'
    implementation 'com.google.code.gson:gson:2.8.6'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

    implementation ('io.socket:socket.io-client:2.0.0') {
        // excluding org.json which is provided by Android
        exclude group: 'org.json', module: 'json'
    }

    // Firebase messaging, optional for push notification
    implementation ('com.google.firebase:firebase-messaging:22.0.0')

    // WebRTC, Optional for video and audio calls
    implementation 'org.webrtc:google-webrtc:1.0.32006'

    // Google maps and google location, optional for location message
    implementation 'com.google.android.gms:play-services-maps:17.0.1'
    implementation 'com.google.android.gms:play-services-location:18.0.0'

    // ExoPlayer, optional for video and audio message
    implementation 'com.google.android.exoplayer:exoplayer-core:2.15.0'
    implementation 'com.google.android.exoplayer:exoplayer-ui:2.15.0'

    // Downloader: optional for file message
    implementation "androidx.tonyodev.fetch2:xfetch2:3.1.4"

    // Web Parser: optional for URL message preview
    implementation 'org.jsoup:jsoup:1.12.1'
  ...
}

apply plugin: 'com.google.gms.google-services'
```

## Step 3 - Initialize IMKIT

Before initializing IMKIT, you need to have two things ready.

1. client key
2. chat server url    

You can get these private values from our dashboard.

![Dashboard](https://i.imgur.com/Q9J0tqG.png "Dashboard")
(In order to continue this tutorial, please check out our dashboard if you don’t have these values.)

Dashboard intro: https://hackmd.io/B2ARb__GQ2SJeLOxuL8sLg

(Please check out the How to get client key tutorial)


```
IMKit.instance()
    .setUrl(url)
    .setClientKey(clientKey)
    .setBucketName(bucketName)
    .setProviderAuthority(providerAuthority)
    .init(context);
```
url: Your chat server entrypoint URL
clientKey: Client Key
bucketName: S3 prefix path of IMKit uploaded file. Default is `imkit`
providerAuthority: Provider authority string

### In your `AndroidManifest.xml`

Add permissions
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.CAMERA"/>    
    
    <!--  Optional for location message  -->
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    
    <!-- Optional for video/audio calls -->
<!--    <uses-permission android:name="android.permission.ACTION_MANAGE_OVERLAY_PERMISSION" />-->
<!--    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />-->
<!--    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />-->
```

Add FileProvider for IMKit to access device files and media
```
<!--Please replace FileProvider to users-->
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="com.imkit.app.fileProvider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_paths"/>
</provider>

<service
    android:name="com.imkit.sdk.firebase.IMFirebaseMessagingService">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT"/>
    </intent-filter>
</service>
```

## Step 4 - Show Chat Room List Scene
We implemented multiple default scenes in IMKIT SDK, and you don’t need generate any swift files to show the chat room list and chat room.

Choose a activity or fragment you want to show the chat room list from, and implement the following code to it.

### 1. Prepare user data
First, we need users to get the chat started. To start, we need three things:

1. userId
2. username
3. accessToken
userId & username are string type, and accessToken is optional string type. So we created currentUserId, currentUserNickname and accessToken for demo purpose.

(It is also completely fine to use the user data from your app)

We also created another user id (called otherUserId) to join the chatroom with the first user.

### 2. Prepare Access Token
Every time you use the IMKIT API, the IMKIT chat server will verify the accessToken which is generated or issued from it previously. For production mode, there are two ways to generate your access token for IMKIT chat server. One way is to get your own access token from your app server, and the other way is to generate one from the IMKIT chat server. In this section, we will use a null access token to indicate that we are runing under sandbox mode, which is for demo purpose only. DO NOT USE A NULL ACCESS TOKEN FOR PRODUCTION MODE.

If you need more details on access token, please check out this tutorial.
https://github.com/FUNTEKco/chat-server-document/wiki/%5BAuth%5D-Client-authorization

After fetching the access token from your app server, provide it with userId through connect method of IMKIT.

```
IMKit.instance().connect(uid, accessTokennew IMRestCallback<Client>() {
    @Override
    public void onResult(Client client) {
        // success
    }

    @Override
    public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
        
    }
});
```

As for the access token from IMKIT, it will be stored permanently and the IMKit SDK will help handle it until you log out from IMKIT.

### 3. Update User Info
After successfully connecting to IMKIT server with access token and userId, try to update your user data including the nickname, avatar, and description, in the IMKIT server through updateCurrentUserInfo method.
```
IMKit.instance().updateCurrentUserInfo(nickname, avatarUrl, new IMRestCallback<Client>() {
    @Override
    public void onResult(Client client) {
        // success
    }

    @Override
    public void onFailure(Call<ApiResponse<Client>> call, Throwable throwable) {
        
    }
});
```

### 4. Create direct-chat-room
In order to demonstrate a direct chat with someone in the chatroom, we created a fixed room ID by hashing with inviteeID.

(Chatrooms will not be created repeatedly after executing the method over and over again. Our solid backend server team will handle this.)

IMKit.instance().createRoomWithUser(String inviteeId, final IMRestCallback<Room> callback) {
    @Override
    public void onResult(Room room) {
    }
});

### 5. Enter chat room list scene
After setup, it is time to enter the chat room list scene by presenting RoomListFragment.

Here’s a sample code:

```
public static void showRoomList() {
    final RoomListFragment fragment = RoomListFragment.newInstance();
    fragment.setListener(new RoomListFragment.RoomListFragmentListener() {

        @Override
        public void onRoomSelect(Room room) {
            // Show chat room
            pushFragment(ChatFragment.newInstance(room.getId(), Utils.getDisplayRoomTitle(this, room)));
        }

        @Override
        public View onCreateRoomListEmptyView(ViewGroup parent) {
            // You can customize a view to display when there's room list is empty
        }

        @Override
        public void onRoomListSearchClick() {

        }
    });

    // Your implementation of preseting fragment
    pushFragment(fragment);
}
```

### 6. Enter a chat room
```
pushFragment(ChatFragment.newInstance(SOME_ROOM_ID, ROOM_TITLE);
```


Please feel free to send your first greeting to the Mock User, with text message or image message maybe. The features included in the chatroom are all default, without any customization.

If there are more ideas you want to implement in your app to make it shine, please check out our documentations.


## Demo app

### IMKIT
Helper module, purposes
1. Reduce calls to IMKit.instance(), wrap custom parameters and navigation implementation for your customization requirement.
2. Demonstrate UI cusotmization

```
// Init
IMKIT.init(context, IMKIT_URL, IMKIT_CLIENT_KEY, IMKIT_BUCKET_NAME, PROVIDER_AUTHORITY);

// Login IMKit with name (for development/sandbox purpose)
IMKIT.login(Activity activity, String name, final IIMKIT.Login callback)

// Login IMKit with accessToken and userInfomation (for production)
// Your should get the access token from your secured server.
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
