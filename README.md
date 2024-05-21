# Vsystem
Vsystem是一款为开发者提供的SDK，旨在帮助他们快速实现Android应用程序的多开功能。使用Vsystem，开发者可以轻松地将其应用程序转换为支持多个用户账户或多个实例的版本，从而满足不同用户的需求。该SDK具有易于集成、高度可定制和稳定性强等特点，可以大大简化应用程序开发过程，并提高用户体验。

# 使用说明
Vsystem SDK是一款免费的产品，虽然不开源，但您可以自由使用而无需通知本作者。我们严格禁止将该SDK用于外挂助手等严重侵犯商业化和互联网安全的软件。技术是一把双刃剑，在合法合规的前提下使用才能发挥其应有的作用。我们希望开发者们能够遵守相关法律法规和道德准则，以确保技术的正当、合理和安全使用。

# 兼容性

|      |    兼容性 |
| -------- | --------|
| Abi  | `armeabi-v7a / arm64-v8a`| 
| Android version     |   `10.0 ~ 14.0 及后续版本持续兼容更新` |  
| google play     |   `不支持` |  
| AccountManager     |   `不支持` |  

# 集成SDK
1. 使用aar方式将Vsystem SDK引入到您的项目中。其中，vsystem.aar是核心SDK，而fake_api.jar则是系统API的编译依赖库，只需要在编译时使用即可。
具体操作步骤如下
    - 将vsystem.aar和fake_api.jar文件复制到您的项目目录下的libs文件夹中。
    - 在您的项目根目录下的build.gradle文件中添加以下代码
    ```
    implementation(files("./libs/vsystem.aar"))
    compileOnly(files("./libs/fake_api.jar"))
    ```
1. 初始化，在Application#attachBaseContext中加入以下代码初始化
    ```
    Vsystem.doAttachBaseContext(this)
    ```

1. 安装应用
    ```
    VPackageManager.installPackageAsUser("apk路径","包名",InstallFrags.INSTALL_FLAG_STORAGE,0)
    ```
1. 启动应用
    ```
    val launchIntent = VPackageManager.getLaunchIntentForPackage(packageName, 0)
    VActivityManager.preloadAppByLaunchIntent(launchIntent, 0)
    VActivityManager.startActivity(launchIntent, 0)
    ``` 
# api 文档


|类|成员变量|方法 |描述 |
|--------|--------|--------|--------|
|Vsystem||doAttachBaseContext|初始化Vsystem sdk|
|Vlog|enableLog||log开关|
|InstallResult|packageName||所安装app的包名|
||msg||错误信息，success为`false`才会有值|
||success||安装是否成功|
|VActivityManager||startActivity|启动应用|
|||preloadAppByLaunchIntent|预加载应用|
|VPackageManager||getInstalledApplications|获取沙盒中已按照的所有app|
|||isInstalled|判断app是否在沙盒中按照|
|||getLaunchIntentForPackage|获取沙盒中指定app的`launch intent`|
|||forceStopApp|强制停止应用|
|||clearApk|清楚app数据|
|||unInstallApk|卸载app|


# 多开支持情况
微信 QQ 小红书 抖音 快手 美团 拼多多 淘宝 陌陌 探探 哔哩哔哩 九秀直播  腾讯视频  爱聊


# todo list
- [ ] 支持google 套件 
- [ ] 支持AccountManager api
- [ ] 沙盒中支持微信、qq 等第三方登录

# 更新记录
### 2024 
###### 5月15日 支持微信登录
