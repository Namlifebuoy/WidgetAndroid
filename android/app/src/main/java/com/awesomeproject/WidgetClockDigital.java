package com.awesomeproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SizeF;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetClockDigital extends AppWidgetProvider {
    private static final String TAG = "WidgetClockDigital";
    private static final String DEFAULT_FONT_NAME = "BlackOpsOne.ttf";
    private static final int UPDATE_INTERVAL_MS =  1000;
    private static String fontName = DEFAULT_FONT_NAME;
    static Context newContext;
    static AppWidgetManager newAppWidgetManager;
    static ArrayList<Integer> newAppWidgetIds=new ArrayList<>();

    public static void setAlarm(Context context, int[] appWidgetIds) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetClockDigital.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetIds[0]});
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), UPDATE_INTERVAL_MS, pendingIntent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        newContext = context;
        newAppWidgetManager = appWidgetManager;
        newAppWidgetIds.add(appWidgetIds[0]);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Log.d(TAG, "onUpdate: " + appWidgetId);
        }
        setAlarm(context, appWidgetIds);
    }

    @Override
    public void onAppWidgetOptionsChanged(
            Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        // Get the new sizes.
        ArrayList<SizeF> sizes =
                newOptions.getParcelableArrayList(AppWidgetManager.OPTION_APPWIDGET_SIZES);
        // Check that the list of sizes is provided by the launcher.
        if (sizes == null || sizes.isEmpty()) {
            return;
        }
        // Map the sizes to the RemoteViews that you want.
        Map<SizeF, RemoteViews> viewMapping = new ArrayMap<>();
        for (SizeF size : sizes) {
            viewMapping.put(size, createRemoteViews(size));
        }
        RemoteViews remoteViews = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            remoteViews = new RemoteViews(viewMapping);
        }
//        appWidgetManager.updateAppWidget(id, remoteViews);
    }

    private RemoteViews createRemoteViews(SizeF size) {
        Log.d(TAG, "createRemoteViews: " + size);
        return null;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetClockDigital.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // Enter relevant functionality for when the last widget is disabled
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, WidgetClockDigital.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }


    public static void setFont(String fontName) {
        WidgetClockDigital.fontName = fontName;
        for (int appWidgetId : newAppWidgetIds) {
            updateAppWidget(newContext, newAppWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String currentTime = timeFormat.format(calendar.getTime());

        // Get the current date
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d");
        String currentDay = dayFormat.format(calendar.getTime());
        String currentDate = dateFormat.format(calendar.getTime());

        // Đọc ảnh từ tệp tin
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_a, bitmapOptions);
//        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.widget_a);



        // Đặt RoundedBitmapDrawable cho ImageView với id là @+id/imgbackground
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_clock_digital);
        views.setImageViewBitmap(R.id.imgday, buildUpdate(currentDay, 60, context, Paint.Align.LEFT));
        views.setImageViewBitmap(R.id.imgdate, buildUpdate(currentDate, 35, context, Paint.Align.LEFT));
        views.setImageViewBitmap(R.id.imgtime, buildUpdate(currentTime, 50, context, Paint.Align.LEFT));
        views.setImageViewBitmap(R.id.imgbackground, getRoundedCornerBitmap(bitmap, 30));

        // Log the size of the ImageView with id R.id.imgbackground
        ImageView imageView = views.apply(context, null).findViewById(R.id.imgbackground);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
//        int widthPx = drawable.getBitmap().getWidth();
//        int heightPx = drawable.getBitmap().getHeight();
//        float density = context.getResources().getDisplayMetrics().density;
//        int widthDp = (int) (widthPx / density);
//        int heightDp = (int) (heightPx / density);
//        Log.d(TAG, "Size of imgbackground_px: " + drawable.getBitmap().getWidth() + " x " + drawable.getBitmap().getHeight());
//        Log.d(TAG, "Size of imgbackground_dp: " + widthDp + " x " + heightDp);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

//    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
//                .getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        final float roundPx = pixels;
//
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//
//        return output;
//    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float dpRadius) {
        // Chuyển đổi giá trị dp thành pixel
        float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpRadius, Resources.getSystem().getDisplayMetrics());

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private static Bitmap buildUpdate(String text, int size, Context context, Paint.Align alignItem) {
        Log.d(TAG, "buildUpdate: " + "fonts/" + fontName);
        Paint paint = new Paint();
        paint.setTextSize(size);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
        paint.setTypeface(typeface);
        paint.setColor(Color.parseColor("#00fff8"));
        paint.setTextAlign(alignItem);
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        float baseline = -paint.ascent();
        int width = (int) (paint.measureText(text) + 2f);
        int height = (int) (baseline + paint.descent() + 2f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

}




