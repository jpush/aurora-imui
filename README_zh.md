[![tag](https://img.shields.io/badge/tag-0.2.0-blue.svg)](https://github.com/jpush/imui/releases)
[![support](https://img.shields.io/badge/support-iOS%20%26%20Android-brightgreen.svg)]()
[![QQ Group](https://img.shields.io/badge/QQ%20Group-604798367-red.svg)]()

# Aurora IMUI

[English Document](./README.md)

Aurora IMUI 是个通用的即时通讯（IM）UI 库，不特定于任何 IM SDK。

本 UI 库提供了消息列表、输入视图等常用组件，支持常见的消息类型：文字、图片、语音、视频等。默认包含多套界面风格，也能根据自己的需要自定义。

同步支持 Android、iOS 平台。计划支持 React Native。

<p align="center">
    <a target="_blank">
        <img src="https://github.com/huangminlinux/resource/blob/master/IMUIPick%402x.png" alt="IMUI" width=960/>
    </a>
</p>

## 功能

可以基于本 UI 库实现的功能：
- 消息列表的展示；
  - 支持多种消息类型；
  - 对每种消息类型的点击、长按处理；
  - 支持用户头像。
- 消息输入：
  - 支持多种消息类型；
  - 语音输入组件；
  - 相册照片选取组件；
  - 视频拍摄。

当前支持展示与输入的消息类型：
- 文字
- 图片
- 语音
- 视频

## 使用
当前提供的组件：

### Android
- [MessageList](./docs/Android/message_list_usage_zh.md) (消息列表)
- [ChatInputView](./Android/chatinput/README.md) (聊天输入组件)

### iOS (Swift)

- [IMUIMessageCollectionView](./docs/iOS/IMUIMessageCollectionView_usage_iOS_zh.md)
- [IMUIInputView](./docs/iOS/IMUIInputView_usage_zh.md)

## Contribute

欢迎提交 PR 与提出 Issues。

有新功能需求的，请也提出 Issue，打上「new feature」标签。我们以大家对每个 new feature 的响应来确定优先级。


## License
MIT © [JiGuang](/LICENSE)
