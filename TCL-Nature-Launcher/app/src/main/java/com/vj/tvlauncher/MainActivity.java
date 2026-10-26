package com.vj.tvlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import org.json.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

/** Nature TV Home 0.1.0: simple local-first TV launcher, no analytics. */
public class MainActivity extends Activity {
  final Handler main = new Handler(Looper.getMainLooper());
  LinearLayout root, dock; TextView clock, date, greeting, weather;
  String city; String[] names={"Netflix","YouTube","Plex","Prime Video","Disney+","Live TV","HDMI 1","Settings"};
  String[] packages={"com.netflix.ninja","com.google.android.youtube.tv","com.plexapp.android","com.amazon.amazonvideo.livingroom","com.disney.disneyplus","","",""};
  int selected=0; final ArrayList<View> tiles=new ArrayList<>();
  final Runnable ticker=new Runnable(){public void run(){refreshClock();main.postDelayed(this, 1000);}};
  @Override public void onCreate(Bundle b){super.onCreate(b); getWindow().getDecorView().setSystemUiVisibility(5894|1024|512); city=getPreferences(0).getString("city","Breda");buildUi();ticker.run();fetchWeather();}
  @Override protected void onResume(){super.onResume();refreshClock();}
  @Override protected void onDestroy(){main.removeCallbacks(ticker);super.onDestroy();}
  int dp(float n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
  TextView label(String s,int sp,boolean heavy){TextView v=new TextView(this); v.setText(s);v.setTextColor(0xffffffff);v.setTextSize(sp); if(heavy)v.setTypeface(null,1);v.setShadowLayer(dp(3),1,2,0xff000000);return v;}
  GradientDrawable panel(int bg,int stroke){GradientDrawable d=new GradientDrawable();d.setColor(bg);d.setCornerRadius(dp(13));d.setStroke(dp(2),stroke);return d;}
  void buildUi(){
    FrameLayout base=new FrameLayout(this);setContentView(base);
    try { Bitmap img=BitmapFactory.decodeStream(getAssets().open("nature.jpg"));ImageView background=new ImageView(this); background.setImageBitmap(img);background.setScaleType(ImageView.ScaleType.CENTER_CROP);base.addView(background,new FrameLayout.LayoutParams(-1,-1)); }catch(Exception ignored){}
    View scrim=new View(this);scrim.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0x88001422,0x08000000,0xAA00101B}));base.addView(scrim,new FrameLayout.LayoutParams(-1,-1));
    LinearLayout left=new LinearLayout(this);left.setOrientation(1);left.setPadding(dp(45),dp(28),dp(12),0);
    FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(dp(530),-2,Gravity.TOP|Gravity.LEFT);base.addView(left,lp);
    TextView brand=label("NATURE  |  TV HOME",18,true);left.addView(brand);
    greeting=label("Good evening",18,false);LinearLayout.LayoutParams g=new LinearLayout.LayoutParams(-2,-2);g.topMargin=dp(24);left.addView(greeting,g);
    clock=label("00:00",76,true);left.addView(clock);
    date=label("",19,false);left.addView(date);
    weather=label(city+"  ·  Weather loading…",21,false);weather.setGravity(Gravity.RIGHT);
    FrameLayout.LayoutParams wp=new FrameLayout.LayoutParams(dp(460),-2,Gravity.TOP|Gravity.RIGHT);wp.setMargins(0,dp(36),dp(45),0);base.addView(weather,wp);
    HorizontalScrollView scroll=new HorizontalScrollView(this);scroll.setHorizontalScrollBarEnabled(false);scroll.setClipToPadding(false);scroll.setPadding(dp(28),dp(10),dp(28),dp(12));
    FrameLayout.LayoutParams sp=new FrameLayout.LayoutParams(-1,dp(156),Gravity.BOTTOM);sp.setMargins(dp(18),0,dp(18),dp(36));base.addView(scroll,sp);
    dock=new LinearLayout(this);dock.setGravity(Gravity.CENTER);dock.setOrientation(LinearLayout.HORIZONTAL);scroll.addView(dock,new android.widget.FrameLayout.LayoutParams(-1,-1));
    for(int i=0;i<names.length;i++){final int index=i;LinearLayout wrap=new LinearLayout(this);wrap.setOrientation(1);wrap.setGravity(Gravity.CENTER);wrap.setFocusable(true);wrap.setFocusableInTouchMode(true);
      TextView icon=label(symbol(i),26,true);icon.setGravity(Gravity.CENTER);icon.setBackground(panel(0xD929394E,0x22FFFFFF));LinearLayout.LayoutParams ip=new LinearLayout.LayoutParams(dp(125),dp(79));wrap.addView(icon,ip);
      TextView t=label(names[i],13,false);t.setGravity(Gravity.CENTER); t.setSingleLine(true);t.setEllipsize(android.text.TextUtils.TruncateAt.END);LinearLayout.LayoutParams tl=new LinearLayout.LayoutParams(dp(132),dp(34));tl.topMargin=dp(4);wrap.addView(t,tl);
      LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(dp(140),dp(135));tp.setMargins(dp(3),0,dp(3),0);dock.addView(wrap,tp);tiles.add(wrap);
      wrap.setOnFocusChangeListener((v,hasFocus)->{icon.setBackground(panel(hasFocus?0xEE254861:0xD929394E,hasFocus?0xffA7E1FF:0x22FFFFFF));icon.setScaleX(hasFocus?1.06f:1f);icon.setScaleY(hasFocus?1.06f:1f);});
      wrap.setOnClickListener(v->launch(index));
      wrap.setOnKeyListener((v,key,e)->{if(e.getAction()==KeyEvent.ACTION_DOWN && key==KeyEvent.KEYCODE_MENU){settings();return true;}return false;});
    }
    main.postDelayed(()->tiles.get(0).requestFocus(),150);
  }
  String symbol(int i){return new String[]{"N","▶","plex","prime","Disney+","▣","HDMI","⚙"}[i];}
  void refreshClock(){Date now=new Date();clock.setText(new SimpleDateFormat("HH:mm",Locale.getDefault()).format(now)); date.setText(new SimpleDateFormat("EEE, d MMM yyyy",Locale.getDefault()).format(now));int hr=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);greeting.setText(hr<12?"Good morning":hr<18?"Good afternoon":"Good evening");}
  void launch(int i){if(i==7){settings();return;}if(i==6){try{startActivity(new Intent("android.settings.INPUT_SETTINGS"));}catch(Exception x){message("HDMI switching is not supported by this TCL firmware.");}return;}
    if(i==5){try{startActivity(new Intent("android.intent.action.VIEW").setPackage("com.tcl.tv"));}catch(Exception x){chooseApp();}return;}
    Intent in=getPackageManager().getLaunchIntentForPackage(packages[i]);if(in!=null){try{startActivity(in);return;}catch(Exception ignored){}}message(names[i]+" is not installed. Use All apps in Settings to open available apps.");
  }
  void settings(){String[] options={"Change weather city","Refresh weather","All installed TV apps","About / build"};new AlertDialog.Builder(this).setTitle("Nature TV Home settings").setItems(options,(d,w)->{if(w==0)editCity();else if(w==1)fetchWeather();else if(w==2)chooseApp();else message("Nature TV Home v0.1.0 (source build)\nNo ads or analytics. Weather: Open-Meteo.");}).show();}
  void editCity(){EditText input=new EditText(this);input.setSingleLine();input.setText(city);new AlertDialog.Builder(this).setTitle("Weather city").setView(input).setPositiveButton("Save",(d,w)->{city=input.getText().toString().trim();if(city.isEmpty())city="Breda";getPreferences(0).edit().putString("city",city).apply();weather.setText(city+" · Loading…");fetchWeather();}).setNegativeButton("Cancel",null).show();}
  void chooseApp(){new Thread(()->{ArrayList<ResolveInfo> found=new ArrayList<>();Intent q=new Intent(Intent.ACTION_MAIN);q.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);found.addAll(getPackageManager().queryIntentActivities(q,0));found.sort((a,b)->a.loadLabel(getPackageManager()).toString().compareToIgnoreCase(b.loadLabel(getPackageManager()).toString()));String[] labels=new String[found.size()];for(int i=0;i<found.size();i++)labels[i]=found.get(i).loadLabel(getPackageManager()).toString();runOnUiThread(()->new AlertDialog.Builder(this).setTitle("Installed TV apps").setItems(labels,(d,index)->{ResolveInfo a=found.get(index);Intent x=new Intent(Intent.ACTION_MAIN);x.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);x.setClassName(a.activityInfo.packageName,a.activityInfo.name);startActivity(x);}).show());}).start();}
  void message(String s){new AlertDialog.Builder(this).setMessage(s).setPositiveButton("OK",null).show();}
  void fetchWeather(){final String place=city; weather.setText(place+"  ·  Updating weather…");new Thread(()->{
    try {String geo=readUrl("https://geocoding-api.open-meteo.com/v1/search?name="+URLEncoder.encode(place,"UTF-8")+"&count=1&language=en&format=json");JSONArray res=new JSONObject(geo).optJSONArray("results");if(res==null||res.length()==0)throw new IOException("City not found");JSONObject loc=res.getJSONObject(0);double lat=loc.getDouble("latitude"),lon=loc.getDouble("longitude");
      String url="https://api.open-meteo.com/v1/forecast?latitude="+lat+"&longitude="+lon+"&current=temperature_2m,weather_code&timezone=auto"; JSONObject cur=new JSONObject(readUrl(url)).getJSONObject("current");int temp=(int)Math.round(cur.getDouble("temperature_2m"));int code=cur.getInt("weather_code");String display=loc.getString("name")+"  ☁  "+temp+"°C\n"+condition(code);getPreferences(0).edit().putString("cachedWeather",display).putLong("weatherWhen",System.currentTimeMillis()).apply();runOnUiThread(()->weather.setText(display));
    }catch(Exception e){String cached=getPreferences(0).getString("cachedWeather",null);runOnUiThread(()->weather.setText(cached!=null?cached+"\nLast saved · offline":place+" · Weather unavailable"));}
  }).start();}
  String readUrl(String s)throws Exception{HttpURLConnection c=(HttpURLConnection)new URL(s).openConnection();c.setConnectTimeout(7000);c.setReadTimeout(7000);c.setRequestProperty("User-Agent","NatureTVHome/0.1.0");try(InputStream in=c.getInputStream();ByteArrayOutputStream out=new ByteArrayOutputStream()){byte[] b=new byte[4096];int n;while((n=in.read(b))!=-1)out.write(b,0,n);return out.toString("UTF-8");}finally{c.disconnect();}}
  String condition(int c){if(c==0)return "Clear sky";if(c<=3)return "Partly cloudy";if(c<=48)return "Foggy";if(c<=67)return "Rain";if(c<=77)return "Snow";if(c<=82)return "Showers";if(c<=86)return "Snow showers";if(c>=95)return "Thunderstorm";return "Cloudy";}
}
