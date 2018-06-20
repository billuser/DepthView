[![](https://jitpack.io/v/billuser/DepthView.svg)](https://jitpack.io/#billuser/DepthView)

# DepthView
虛擬貨幣深度圖<br>

## 導入專案<br>
#### Add it in your root build.gradle at the end of repositories: <br>

```
allprojects {
  repositories {
	...
	maven { url 'https://jitpack.io' }
  }
}
```
  <br>
  <br>
  
  
#### Add the dependency <br>
```
dependencies {
       implementation 'com.github.billuser:DepthView:v1.3'
}
```

## Usage
<br>


```
//listDepthBuy 買單資料
//listDepthSell 賣單資料

MergeDepthView mergeDepthView = new MergeDepthView(this);
mergeDepthView.setMdata(listDepthBuy, listDepthSell); //加入深度圖資料

```
<br>

## 參數設定
<br>

```
mergeDepthView.setDepthBg("#ffffff");  //深度圖背景顏色
mergeDepthView.setYaxisTextColor("#ffffff"); //深度圖 y軸文字顏色
mergeDepthView.setXaxisTextColor("#ffffff"); //深度圖 X軸文字顏色
mergeDepthView.setYaxisTextSize(20); //深度圖 y軸文字大小
mergeDepthView.setXaxisTextSize(20); //深度圖 X軸文字大小
mergeDepthView.setDepthViewHeigh(500); //深度圖高度

```

![](https://github.com/billuser/DepthView/blob/master/photo/Webp.net-gifmaker.gif)
