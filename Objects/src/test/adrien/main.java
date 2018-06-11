package test.adrien;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "test.adrien", "test.adrien.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "test.adrien", "test.adrien.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "test.adrien.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return main.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static String _shared = "";
public anywheresoftware.b4a.phone.Phone.PhoneWakeState _awake = null;
public anywheresoftware.b4a.objects.LabelWrapper _lati = null;
public anywheresoftware.b4a.objects.LabelWrapper _longi = null;
public anywheresoftware.b4a.objects.LabelWrapper _satellit = null;
public anywheresoftware.b4a.objects.LabelWrapper _speed = null;
public anywheresoftware.b4a.objects.LabelWrapper _altitude = null;
public anywheresoftware.b4a.objects.LabelWrapper _dist = null;
public anywheresoftware.b4a.objects.MapFragmentWrapper _mapfragment1 = null;
public anywheresoftware.b4a.objects.MapFragmentWrapper.GoogleMapWrapper _gmap = null;
public anywheresoftware.b4a.objects.ButtonWrapper _reset = null;
public anywheresoftware.b4a.objects.ButtonWrapper _save = null;
public test.adrien.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 49;BA.debugLine="Log(\"DANS ActivityCreate\")";
anywheresoftware.b4a.keywords.Common.Log("DANS ActivityCreate");
 //BA.debugLineNum = 56;BA.debugLine="Activity.LoadLayout(\"VuePrincipale\")";
mostCurrent._activity.LoadLayout("VuePrincipale",mostCurrent.activityBA);
 //BA.debugLineNum = 57;BA.debugLine="If MapFragment1.IsGooglePlayServicesAvailable = F";
if (mostCurrent._mapfragment1.IsGooglePlayServicesAvailable(mostCurrent.activityBA)==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 58;BA.debugLine="ToastMessageShow(\"Google Play services not avail";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Google Play services not available."),anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 154;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 155;BA.debugLine="Log(\"DANS Pause\")";
anywheresoftware.b4a.keywords.Common.Log("DANS Pause");
 //BA.debugLineNum = 156;BA.debugLine="If UserClosed Then";
if (_userclosed) { 
 //BA.debugLineNum = 157;BA.debugLine="Awake.ReleaseKeepAlive";
mostCurrent._awake.ReleaseKeepAlive();
 };
 //BA.debugLineNum = 160;BA.debugLine="End Sub";
return "";
}
public static void  _activity_resume() throws Exception{
ResumableSub_Activity_Resume rsub = new ResumableSub_Activity_Resume(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Resume extends BA.ResumableSub {
public ResumableSub_Activity_Resume(test.adrien.main parent) {
this.parent = parent;
}
test.adrien.main parent;
String _permission = "";
boolean _result = false;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 93;BA.debugLine="Log(\"DANS Resume \")";
anywheresoftware.b4a.keywords.Common.Log("DANS Resume ");
 //BA.debugLineNum = 95;BA.debugLine="Log(\"DANS Resume après map\")";
anywheresoftware.b4a.keywords.Common.Log("DANS Resume après map");
 //BA.debugLineNum = 96;BA.debugLine="If Starter.gps.GPSEnabled = False Then";
if (true) break;

case 1:
//if
this.state = 12;
if (parent.mostCurrent._starter._gps.getGPSEnabled()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 97;BA.debugLine="ToastMessageShow(\"Please enable the GPS device.\"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Please enable the GPS device."),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 99;BA.debugLine="StartActivity(Starter.gps.LocationSettingsIntent";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)(parent.mostCurrent._starter._gps.getLocationSettingsIntent()));
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 102;BA.debugLine="Starter.rp.CheckAndRequest(Starter.rp.PERMISSION";
parent.mostCurrent._starter._rp.CheckAndRequest(processBA,parent.mostCurrent._starter._rp.PERMISSION_ACCESS_FINE_LOCATION);
 //BA.debugLineNum = 103;BA.debugLine="Wait For Activity_PermissionResult (Permission A";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 6;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 104;BA.debugLine="If Result Then CallSubDelayed(Starter, \"StartGPS";
if (true) break;

case 6:
//if
this.state = 11;
if (_result) { 
this.state = 8;
;}if (true) break;

case 8:
//C
this.state = 11;
anywheresoftware.b4a.keywords.Common.CallSubDelayed(processBA,(Object)(parent.mostCurrent._starter.getObject()),"StartGPS");
if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 109;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _activity_permissionresult(String _permission,boolean _result) throws Exception{
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private shared As String";
mostCurrent._shared = "";
 //BA.debugLineNum = 26;BA.debugLine="Dim Awake As PhoneWakeState";
mostCurrent._awake = new anywheresoftware.b4a.phone.Phone.PhoneWakeState();
 //BA.debugLineNum = 31;BA.debugLine="Private Lati As Label";
mostCurrent._lati = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private Longi As Label";
mostCurrent._longi = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private Satellit As Label";
mostCurrent._satellit = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 34;BA.debugLine="Private Speed As Label";
mostCurrent._speed = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private Altitude As Label";
mostCurrent._altitude = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 36;BA.debugLine="Private Dist As Label";
mostCurrent._dist = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private MapFragment1 As MapFragment";
mostCurrent._mapfragment1 = new anywheresoftware.b4a.objects.MapFragmentWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private gmap As GoogleMap";
mostCurrent._gmap = new anywheresoftware.b4a.objects.MapFragmentWrapper.GoogleMapWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private Reset As Button";
mostCurrent._reset = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private Save As Button";
mostCurrent._save = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static String  _gps_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1,double _distance) throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Public Sub GPS_LocationChanged (Location1 As Locat";
 //BA.debugLineNum = 128;BA.debugLine="Log(\"DANS LocationChanged\")";
anywheresoftware.b4a.keywords.Common.Log("DANS LocationChanged");
 //BA.debugLineNum = 129;BA.debugLine="Awake.KeepAlive(True)";
mostCurrent._awake.KeepAlive(processBA,anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 132;BA.debugLine="Altitude.Text = Location1.Altitude";
mostCurrent._altitude.setText(BA.ObjectToCharSequence(_location1.getAltitude()));
 //BA.debugLineNum = 135;BA.debugLine="Lati.Text = Round(Location1.Latitude*11930464.711";
mostCurrent._lati.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Round(_location1.getLatitude()*11930464.7111)));
 //BA.debugLineNum = 136;BA.debugLine="Longi.Text = Round(Location1.Longitude*11930464.7";
mostCurrent._longi.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Round(_location1.getLongitude()*11930464.7111)));
 //BA.debugLineNum = 137;BA.debugLine="Speed.Text = $\"$1.1{Location1.Speed*3.6} km/h \"$";
mostCurrent._speed.setText(BA.ObjectToCharSequence((""+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.1",(Object)(_location1.getSpeed()*3.6))+" km/h ")));
 //BA.debugLineNum = 142;BA.debugLine="Dist.Text = Distance";
mostCurrent._dist.setText(BA.ObjectToCharSequence(_distance));
 //BA.debugLineNum = 147;BA.debugLine="Awake.ReleaseKeepAlive";
mostCurrent._awake.ReleaseKeepAlive();
 //BA.debugLineNum = 148;BA.debugLine="End Sub";
return "";
}
public static String  _gpsstatus(anywheresoftware.b4a.objects.collections.List _satellites) throws Exception{
anywheresoftware.b4a.keywords.StringBuilderWrapper _sb = null;
int _i = 0;
anywheresoftware.b4a.gps.GpsSatelliteWrapper _satellite = null;
 //BA.debugLineNum = 112;BA.debugLine="Public Sub GpsStatus (Satellites As List)";
 //BA.debugLineNum = 113;BA.debugLine="Log(\"DANS GPS_Status\")";
anywheresoftware.b4a.keywords.Common.Log("DANS GPS_Status");
 //BA.debugLineNum = 115;BA.debugLine="Dim sb As StringBuilder";
_sb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 116;BA.debugLine="sb.Initialize";
_sb.Initialize();
 //BA.debugLineNum = 118;BA.debugLine="For i = 0 To Satellites.Size - 1";
{
final int step4 = 1;
final int limit4 = (int) (_satellites.getSize()-1);
_i = (int) (0) ;
for (;(step4 > 0 && _i <= limit4) || (step4 < 0 && _i >= limit4) ;_i = ((int)(0 + _i + step4))  ) {
 //BA.debugLineNum = 119;BA.debugLine="Dim Satellite As GPSSatellite = Satellites.Get(i";
_satellite = new anywheresoftware.b4a.gps.GpsSatelliteWrapper();
_satellite.setObject((android.location.GpsSatellite)(_satellites.Get(_i)));
 //BA.debugLineNum = 120;BA.debugLine="sb.Append(CRLF).Append(Satellite.Prn).Append($\"";
_sb.Append(anywheresoftware.b4a.keywords.Common.CRLF).Append(BA.NumberToString(_satellite.getPrn())).Append((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_satellite.getSnr()))+"")).Append(" ").Append(BA.ObjectToString(_satellite.getUsedInFix()));
 //BA.debugLineNum = 121;BA.debugLine="sb.Append(\" \").Append($\" $1.2{Satellite.Azimuth}";
_sb.Append(" ").Append((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_satellite.getAzimuth()))+"")).Append((" "+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_satellite.getElevation()))+""));
 }
};
 //BA.debugLineNum = 123;BA.debugLine="Satellit.Text = sb.ToString";
mostCurrent._satellit.setText(BA.ObjectToCharSequence(_sb.ToString()));
 //BA.debugLineNum = 124;BA.debugLine="End Sub";
return "";
}
public static String  _mapfragment1_ready() throws Exception{
 //BA.debugLineNum = 166;BA.debugLine="Sub MapFragment1_Ready";
 //BA.debugLineNum = 167;BA.debugLine="Log(\"DANS Map_ready\")";
anywheresoftware.b4a.keywords.Common.Log("DANS Map_ready");
 //BA.debugLineNum = 168;BA.debugLine="gmap = MapFragment1.GetMap";
mostCurrent._gmap = mostCurrent._mapfragment1.GetMap();
 //BA.debugLineNum = 172;BA.debugLine="gmap.MyLocationEnabled = True";
mostCurrent._gmap.setMyLocationEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 181;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 17;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _reset_longclick() throws Exception{
 //BA.debugLineNum = 222;BA.debugLine="Sub Reset_LongClick";
 //BA.debugLineNum = 223;BA.debugLine="Log(\"DANS Reset\")";
anywheresoftware.b4a.keywords.Common.Log("DANS Reset");
 //BA.debugLineNum = 224;BA.debugLine="Starter.Distance = 0";
mostCurrent._starter._distance = 0;
 //BA.debugLineNum = 225;BA.debugLine="Dist.Text = 0";
mostCurrent._dist.setText(BA.ObjectToCharSequence(0));
 //BA.debugLineNum = 226;BA.debugLine="Starter.cpt = 0";
mostCurrent._starter._cpt = (int) (0);
 //BA.debugLineNum = 227;BA.debugLine="Save.Enabled = True";
mostCurrent._save.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 228;BA.debugLine="Starter.listLocations.Clear";
mostCurrent._starter._listlocations.Clear();
 //BA.debugLineNum = 229;BA.debugLine="Starter.List1.Clear";
mostCurrent._starter._list1.Clear();
 //BA.debugLineNum = 230;BA.debugLine="End Sub";
return "";
}
public static void  _save_click() throws Exception{
ResumableSub_Save_Click rsub = new ResumableSub_Save_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_Save_Click extends BA.ResumableSub {
public ResumableSub_Save_Click(test.adrien.main parent) {
this.parent = parent;
}
test.adrien.main parent;
String _permission = "";
boolean _result = false;
anywheresoftware.b4a.objects.MapFragmentWrapper.PolylineWrapper _pl = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 186;BA.debugLine="Log(\"DANS SaveClick\")";
anywheresoftware.b4a.keywords.Common.Log("DANS SaveClick");
 //BA.debugLineNum = 187;BA.debugLine="If File.ExternalWritable = False Then";
if (true) break;

case 1:
//if
this.state = 12;
if (anywheresoftware.b4a.keywords.Common.File.getExternalWritable()==anywheresoftware.b4a.keywords.Common.False) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 12;
 //BA.debugLineNum = 188;BA.debugLine="Msgbox(\"No rights to write\",\" \")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("No rights to write"),BA.ObjectToCharSequence(" "),mostCurrent.activityBA);
 //BA.debugLineNum = 189;BA.debugLine="Return";
if (true) return ;
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 191;BA.debugLine="shared = Starter.rp.GetSafeDirDefaultExternal(\"t";
parent.mostCurrent._shared = parent.mostCurrent._starter._rp.GetSafeDirDefaultExternal("test");
 //BA.debugLineNum = 192;BA.debugLine="Starter.rp.CheckAndRequest(Starter.rp.PERMISSION";
parent.mostCurrent._starter._rp.CheckAndRequest(processBA,parent.mostCurrent._starter._rp.PERMISSION_READ_EXTERNAL_STORAGE);
 //BA.debugLineNum = 193;BA.debugLine="Starter.rp.CheckAndRequest(Starter.rp.PERMISSION";
parent.mostCurrent._starter._rp.CheckAndRequest(processBA,parent.mostCurrent._starter._rp.PERMISSION_WRITE_EXTERNAL_STORAGE);
 //BA.debugLineNum = 194;BA.debugLine="Wait For Activity_PermissionResult(Permission As";
anywheresoftware.b4a.keywords.Common.WaitFor("activity_permissionresult", processBA, this, null);
this.state = 13;
return;
case 13:
//C
this.state = 6;
_permission = (String) result[0];
_result = (Boolean) result[1];
;
 //BA.debugLineNum = 195;BA.debugLine="If Result Then SaveStringExample";
if (true) break;

case 6:
//if
this.state = 11;
if (_result) { 
this.state = 8;
;}if (true) break;

case 8:
//C
this.state = 11;
_savestringexample();
if (true) break;

case 11:
//C
this.state = 12;
;
 if (true) break;

case 12:
//C
this.state = -1;
;
 //BA.debugLineNum = 203;BA.debugLine="Dim pl As Polyline = gmap.AddPolyline";
_pl = new anywheresoftware.b4a.objects.MapFragmentWrapper.PolylineWrapper();
_pl = parent.mostCurrent._gmap.AddPolyline();
 //BA.debugLineNum = 204;BA.debugLine="pl.Points = Starter.listLocations";
_pl.setPoints(parent.mostCurrent._starter._listlocations);
 //BA.debugLineNum = 205;BA.debugLine="pl.Color=Colors.Blue";
_pl.setColor(anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 206;BA.debugLine="pl.ZIndex=3";
_pl.setZIndex((float) (3));
 //BA.debugLineNum = 214;BA.debugLine="Save.Enabled = False";
parent.mostCurrent._save.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 220;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _savestringexample() throws Exception{
 //BA.debugLineNum = 87;BA.debugLine="Sub SaveStringExample";
 //BA.debugLineNum = 88;BA.debugLine="Log(\"DANS SaveStringExample\")";
anywheresoftware.b4a.keywords.Common.Log("DANS SaveStringExample");
 //BA.debugLineNum = 89;BA.debugLine="File.WriteList(shared, \"PositionsGPS.txt\", Starte";
anywheresoftware.b4a.keywords.Common.File.WriteList(mostCurrent._shared,"PositionsGPS.txt",mostCurrent._starter._list1);
 //BA.debugLineNum = 90;BA.debugLine="End Sub";
return "";
}
}
