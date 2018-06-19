package com.be.you.depthchart.depthchart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.be.you.depthchart.MyBigDecimal;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class DepthView extends View {
    private int width;
    private int heigh = 200;

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
    private Paint mPaint_brokenline;
    //路径
    private Path mpath = new Path();
    //客户拜访的折线（BrokenLineCusVisit）数据
    private List<DepthDataObject> mdata;

    private final int count = 10;
    private int max_value = 11000;
    private boolean isBuy;
    private HashMap<Float, Float> mapX;
    private HashMap<Float, Float> mapY;
    private Canvas mCanvas;
    private List<DepthDataObject> priceSort;
    private List<Float> buyPrice;
    private  GestureDetector gestureDetector;
    private float maxVolume;
    private Context mContext;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public DepthView(Context context, boolean isbuy) {
        super(context);
        mContext = context;
        isBuy = isbuy;
        init(context);

    }

    public DepthView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    public DepthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        mapX = new HashMap<>();
        mapY = new HashMap<>();
        mPaint_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_bg.setColor(Color.argb(0xff,0xef,0xef,0xef));

        mPaint_gridline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_gridline.setColor(Color.argb(0xff,0xce,0xCB,0xce));

        mPaint_gridText = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_gridText.setColor(Color.parseColor("#000000"));

        mPaint_brokenline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_brokenline.setColor(Color.argb(0xff,0x91,0xC8,0xD6));
        mPaint_brokenline.setTextSize(18);
        mPaint_brokenline.setTextAlign(Paint.Align.CENTER);

        mPaint_point_bg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_point_bg.setColor(Color.argb(0xff, 0x91, 0xC8, 0xD6));
        //注意path的画笔的透明度已经改变了

        mPaint_path = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_path.setColor(Color.argb(0x33,0x91,0xC8,0xD6));
        mPaint_path_buy = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_path_buy.setColor(Color.parseColor("#5500ff00"));

        mPaint_point_sur = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_point_sur.setColor(Color.WHITE);

        mPaint_text = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint_text.setColor(Color.BLACK);
        mPaint_text.setTextAlign(Paint.Align.CENTER);

        invalidate();
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {

                for(float key : mapX.keySet()){
                    if(key < e.getX() && key + 50 > e.getX()){

//                   Log.e("bbbbcc", " " + mapX.get(key) + " mCanvas " +mCanvas);

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

    public List<DepthDataObject> getMdata() {
        return mdata;
    }

    public void setMdata(List<DepthDataObject> mdata) {
//        this.mdata = mdata;

        this.mdata = new ArrayList<>();
        priceSort = mdata;
        buyPrice = new ArrayList<>();
        if(isBuy){
            Collections.sort(priceSort, new comparePriceBuy()); //價格排序由低至高
            float fPrice = priceSort.get(0).getPrice();
            for(int i = 0; i < 3; i++){
                switch (i){
                    case 0:
                        fPrice = priceSort.get(0).getPrice() * 0.9f;
                        break;
                    case 1:
                        fPrice = priceSort.get(0).getPrice() * 0.96f;
                        break;
                    case 2:
                        fPrice = priceSort.get(0).getPrice();
                        break;
                }

                buyPrice.add(fPrice);
            }
            //加總數量
            float vol = 0;
            ArrayList<Float> arrVolume = new ArrayList<>();
            for(int i = 0; i <= mdata.size() - 1; i++){
//                float vol = 0;
                DepthDataObject obj = new DepthDataObject();
                if(i == mdata.size() - 1){
                    vol += mdata.get(i).getVolume();
                }else{
                    vol += mdata.get(i + 1).getVolume();
                }
                obj.setVolume(vol);
                obj.setPrice(mdata.get(i).getPrice());
                arrVolume.add(vol);
                this.mdata.add(obj);

            }
            Collections.sort(this.mdata, new compare());

            //調整圖表高度
            maxVolume = Collections.max(arrVolume);
            String count = String.valueOf((int)maxVolume);
//            max_value = (int)maxVolume + (int)Math.pow(10, (count.length() - 1));
            max_value = (int)maxVolume + 1;

        }else{
//            this.mdata = mdata;
            Collections.sort(priceSort, new comparePriceSell());
            float fPrice = priceSort.get(0).getPrice();
            for(int i = 0; i < 3; i++){
                switch (i){
                    case 0:
                        fPrice = priceSort.get(0).getPrice();
                        break;
                    case 1:
                        fPrice = priceSort.get(0).getPrice() * 1.04f;
                        break;
                    case 2:
                        fPrice = priceSort.get(0).getPrice() * 1.09f;
                        break;
                }
                buyPrice.add(fPrice);
            }

            float vol = 0;
            ArrayList<Float> arrVolume = new ArrayList<>();
            for(int i = 0; i <= mdata.size() - 1; i++){
                DepthDataObject obj = new DepthDataObject();
                if(i == mdata.size() - 1){
                    vol += mdata.get(i).getVolume();
                }else{
                    vol += mdata.get(i + 1).getVolume();
                }
                obj.setVolume(vol);
                obj.setPrice(mdata.get(i).getPrice());
                arrVolume.add(vol);
                this.mdata.add(obj);
            }

            Log.e("asas", "vv " +mdata.get(0).getVolume());
            Log.e("asas", "ccc " +mdata.get(mdata.size() - 1).getVolume());

            Collections.sort(mdata, new compareSell());

            Log.e("asas", "fff " +mdata.get(0).getVolume());
            Log.e("asas", "hh " +mdata.get(mdata.size() - 1).getVolume());


            maxVolume = Collections.max(arrVolume);
            max_value = (int)maxVolume + 1;
        }

        requestLayout();
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        canvas.drawColor(Color.WHITE);
        canvas.drawRect(0,0,width,heigh - brokenline_bottom, mPaint_bg);

        float multiple = maxVolume / lineCount;
        float value = maxVolume;

        for (int j = 0;j < lineCount; j++){
            //橫線
            canvas.drawLine(0,gridspace_heigh * (j + 1), width
                    ,gridspace_heigh * (j + 1), mPaint_gridline);

            if(isBuy){
                if(j == 0){
                    value = maxVolume;
                }else if(j == (lineCount -1)){
                    value = 0;
                }else{
                    value -= multiple;
                }
                canvas.drawText(MyBigDecimal.getValue(String.valueOf(value)), 0
                        ,gridspace_heigh * (j + 1), mPaint_gridText);
            }
        }

        for (int i = 0; i < mdata.size(); i++){

            if (i == 0){
                mpath.moveTo(gridspace_width * i + count,
                        (heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getVolume() / max_value));
            }

            //直線
            canvas.drawLine(gridspace_width * i + count, 0, gridspace_width * i + count
                    , heigh - brokenline_bottom, mPaint_gridline);

            if (i != mdata.size() - 1){ //深度圖數組繪製

                canvas.drawLine(gridspace_width * i + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getVolume() / max_value,
                        gridspace_width * (i + 1) + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i + 1).getVolume() / max_value,
                        mPaint_brokenline);

                mpath.quadTo(gridspace_width * i + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getVolume() / max_value,
                        gridspace_width * (i + 1) + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i + 1).getVolume() / max_value);
            }

            float x = gridspace_width * i + count;
            float y = heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getVolume() / max_value;
            mapY.put(y, mdata.get(i).getVolume());
            mapX.put(x, mdata.get(i).getVolume());

//            canvas.drawText(data,
//                    gridspace_width * i + count,
//                    heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getVolume()/max_value - mPaint_brokenline.measureText(data),
//                    mPaint_brokenline);

            String date = MyBigDecimal.getValue(String.valueOf(mdata.get(i).getPrice()));

            if(isBuy){ //x軸數值顯示
                String data = String.valueOf(buyPrice.get(0));
                if(i == 1 || i == (mdata.size() - 1) || (i == (mdata.size() - 1)/2)){
                    if(i == 0){
                        data = String.valueOf(buyPrice.get(0));
                    }else if(i == ((mdata.size() - 1)/2)){
                        data = String.valueOf(buyPrice.get(1));
                    }else if(i == (mdata.size() - 1)){
                        data = String.valueOf(buyPrice.get(2));
                    }
                    if(i == (mdata.size() - 1)){
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
            }else{
                String data = String.valueOf(buyPrice.get(0));
                if(i == 1 || i == (mdata.size() - 1) || (i == (mdata.size() - 1)/2)){
                    if(i == ((mdata.size() - 1)/2)){
                        data = String.valueOf(buyPrice.get(2));
                    }else if(i == (mdata.size() - 1)){
                        data = String.valueOf(buyPrice.get(1));
                    }else if(i == 0){
                        data = String.valueOf(buyPrice.get(0));
                    }
                    if(i == (mdata.size() - 1)){
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
            }


            if (i == mdata.size() - 1){
                mpath.quadTo(gridspace_width * i + count,
                        heigh - brokenline_bottom - (heigh - brokenline_bottom) * mdata.get(i).getVolume() / max_value,
                        gridspace_width * i + count,
                        heigh - brokenline_bottom );

                mpath.quadTo(gridspace_width * i + count,
                        heigh - brokenline_bottom ,
                        count,
                        heigh - brokenline_bottom);
                mpath.close();
            }
        }
        if(isBuy){
            canvas.drawPath(mpath,mPaint_path_buy);
        }else{
            canvas.drawPath(mpath,mPaint_path);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
        int widthScreen = displayMetrics.widthPixels;

        gridspace_width = 3;
        if(mdata.size() == 0){
            width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);

        }
        else{
            //根据数据的条数设置宽度
            width = gridspace_width * mdata.size();
//            width = widthScreen / 2;

        }

        Log.e("vvb", "width " +width);
        Log.e("vvb", "widthScreen " +widthScreen);

        heigh = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        brokenline_bottom = 50;

        gridspace_heigh = (heigh-brokenline_bottom)/lineCount;
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
