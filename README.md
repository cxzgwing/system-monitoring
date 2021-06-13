# system-monitoring Java简易系统监视器
Java Swing applet, real-time monitoring system CPU usage and memory usage.

# 项目与工具
Maven、maven-assembly-plugin、Java 8、jnativehook-2.1.0

# 预览图
![Snipaste_2021-06-13_16-24-00](https://user-images.githubusercontent.com/41880446/121800502-effc0900-cc64-11eb-8b6b-772698fc57be.jpg)
![image](https://user-images.githubusercontent.com/41880446/121800529-13bf4f00-cc65-11eb-9bcc-5937cd314cfd.png)
![image](https://user-images.githubusercontent.com/41880446/121800680-df985e00-cc65-11eb-8b23-a47711509398.png)
![image](https://user-images.githubusercontent.com/41880446/121800704-f474f180-cc65-11eb-9727-4b7694c10d71.png)


# 功能说明
<ol>
  <li>Java Swing小程序，实时监测系统CPU使用率和内存使用率，每秒刷新一次。</li>
  <li>程序启动默认位置为屏幕右下角，且默认无法移动。</li>
  <li>鼠标右击系统托盘图标可弹出菜单栏，菜单栏上有“移动”、“取消”、“退出程序”三个按钮。</li>
  <li>点击“移动”按钮后，方可移动窗体，此时鼠标若悬浮于窗体之上则变成十字状态，窗体有边界限制，不可移至屏幕外；再次点击该按钮时（此时显示为“固定”按钮）即可固定位置。</li>
  <li>点击“取消”按钮后隐藏菜单栏。当菜单栏显示时，鼠标左击菜单栏之外的区域也可隐藏菜单栏。</li>
  <li>点击“退出程序”按钮即可退出该程序。</li>
  <li>窗体默认隐藏任务栏图标，且设置为置顶显示。</li>
</ol>
 
# 项目说明
<ol>
  <li>窗体使用JFrame，且自定义样式。</li>
  <li>菜单栏使用JPopupMenu，“当菜单栏显示时，鼠标左击菜单栏之外的区域也可隐藏菜单栏”的技术点为jnativehook全局监听鼠标左击。详细说明：由于“创建点击图标时的弹出菜单方案二”中，弹出的菜单无法消失，会一直显示，所以需要根据鼠标是否悬浮于菜单上以及鼠标左键是否点击来判断是否需要隐藏菜单：若鼠标不在菜单上并且鼠标左键点击了并且菜单是显示状态，则隐藏菜单。默认设置为鼠标未悬浮于菜单之上、鼠标未点击、菜单未显示。其中，鼠标是否悬浮于菜单上的判断条件包含鼠标是否悬浮于菜单上、鼠标是否悬浮于按钮上。</li>
  <li>菜单栏创建方案有两种，此项目使用的是方案二JPopupMenu，方案一PopupMenu的代码也保留在项目中（已注释）。简要说明采用方案二的原因：使用PopupMenu创建的菜单栏，无法添加鼠标监听，局限性太大。</li>
</ol>

# 项目地址
https://github.com/cxzgwing/system-monitoring

# 博客地址
https://blog.csdn.net/qq_36533690/article/details/117881862
