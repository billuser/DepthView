package com.be.you.depthchart.depthchart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.be.you.depthchart.MyBigDecimal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MergeDepthView extends View {
    private int width;
    private int heigh;

    //网格的宽度与高度
    private int gridspace_width;
    private int gridspace_heigh;
    //底部空白的高度
    private int brokenline_bottom;
    //灰色背景的画笔
    private Paint mPaint_bg;
    //灰色网格的画笔
    private Paint mPaint_gridline;

    private Paint mPaint_gridText;
    //文本数据的画笔
    private Paint mPaint_text;
    //橫線數量
    private int lineCount = 4;

    //折线圆点的蓝色背景
    private Paint mPaint_point_bg;
    //折线圆点的白色表面
    private Paint mPaint_point_sur;
    //阴影路径的画笔
    private Paint mPaint_path;
    private Paint mPaint_path_buy;
    //折线的画笔
    private Paint mPaint_brokenlineBuy;
    private Paint mPaint_brokenlineSell;

    //路径
    private Path mpath = new Path();
    private Path mpathBuy = new Path();
    //客户拜访的折线（BrokenLineCusVisit）数据
    private List<DepthDataObject> dataBuy;
    private List<DepthDataObject> dataSell;

    private final int count = 10;
    private int max_value = 11000;
    private HashMap<Float, Float> mapX;
    private HashMap<Float, Float> mapY;
    private Canvas mCanvas;
    private List<DepthDataObject> priceSortBuy;
    private List<DepthDataObject> priceSortSell;
    private List<Float> buyPrice;
    private  GestureDetector gestureDetector;
    private float maxVolume;
    private Context mContext;
    private int spacingSell = 0;
    private Paint beelinePaint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public void setGridspace_width(int gridspace_width) {
        this.gridspace_width = gridspace_width;
    }

    public MergeDepthView(Context context) {
        super(context);
        mContext = context;
        init(context);

    }

    public MergeDepthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public MergeDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    public MergeDepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(context);
    }

    /**
     * 設定y軸文字顏色
     * @param color
     */
    public void setYaxisTextColor(String color) {
        mPaint_gridText.setColor(Color.parseColor(color));
    }

    /**
     * 設定y軸文字大小
     * @param size
     */
    public void setYaxisTextSize(float size){
        mPaint_gridText.setTextSize(size);
    }

    /**
     * 設定深度圖高度
     * @param mHeigh
     */
    public void setDepthViewHeigh(int mHeigh){
        heigh = mHeigh;
    }

    /**
     * 設定X軸文字顏色
     * @param color
     */
    public void setXaxisTextColor(String color){
        mPaint_text.setColor(Color.parseColor(color));
    }

    /**
     * 設定X軸文字大小
     * @param size
     */
    public void setXaxisTextSize(float size){
        mPaint_text.setTextSize(size);
    }

    /**
     * 設定Y軸線顏色
     * @param color
     */
    public void setYaxisLineColor(String color){
        mPaint_gridline.setColor(Color.parseColor(color));
    }



    private void init(Context context) {
        mapX = new HashMap<>();
        mapY = new HashMap<>();
        mPaint_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_bg.setColor(Color.argb(0xff,0xef,0xef,0xef));
        mPaint_gridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_gridline.setColor(Color.argb(0xff,0xce,0xCB,0xce));
        beelinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        beelinePaint.setColor(Color.argb(0x00,0x00,0x00,0x00));
        mPaint_gridText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_gridText.setColor(Color.parseColor("#000000"));
        mPaint_gridText.setTextSize(18);
        mPaint_brokenlineBuy = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_brokenlineBuy.setColor(Color.parseColor("#5570a800"));
        mPaint_brokenlineBuy.setTextSize(18);
        mPaint_brokenlineBuy.setTextAlign(Paint.Align.CENTER);
        mPaint_brokenlineSell = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_brokenlineSell.setColor(Color.parseColor("#55ff6c78"));
        mPaint_brokenlineSell.setTextSize(18);
        mPaint_brokenlineSell.setTextAlign(Paint.Align.CENTER);
        mPaint_point_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_point_bg.setColor(Color.argb(0xff, 0x91, 0xC8, 0xD6));
        //注意path的画笔的透明度已经改变了
        mPaint_path = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_path.setColor(Color.parseColor("#55ffa0a8"));
        mPaint_path_buy = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_path_buy.setColor(Color.parseColor("#55bcd688"));
        mPaint_point_sur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_point_sur.setColor(Color.WHITE);
        mPaint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_text.setColor(Color.BLACK);
        mPaint_text.setTextAlign(Paint.Align.CENTER);
        mPaint_text.setTextSize(18);
        invalidate();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {

                for(float key : mapX.keySet()){
                    if(key < e.getX() && key + 50 > e.getX()){

//                   mCanvas.drawText(String.valueOf(mapX.get(key)),
//                           key,
//                           (heigh - brokenline_bottom - (heigh - brokenline_bottom) * mapX.get(key)/max_value - mPaint_brokenline.measureText(String.valueOf(mapX.get(key)))) + 65,
//                           mPaint_brokenline);

                        requestLayout();
                        invalidate();

//                   mCanvas.restore();

                    }
                }

            }
        });
    }

    public void setDepthBg(String color){
        mPaint_bg.setColor(Color.parseColor(color));
    }


    public void setMdata(List<DepthDataObject> mdataBuy, List<DepthDataObject> mdataSell) {
        buyPrice = new ArrayList<>();
        priceSortBuy = mdataBuy;
        dataBuy = new ArrayList<>();

        //买的数据不为空的时候才进行处理，否则会报空指针
        if(priceSortBuy.size()>0){
            Collections.sort(priceSortBuy, new comparePriceBuy()); //價格排序由低至高
            float fPrice = priceSortBuy.get(0).getPrice();
            for(int i = 0; i < 3; i++){
                switch (i){
                    case 0:
                        fPrice = priceSortBuy.get(0).getPrice() * 0.9f;
                        break;
                    case 1:
                        fPrice = priceSortBuy.get(0).getPrice() * 0.96f;
                        break;
                    case 2:
                        fPrice = priceSortBuy.get(0).getPrice();
                        break;
                }

                buyPrice.add(fPrice);
            }


            //加總數量
            float vol = 0;
            ArrayList<Float> arrVolume = new ArrayList<>();
            for(int i = -1; i <= mdataBuy.size() - 1; i++){
                DepthDataObject obj = new DepthDataObject();
                if(i == mdataBuy.size() - 1){
                    vol += mdataBuy.get(i).getVolume();
                }else{
                    vol += mdataBuy.get(i + 1).getVolume();
                }

                obj.setVolume(vol);
//            obj.setPrice(mdataBuy.get(i + 1).getPrice());
                arrVolume.add(vol);
                LogUtils.e("vol---->",vol+"");
                dataBuy.add(obj);
            }

            Collections.sort(dataBuy, new compare());
            //調整圖表高度
            maxVolume = Collections.max(arrVolume);
            String count = String.valueOf((int)maxVolume);
            max_value = (int)maxVolume;
        }



//

        priceSortSell = mdataSell;
        dataSell = new ArrayList<>();
        //卖的数据不为空的时候才进行处理，否则会报空指针

        if(priceSortSell.size()>0){
            Collections.sort(priceSortSell, new comparePriceSell());
            float fsPrice = priceSortSell.get(0).getPrice();
            for(int i = 0; i < 3; i++){
                switch (i){
                    case 0:
                        fsPrice = priceSortSell.get(0).getPrice();
                        break;
                    case 1:
                        fsPrice = priceSortSell.get(0).getPrice() * 1.04f;
                        break;
                    case 2:
                        fsPrice = priceSortSell.get(0).getPrice() * 1.09f;
                        break;
                }
                buyPrice.add(fsPrice);
            }

        }

        //卖的数据不为空的时候才进行处理，否则会报空指针

        if(mdataSell.size()>0){
            float volsell = 0;
            ArrayList<Float> arrSellVolume = new ArrayList<>();
            for(int i = -1; i <= mdataSell.size() - 1; i++){
                DepthDataObject obj = new DepthDataObject();
                if(i == mdataSell.size() - 1){

                    volsell += mdataSell.get(i).getVolume();
                }else{
                    volsell += mdataSell.get(i + 1).getVolume();
                }
                obj.setVolume(volsell);
                arrSellVolume.add(volsell);
                dataSell.add(obj);
            }
            Collections.sort(dataSell, new compareSell());
            if(maxVolume < Collections.max(arrSellVolume)){
                maxVolume = Collections.max(arrSellVolume);
                max_value = (int)maxVolume;
            }
        }

        requestLayout();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.save();
        canvas.drawColor(Color.parseColor("#172432"));
        canvas.drawRect(0,0,width,heigh - brokenline_bottom, mPaint_bg);
        float multiple = maxVolume / lineCount;
        float value = maxVolume;
        for (int j = 0;j < lineCount; j++){
            //橫線
            canvas.drawLine(0,gridspace_heigh * (j + 1), width
                    ,gridspace_heigh * (j + 1), mPaint_gridline);

            if(j == 0){
                value = maxVolume - multiple;
            }else if(j == (lineCount -1)){
                value = 0;
            }else{
                value -= multiple;
            }

            String xPos=String.valueOf(value).replace(".","");
            int postion=(xPos.length())*10;

            canvas.drawText(MyBigDecimal.getValue(String.valueOf(value)), postion
                    ,gridspace_heigh * (j + 1), mPaint_gridText);

        }

//        for (int j = 0;j < lineCount; j++){
//            //橫線
//            canvas.drawLine(0,gridspace_heigh * (j + 1), width
//                    ,gridspace_heigh * (j + 1), mPaint_gridline);
//
//            if(j == 0){
//                value = maxVolume - multiple;
//            }else if(j == (lineCount -1)){
//                value = 0;
//            }else{
//                value -= multiple;
//            }
//            String xPos = String.valueOf(value).replace(".","");
//            int position = xPos.length() * 10;
//            canvas.drawText(MyBigDecimal.getValue(String.valueOf(value)), position
//                    ,gridspace_heigh * (j + 1), mPaint_gridText);
//
//        }


//        canvas.drawText(MyBigDecimal.getValue(String.valueOf(value)),postion,gridspace_heigh*(j+1));

        displayBuy(canvas);
        displaySell(canvas);
        canvas.restore();
    }


    /**
     * 顯示掛買折線圖
     */
    private void displayBuy(Canvas canvas){
        mpathBuy.reset();
        for (int i = 0; i < dataBuy.size(); i++){

            if (i == 0){
                mpathBuy.moveTo(gridspace_width * i + count,
                        (heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i).getVolume() / max_value));
            }

            //直線
            canvas.drawLine(gridspace_width * i + count, 0, gridspace_width * i + count
                    , heigh - brokenline_bottom, beelinePaint);

            if (i != dataBuy.size() - 1){ //深度圖數組繪製


                canvas.drawLine(gridspace_width * i + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i).getVolume() / max_value,
                        gridspace_width * (i + 1) + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i + 1).getVolume() / max_value,
                        mPaint_brokenlineBuy);

                mpathBuy.quadTo(gridspace_width * i + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i).getVolume() / max_value,
                        gridspace_width * (i + 1) + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i + 1).getVolume() / max_value);
            }

            float x = gridspace_width * i + count;
            float y = heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i).getVolume() / max_value;
            mapY.put(y, dataBuy.get(i).getVolume());
            mapX.put(x, dataBuy.get(i).getVolume());
            //x軸數值顯示
            String data = String.valueOf(buyPrice.get(0));
            if(i == 1 || i == (dataBuy.size() - 1) || (i == (dataBuy.size() - 1)/2)){
                if(i == 0){
                    data = String.valueOf(buyPrice.get(0));
                }else if(i == ((dataBuy.size() - 1)/2)){
                    data = String.valueOf(buyPrice.get(1));
                }else if(i == (dataBuy.size() - 1)){
                    data = String.valueOf(buyPrice.get(2));
                }
                if(i == (dataBuy.size() - 1)){
                    canvas.drawText(data,
                            gridspace_width * i + count - 50,
                            heigh - brokenline_bottom / 2,
                            mPaint_text);
                }else if(i == 1){
                    canvas.drawText(data,
                            gridspace_width * i + count + 50,
                            heigh - brokenline_bottom / 2,
                            mPaint_text);
                }else{
                    canvas.drawText(data,
                            gridspace_width * i + count,
                            heigh - brokenline_bottom / 2,
                            mPaint_text);
                }

            }

            if (i == dataBuy.size() - 1){
                mpathBuy.quadTo(gridspace_width * i + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataBuy.get(i).getVolume() / max_value,
                        gridspace_width * i + count,
                        heigh - brokenline_bottom);

                mpathBuy.quadTo(gridspace_width * i + count,
                        heigh - brokenline_bottom ,
                        count,
                        heigh - brokenline_bottom);
                mpathBuy.close();
            }
            spacingSell = gridspace_width * i + count;
//            spacingSell = 0;

        }
        canvas.drawPath(mpathBuy,mPaint_path_buy);
    }

    /**
     * 顯示掛賣折線圖
     */
    private void displaySell(Canvas canvas){
        mpath.reset();
        for (int i = 0; i < dataSell.size(); i++){

            if (i == 0){
//                mpath.moveTo((gridspace_width * i + count) + spacingSell,
//                        (heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i).getVolume() / max_value));
                mpath.moveTo((gridspace_width * i + count) + spacingSell,
                        heigh - 50);

            }

            //直線
            canvas.drawLine((gridspace_width * i + count) + spacingSell, 0, (gridspace_width * i + count) + spacingSell
                    , heigh - brokenline_bottom, beelinePaint);

            if (i != dataSell.size() - 1){ //深度圖數組繪製

                canvas.drawLine((gridspace_width * i + count) + spacingSell,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i).getVolume() / max_value,
                        (gridspace_width * (i + 1) + count) + spacingSell,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i + 1).getVolume() / max_value,
                        mPaint_brokenlineSell);

                mpath.quadTo((gridspace_width * i + count) + spacingSell,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i).getVolume() / max_value,
                        (gridspace_width * (i + 1) + count) + spacingSell,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i + 1).getVolume() / max_value);
            }

            float x = (gridspace_width * i + count) + spacingSell;
            float y = heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i).getVolume() / max_value;
            mapY.put(y, dataSell.get(i).getVolume());
            mapX.put(x, dataSell.get(i).getVolume());

            String data = String.valueOf(buyPrice.get(0));
            if(i == 0 || i == (dataSell.size() - 1) || (i == (dataSell.size() - 1)/2)){
                if(i == ((dataSell.size() - 1)/2)){
                    //判断是否有这么多数据，否则会报数组下标越界
                    if(buyPrice.size()>5){
                        data = String.valueOf(buyPrice.get(4));

                    }
                }else if(i == (dataSell.size() - 1)){
                    //判断是否有这么多数据，否则会报数组下标越界
                    if(buyPrice.size()>6){
                        data = String.valueOf(buyPrice.get(5));
                    }

                }else if(i == 0){
                    //判断是否有这么多数据，否则会报数组下标越界
                    if(buyPrice.size()>4){
                        data = String.valueOf(buyPrice.get(3));
                    }
                }
                if(i == (dataSell.size() - 1)){
                    canvas.drawText(data,
                            (gridspace_width * i + count - 50) + spacingSell,
                            heigh - brokenline_bottom / 2,
                            mPaint_text);
                }else if(i == 0){
                    canvas.drawText(data,
                            (gridspace_width * i + count + 50) + spacingSell,
                            heigh - brokenline_bottom / 2,
                            mPaint_text);
                }else{
                    canvas.drawText(data,
                            (gridspace_width * i + count) + spacingSell,
                            heigh - brokenline_bottom / 2,
                            mPaint_text);
                }
            }

            if (i == dataSell.size() - 1){
                mpath.quadTo((gridspace_width * i + count) + spacingSell,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * dataSell.get(i).getVolume() / max_value,
                        (gridspace_width * i + count) + spacingSell,
                        heigh - brokenline_bottom );

                mpath.quadTo((gridspace_width * i + count) + spacingSell,
                        heigh - brokenline_bottom ,
                        count,
                        heigh - brokenline_bottom);
                mpath.close();
            }
        }

        canvas.drawPath(mpath, mPaint_path);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        DisplayMetrics monitorsize = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(monitorsize);

        gridspace_width = (width / (dataBuy.size() + dataSell.size()));
        if(dataBuy.size() == 0){
            width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        }else{
            //根据数据的条数设置宽度
//            width = gridspace_width * dataBuy.size() + gridspace_width * dataSell.size()+ 25;
            width = monitorsize.widthPixels;
        }
        if(heigh == 0){
            heigh = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        }

        brokenline_bottom = 50;
        gridspace_heigh = (heigh - brokenline_bottom)/lineCount;
        setMeasuredDimension(width, heigh);
    }

    public class compare implements Comparator<DepthDataObject> {
        @Override
        public int compare(DepthDataObject o1, DepthDataObject o2) {
            float str1 = o1.getVolume();
            float str2 = o2.getVolume();
            if(str1 < str2)
            {
                return 1;
            }else
            if(str1 > str2)
            {
                return -1;
            }else
            {
                return 0;
            }
        }
    }

    public class compareSell implements Comparator<DepthDataObject> {
        @Override
        public int compare(DepthDataObject o1, DepthDataObject o2) {
            float str1 = o1.getVolume();
            float str2 = o2.getVolume();
            if(str1 > str2)
            {
                return 1;
            }else
            if(str1 < str2)
            {
                return -1;
            }else
            {
                return 0;
            }
        }
    }


    public class comparePriceBuy implements Comparator<DepthDataObject> {
        @Override
        public int compare(DepthDataObject o1, DepthDataObject o2) {
            float str1 = o1.getPrice();
            float str2 = o2.getPrice();
            if(str1 > str2)
            {
                return 1;
            }else
            if(str1 < str2)
            {
                return -1;
            }else
            {
                return 0;
            }
        }
    }


    public class comparePriceBuyb implements Comparator<DepthDataObject> {
        @Override
        public int compare(DepthDataObject o1, DepthDataObject o2) {
            float str1 = o1.getPrice();
            float str2 = o2.getPrice();
            if(str1 < str2)
            {
                return 1;
            }else
            if(str1 > str2)
            {
                return -1;
            }else
            {
                return 0;
            }
        }
    }

    public class comparePriceSell implements Comparator<DepthDataObject> {
        @Override
        public int compare(DepthDataObject o1, DepthDataObject o2) {
            float str1 = o1.getPrice();
            float str2 = o2.getPrice();
            if(str1 > str2)
            {
                return 1;
            }else
            if(str1 < str2)
            {
                return -1;
            }else
            {
                return 0;
            }
        }
    }
}
