# Java简易系统监视器 system-monitoring 

![image](https://img.shields.io/github/license/cxzgwing/system-monitoring?style=flat-square)

Java Swing applet, real-time monitoring system CPU usage and memory usage.

Java Swing小程序，实时监测系统CPU使用率和内存使用率。

Java简易系统监视器system-monitoring：实时显示CPU使用率、内存使用率、笔记本电脑电池剩余电量、时间（时、分、秒）。创建系统托盘，设置系统托盘菜单，窗体置顶显示。通过jna调用dll文件读取电池数据。

# 项目与工具
Maven、Java 8、Swing、maven-assembly-plugin（jar-with-dependencies）、jna、dll

# 预览图
![Snipaste_2021-06-13_16-24-00](https://user-images.githubusercontent.com/41880446/121800502-effc0900-cc64-11eb-8b6b-772698fc57be.jpg)
![image](https://user-images.githubusercontent.com/41880446/121800529-13bf4f00-cc65-11eb-9bcc-5937cd314cfd.png)
![image](https://user-images.githubusercontent.com/41880446/121800680-df985e00-cc65-11eb-8b23-a47711509398.png)
![image](https://user-images.githubusercontent.com/41880446/121800704-f474f180-cc65-11eb-9727-4b7694c10d71.png)

# v1.2.2菜单预览
![图片](https://user-images.githubusercontent.com/41880446/124697063-c14a1900-df18-11eb-9c95-d26157a0784a.png)

# v2.0.0预览图
![Snipaste_2022-01-18_14-37-23](https://user-images.githubusercontent.com/41880446/149907348-5da5d631-f1ba-410f-b64d-14654e5ef36d.png)
![Snipaste_2022-01-18_14-36-39](https://user-images.githubusercontent.com/41880446/149907383-629cc41d-a62f-4110-aec4-57cb84c75128.png)
![Snipaste_2022-01-18_14-36-42](https://user-images.githubusercontent.com/41880446/149907401-e904cbed-30b4-48c0-8202-b3e6bb388b19.png)
![Snipaste_2022-01-18_14-36-45](https://user-images.githubusercontent.com/41880446/149907419-9fd9df61-b554-4f0b-b808-832431f279f6.png)

# 功能说明
1 Java简易系统监视器system-monitoring：实时显示CPU使用率、内存使用率、笔记本电脑电池剩余电量、时间（时、分、秒）。CPU使用率、内存使用率和时间每秒更新一次，笔记本电脑电池剩余电量每15秒更新一次。

2 创建系统托盘，设置系统托盘菜单，窗体置顶显示。

3 系统托盘按钮：

  --3.1 移动（或固定）：点击后可移动或固定窗体。
  
  --3.2 刷新：点击可刷新窗体，当窗体显示异常时可使用。
  
  --3.3 布局：可更改窗体布局，可选“单列布局”、“双列布局”。（效果图为双列布局）
  
  --3.4 显示：可选择需要显示的参数，可勾选“CPU”、“内存”、“电量”、“时间”。
  
  --3.5 退出程序：点击可退出程序。
  
4 窗体不可移动至屏幕外。

5 窗体大小可根据显示的参数个数自动适配。

6 点击系统托盘后，弹出多级菜单，当鼠标点击非菜单区域，弹出的多级菜单会自动消失。（基于JFrame+JPopupMenu实现）
 
# 项目说明
<ol>
  <li>窗体使用JFrame，且自定义样式。</li>
  <li>（旧版）菜单栏使用JPopupMenu，“当菜单栏显示时，鼠标左击菜单栏之外的区域也可隐藏菜单栏”的技术点为jnativehook全局监听鼠标左击。详细说明：由于“创建点击图标时的弹出菜单方案二”中，弹出的菜单无法消失，会一直显示，所以需要根据鼠标是否悬浮于菜单上以及鼠标左键是否点击来判断是否需要隐藏菜单：若鼠标不在菜单上并且鼠标左键点击了并且菜单是显示状态，则隐藏菜单。默认设置为鼠标未悬浮于菜单之上、鼠标未点击、菜单未显示。其中，鼠标是否悬浮于菜单上的判断条件包含鼠标是否悬浮于菜单上、鼠标是否悬浮于按钮上。</li>
  <li>（旧版）菜单栏创建方案有两种，此项目使用的是方案二JPopupMenu，方案一PopupMenu的代码也保留在项目中（已注释）。简要说明采用方案二的原因：使用PopupMenu创建的菜单栏，无法添加鼠标监听，局限性太大。</li>
  <li>v2.0.0版本采用JFrame+JPopupMenu实现系统托盘多级菜单</li>
</ol>

# 项目地址
https://github.com/cxzgwing/system-monitoring

# 博客地址
https://blog.csdn.net/qq_36533690/article/details/117881862

https://blog.csdn.net/qq_36533690/article/details/122559092

# 分支说明：
**main**：主分支，稳定版本代码，功能将落后于develop分支。

**develop**：开发分支，新功能优先上该分支，待自我验证运行一段时间无重大问题后，将更新至main分支。

**reduce**：精简分支，只保留最基本功能，部分用于优化用户体验但会增加系统开销的功能将去除。

**first**：最初版本分支，为防止到最后被改得“面目全非”、不便于查找，特别新建该分支，保留最初版本。
