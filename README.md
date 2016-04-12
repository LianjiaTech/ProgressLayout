#ProgressLayout

![](https://github.com/SmartDengg/ProgressLayout/blob/master/images/launcher.png)


##介绍
-----------------

一个轻量的ProgressLayout，能够帮助你实现“加载中”、“无内容”，“网络错误”，“加载失败”等不同场景下的页面切换与展示，并且支持点击页面重试。

##用法
-----------------

可以使用以下几种属性来指定不同情况需要展现的layout：

```java
<resources>
  <declare-styleable name="ProgressLayout">
    <!--正在加载-->
    <attr name="loading_layout" format="integer"/>
    <!--无内容-->
    <attr name="none_content" format="integer"/>
    <!--网络错误-->
    <attr name="network_content" format="integer"/>
    <!--加载失败-->
    <attr name="failed_content" format="integer"/>
  </declare-styleable>

  <attr name="progressLayoutDefStyle" format="reference"/>
</resources>
```


**在Xml中设置**

```java
<com.lianjiatech.infrastructure.ProgressLayout
      android:layout_below="@id/scroller"
      android:id="@+id/progress_container"
      android:layout_margin="@dimen/material_4dp"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="@drawable/boundary_background"
      app:loading_layout="@layout/loading_layout"
      app:none_content="@layout/none_layout"
      app:network_content="@layout/connectionless_layout"
      app:failed_content="@layout/failed_layout"
      >

    <!--Your content layout-->
   ......

  </com.lianjiatech.infrastructure.ProgressLayout>
```

**在Theme中设置**

如果不同的Activity需要对应不同的页面，也可以通过在Theme中指定`progressLayoutDefStyle`的方式来设置需要的Layout：

```java
  <!-- Base application theme. -->
  <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
    <!-- Customize your theme here. -->
     ......

    <item name="progressLayoutDefStyle">@style/progressLayoutStyle</item>
  </style>

  <style name="progressLayoutStyle">
    <item name="loading_layout">@layout/loading_layout</item>
    <item name="none_content">@layout/none_layout</item>
    <item name="network_content">@layout/connectionless_layout</item>
    <item name="failed_content">@layout/failed_layout</item>
  </style>
```

**函数使用**

通过调用不同的方法，来展示对应的页面，如`.showLoading();`、`.showContent()`等。或者为“错误页面”添加点击重试事件：

```java
  progressLayout.showNetError(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*重试逻辑*/
            }
        });
```



**Gradle**

在项目根目录下的`build.gradle`中添加如下代码：
```java
 allprojects {
    repositories {
      ...
      maven { url "https://jitpack.io" }
    }
  }
```

在module的`build.gradle`中添加最新版本库的依赖：
```java
 dependencies {
          compile 'com.github.SmartDengg:ProgressLayout:x.y.z'
  }
```

##截图
-----------------
![](https://github.com/SmartDengg/ProgressLayout/blob/master/images/progresslayout.gif)

##Developed By
-----------------

- 小鄧子 - Hi4Joker@gmail.com

<a href="http://homelinkcn.github.io/"><img alt="Follow me on Weibo" src="https://github.com/SmartDengg/ProgressLayout/blob/master/images/homelink.png" />
</a>
















