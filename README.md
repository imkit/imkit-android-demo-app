# Setup bintray
1. Edit local.properties, add
```
bintray.user=<Your Bintray User Account>
bintray.apikey=<Your Bintray API Key>
```

2. Modify Bintray Repo Description in bintray-upload.gradle
### Sample
```
bintray {
    ...
    pkg {
        repo = "IMKit-SDK-V2-Test"
        // it is the name that appears in bintray when logged
        name = project.PUBLISH_ARTIFACT_ID
        userOrg = user
        websiteUrl = siteUrl
        vcsUrl = gitUrl
        licenses = [""]
        publish = false
        version {
            name = project.PUBLISH_VERSION
            desc = 'IMKit-SDK-V2'
            released  = new Date()
            vcsTag = project.PUBLISH_VERSION
            gpg {
                sign = false
            }
        }
    }
    publications = ['Production']
}
```

# Publish AAR Library
0. Clean
./gradlew app:clean
./gradlew sdk:clean
./gradlew widget:clean

1. Archive AAR
```
./gradlew sdk:assembleRelease
./gradlew widget:assembleRelease
```

2. Generate POM
```
./gradlew sdk:generatePomFileForProductionPublication
./gradlew widget:generatePomFileForProductionPublication
```

2.1 Check POM files correctly generated, could be found at
```
sdk/build/publications/Production/pom-default.xml
widget/build/publications/Production/pom-default.xml
```

3. Upload to Bintray
```
./gradlew sdk:bintrayUpload
./gradlew widget:bintrayUpload
```

4. Goto Bintray Web Console and clicks on Publish.


## Gradle Settings

Add to your project root build.gradle
```
buildscript {
  dependencies {
    classpath "io.realm:realm-gradle-plugin:3.0.0"
    classpath 'com.google.gms:google-services:3.0.0'
  }
}
```

Add to your application modules' build.gradle
```
apply plugin: 'com.android.application'
apply plugin: 'realm-android'

repositories {
    maven {
        // Change to official release repository
        url  "http://dl.bintray.com/shine-chen/IMKit-SDK-V2-Test"
    }
}

dependencies {
  ...

  compile ('com.imkit:imkit-sdk-v2:2.0.67@aar') {
    transitive=true
  }
  compile ('com.imkit:imkit-widget-v2:2.0.67@aar')
  compile 'com.android.support:appcompat-v7:27.1.0'
  compile 'com.android.support:recyclerview-v7:27.1.0'
  compile 'com.android.support:support-v4:27.1.0'
  compile 'com.github.bumptech.glide:glide:3.7.0'
  compile 'com.github.bumptech.glide:okhttp3-integration:1.4.0@aar'
  compile 'com.google.android.exoplayer:exoplayer:r2.3.1'
  compile 'com.kbeanie:multipicker:1.1.31@aar'
  compile 'com.google.firebase:firebase-messaging:12.0.0'

  ...
}
```

## Initialization
Add your own Application Class extends Android Application
```
public class MyApplication extends Application {

    private static final String TAG = "MyApplication";

    public void onCreate() {
        super.onCreate();

        IMKit.instance()
                .setUrl(YOUR_IMKIT_SERVER_URL )
                .setClientKey(YOUR_CLIENT_KEY)
                .setToken(YOUR_CLIENT_TOKEN) // The client token could be set later when your obtain one
                .setBucketName(UPLOAD_FILE_BUCKET_NAME) // S3 Bucket Name
                .init(this);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        IMKit.instance().subscribe(refreshedToken, null);

    }
}
```

## Present RoomList and ChatRoom UI
Snippet from MainActivity.java

```java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ...

        pushFragment(instantiateRoomListFragment(), false);
    }

    public RoomListFragment instantiateRoomListFragment() {
        return RoomListFragment.newInstance().setListener(new RoomListFragment.RoomListFragmentListener() {
            @Override
            public void onRoomSelect(Room room) {
                pushFragment(instantiateChatFragment(room));
            }
        });
    }

    public ChatFragment instantiateChatFragment(final Room room) {
        return ChatFragment.newInstance(room.getId(), room.getName()).setListener(new ChatFragment.ChatFragmentListener() {
            @Override
            public void onShowRoomInfo() {
                pushFragment(instantiateRoomInfoFragment(room));
            }
        });
    }

    public RoomInfoFragment instantiateRoomInfoFragment(Room room) {
        return RoomInfoFragment.newInstance(room.getId(), room.getName());
    }

    @Override
    public void onStart() {
        super.onStart();

        IMKit.instance().connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        IMKit.instance().disconnect();
    }
    ...
}
```
