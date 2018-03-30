## IMUI for React Native

## 安装

```shell
npm install aurora-imui-react-native --save
react-native link
```

如果 link 安卓失败，需要手动修改一下 `settings.gradle` 中的引用路径：

```java
include ':app', ':aurora-imui-react-native'
project(':aurora-imui-react-native').projectDir = new File(rootProject.projectDir, '../node_modules/aurora-imui-react-native/ReactNative/android')
```

然后在 app 的 `build.gradle`中引用：

```java
dependencies {
    compile project(':aurora-imui-react-native')
}
```

**注意事项（Android）：我们使用了 support v4, v7 25.3.1 版本，因此需要将你的 build.gradle 中 buildToolsVersion 及 compileSdkVersion 改为 25 以上。可以参考 sample 的配置。** 如果你的项目报错：

```
Failed to execute aapt
com.android.ide.common.process.ProcessException: Failed to execute aapt
...
Caused by: java.util.concurrent.ExecutionException: java.util.concurrent.ExecutionException: com.android.tools.aapt2.Aapt2Exception: AAPT2 error: check logs for details
```

那么可能是 aurora-imui-react-native 与你当前项目的 support 包存在冲突。解决方法为在你的 build.gradle 中 exclude aurora-imui 的 support 包。然后显式地添加你自己的扩展包版本。

```groovy
compile (project(':aurora-imui-react-native')) {
    exclude group: 'com.android.support'
}
// 添加自己的版本，需要与 compileSdkVersion 相匹配
implementation 'com.android.support:appcompat-v7:27.1.0'
implementation 'com.android.support:design:27.1.0'
```



## 配置

- ### Android

  - 引入 Package:

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
 #### Android release (打包 Release apk)
 需要添加混淆代码到 app/proguard-rules.pro 文件中：
 ```
 -keep class cn.jiguang.imui.** { *; }
 ```

- ### iOS

  - 找到 PROJECT -> TARGETS -> General -> Embedded Binaries  然后添加 RCTAuroraIMUI.framework。
  - 找到 PROJECT -> TARGETS -> Build Phases -> Target Dependencies 添加 RCTWebSocket。

## API

[API doc](./docs/APIs_zh.md)
