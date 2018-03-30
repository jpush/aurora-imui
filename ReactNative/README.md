## IMUI for React Native

[中文文档](./README_zh.md)

## Install

```
npm install aurora-imui-react-native --save
react-native link
```

If link Android failed, you need modify `settings.gradle`:

```
include ':app', ':aurora-imui-react-native'
project(':aurora-imui-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/aurora-imui-react-native/ReactNative/android')
```

And add dependency in your app's `build.gradle`:

```
dependencies {
    compile project(':aurora-imui-react-native')
}
```

**Attention（Android）：We are using support v4 & v7 version 25.3.1, so you should modify buildToolsVersion and compileSdkVersion to 25 or later, you can refer to sample's configuration. If you have a build error：

```
Failed to execute aapt
com.android.ide.common.process.ProcessException: Failed to execute aapt
...
Caused by: java.util.concurrent.ExecutionException: java.util.concurrent.ExecutionException: com.android.tools.aapt2.Aapt2Exception: AAPT2 error: check logs for details
```

It would be likely that the android support  libraries in  aurora-imui-react-native conflicts with yours. Solution: In your build.gradle, exclude support libraries from aurora-imui，and add your support libraries.

```groovy
compile (project(':aurora-imui-react-native')) {
    exclude group: 'com.android.support'
}
// Add your support libraries, versions must match compileSdkVersion.
implementation 'com.android.support:appcompat-v7:27.1.0'
implementation 'com.android.support:design:27.1.0'
```



## Configuration

- ### Android

  - Add Package:

  > MainApplication.java

  ```java
  import cn.jiguang.imui.messagelist.ReactIMUIPackage;
  ...

  @Override
  protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          new MainReactPackage(),
          new ReactIMUIPackage()
      );
  }
  ```

  - import IMUI from 'aurora-imui-react-native';

  #### Android Release (Generate release apk)

  Need add proguard rule in your proguard-rules.pro：

  ```
  -keep class cn.jiguang.imui.** { *; }
  ```

  ​


- ### iOS

  - Find PROJECT -> TARGETS -> General -> Embedded Binaries  and add RCTAuroraIMUI.framework
  - Find PROJECT -> TARGETS ->  Build Phases -> Target Dependencies and add RCTWebSocket

## API

[API doc](./docs/APIs.md)