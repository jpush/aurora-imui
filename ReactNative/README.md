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

**Attention（Android）：We are using support v4 & v7 version 25.3.1, so you should modify buildToolsVersion and compileSdkVersion to 25 or later, you can refer to sample's configuration.**

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


- ### iOS

  - Find PROJECT -> TARGETS -> General -> Embedded Binaries  and add RCTAuroraIMUI.framework
  - Find PROJECT -> TARGETS ->  Build Phases -> Target Dependencies and add RCTWebSocket

## API

[API doc](./docs/APIs.md)