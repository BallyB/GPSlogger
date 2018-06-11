package test.adrien;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.objects.ServiceHelper;
import anywheresoftware.b4a.debug.*;

public class starter extends  android.app.Service{
	public static class starter_BR extends android.content.BroadcastReceiver {

		@Override
		public void onReceive(android.content.Context context, android.content.Intent intent) {
            BA.LogInfo("** Receiver (starter) OnReceive **");
			android.content.Intent in = new android.content.Intent(context, starter.class);
			if (intent != null)
				in.putExtra("b4a_internal_intent", intent);
            ServiceHelper.StarterHelper.startServiceFromReceiver (context, in, true, BA.class);
		}

	}
    static starter mostCurrent;
	public static BA processBA;
    private ServiceHelper _service;
    public static Class<?> getObject() {
		return starter.class;
	}
	@Override
	public void onCreate() {
        super.onCreate();
        mostCurrent = this;
        if (processBA == null) {
		    processBA = new BA(this, null, null, "test.adrien", "test.adrien.starter");
            if (BA.isShellModeRuntimeCheck(processBA)) {
                processBA.raiseEvent2(null, true, "SHELL", false);
		    }
            try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            processBA.loadHtSubs(this.getClass());
            ServiceHelper.init();
        }
        _service = new ServiceHelper(this);
        processBA.service = this;
        
        if (BA.isShellModeRuntimeCheck(processBA)) {
			processBA.raiseEvent2(null, true, "CREATE", true, "test.adrien.starter", processBA, _service, anywheresoftware.b4a.keywords.Common.Density);
		}
        if (!true && ServiceHelper.StarterHelper.startFromServiceCreate(processBA, false) == false) {
				
		}
		else {
            processBA.setActivityPaused(false);
            BA.LogInfo("*** Service (starter) Create ***");
            processBA.raiseEvent(null, "service_create");
        }
        processBA.runHook("oncreate", this, null);
        if (true) {
			ServiceHelper.StarterHelper.runWaitForLayouts();
		}
    }
		@Override
	public void onStart(android.content.Intent intent, int startId) {
		onStartCommand(intent, 0, 0);
    }
    @Override
    public int onStartCommand(final android.content.Intent intent, int flags, int startId) {
    	if (ServiceHelper.StarterHelper.onStartCommand(processBA, new Runnable() {
            public void run() {
                handleStart(intent);
            }}))
			;
		else {
			ServiceHelper.StarterHelper.addWaitForLayout (new Runnable() {
				public void run() {
                    processBA.setActivityPaused(false);
                    BA.LogInfo("** Service (starter) Create **");
                    processBA.raiseEvent(null, "service_create");
					handleStart(intent);
                    ServiceHelper.StarterHelper.removeWaitForLayout();
				}
			});
		}
        processBA.runHook("onstartcommand", this, new Object[] {intent, flags, startId});
		return android.app.Service.START_NOT_STICKY;
    }
    public void onTaskRemoved(android.content.Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        if (true)
            processBA.raiseEvent(null, "service_taskremoved");
            
    }
    private void handleStart(android.content.Intent intent) {
    	BA.LogInfo("** Service (starter) Start **");
    	java.lang.reflect.Method startEvent = processBA.htSubs.get("service_start");
    	if (startEvent != null) {
    		if (startEvent.getParameterTypes().length > 0) {
    			anywheresoftware.b4a.objects.IntentWrapper iw = ServiceHelper.StarterHelper.handleStartIntent(intent, _service, processBA);
    			processBA.raiseEvent(null, "service_start", iw);
    		}
    		else {
    			processBA.raiseEvent(null, "service_start");
    		}
    	}
    }
	
	@Override
	public void onDestroy() {
        super.onDestroy();
        BA.LogInfo("** Service (starter) Destroy **");
		processBA.raiseEvent(null, "service_destroy");
        processBA.service = null;
		mostCurrent = null;
		processBA.setActivityPaused(true);
        processBA.runHook("ondestroy", this, null);
	}

@Override
	public android.os.IBinder onBind(android.content.Intent intent) {
		return null;
	}public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public static anywheresoftware.b4a.gps.GPS _gps = null;
public static boolean _gpsstarted = false;
public static anywheresoftware.b4a.objects.collections.List _list1 = null;
public static anywheresoftware.b4a.gps.LocationWrapper _startlocation = null;
public static anywheresoftware.b4a.gps.LocationWrapper _endlocation = null;
public static double _latitudereal = 0;
public static double _longitudereal = 0;
public static double _latitudestart = 0;
public static double _longitudeend = 0;
public static anywheresoftware.b4a.objects.collections.List _listlocations = null;
public static double _distance = 0;
public static int _cpt = 0;
public test.adrien.main _main = null;
public static boolean  _application_error(anywheresoftware.b4a.objects.B4AException _error,String _stacktrace) throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub Application_Error (Error As Exception, StackTr";
 //BA.debugLineNum = 87;BA.debugLine="Log(\"DANS STARTER AppliError\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER AppliError");
 //BA.debugLineNum = 88;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return false;
}
public static String  _gps_gpsstatus(anywheresoftware.b4a.objects.collections.List _satellites) throws Exception{
 //BA.debugLineNum = 80;BA.debugLine="Sub GPS_GpsStatus (Satellites As List)";
 //BA.debugLineNum = 81;BA.debugLine="Log(\"DANS STARTER Gps_status\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER Gps_status");
 //BA.debugLineNum = 82;BA.debugLine="CallSub2(Main, \"GpsStatus\", Satellites)";
anywheresoftware.b4a.keywords.Common.CallSubNew2(processBA,(Object)(mostCurrent._main.getObject()),"GpsStatus",(Object)(_satellites));
 //BA.debugLineNum = 83;BA.debugLine="End Sub";
return "";
}
public static String  _gps_locationchanged(anywheresoftware.b4a.gps.LocationWrapper _location1) throws Exception{
anywheresoftware.b4a.objects.MapFragmentWrapper.LatLngWrapper _locations = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _sbb = null;
 //BA.debugLineNum = 30;BA.debugLine="Sub GPS_LocationChanged (Location1 As Location)";
 //BA.debugLineNum = 31;BA.debugLine="Log(\"DANS STARTER LocationChanged\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER LocationChanged");
 //BA.debugLineNum = 32;BA.debugLine="Dim locations As LatLng";
_locations = new anywheresoftware.b4a.objects.MapFragmentWrapper.LatLngWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Dim sbb As StringBuilder";
_sbb = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 34;BA.debugLine="locations.Initialize(Location1.Latitude,Location1";
_locations.Initialize(_location1.getLatitude(),_location1.getLongitude());
 //BA.debugLineNum = 35;BA.debugLine="listLocations.Add(locations)";
_listlocations.Add((Object)(_locations.getObject()));
 //BA.debugLineNum = 36;BA.debugLine="LatitudeReal   = Location1.Latitude";
_latitudereal = _location1.getLatitude();
 //BA.debugLineNum = 37;BA.debugLine="LongitudeReal  = Location1.Longitude";
_longitudereal = _location1.getLongitude();
 //BA.debugLineNum = 38;BA.debugLine="If cpt == 0 Then";
if (_cpt==0) { 
 //BA.debugLineNum = 39;BA.debugLine="StartLocation.Initialize2(LatitudeReal, Longitud";
_startlocation.Initialize2(BA.NumberToString(_latitudereal),BA.NumberToString(_longitudereal));
 };
 //BA.debugLineNum = 43;BA.debugLine="EndLocation.Initialize2(LatitudeReal, LongitudeRe";
_endlocation.Initialize2(BA.NumberToString(_latitudereal),BA.NumberToString(_longitudereal));
 //BA.debugLineNum = 45;BA.debugLine="Distance = Distance + StartLocation.DistanceTo(En";
_distance = _distance+_startlocation.DistanceTo((android.location.Location)(_endlocation.getObject()));
 //BA.debugLineNum = 49;BA.debugLine="LatitudeStart = LatitudeReal";
_latitudestart = _latitudereal;
 //BA.debugLineNum = 50;BA.debugLine="LongitudeEnd = LongitudeReal";
_longitudeend = _longitudereal;
 //BA.debugLineNum = 51;BA.debugLine="StartLocation.Initialize2(LatitudeStart, Longitud";
_startlocation.Initialize2(BA.NumberToString(_latitudestart),BA.NumberToString(_longitudeend));
 //BA.debugLineNum = 52;BA.debugLine="sbb.Initialize";
_sbb.Initialize();
 //BA.debugLineNum = 53;BA.debugLine="sbb.Append(\"timestamp,\" & Chr(34) & (Round(DateTi";
_sbb.Append("timestamp,"+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+BA.NumberToString((anywheresoftware.b4a.keywords.Common.Round(anywheresoftware.b4a.keywords.Common.DateTime.getNow()/(double)1000)-631065600))+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+",s,altitude,"+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+BA.NumberToString((_location1.getAltitude()))+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+",m,distance,"+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+BA.NumberToString(_distance)+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+",m,speed,"+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+(""+anywheresoftware.b4a.keywords.Common.SmartStringFormatter("1.2",(Object)(_location1.getSpeed()))+"")+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+",m/s,position_lat,"+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+BA.NumberToString((anywheresoftware.b4a.keywords.Common.Round(_location1.getLatitude()*11930464.7111)))+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+",position_long,"+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34)))+BA.NumberToString((anywheresoftware.b4a.keywords.Common.Round(_location1.getLongitude()*11930464.7111)))+BA.ObjectToString(anywheresoftware.b4a.keywords.Common.Chr((int) (34))));
 //BA.debugLineNum = 55;BA.debugLine="List1.Add(sbb.ToString)";
_list1.Add((Object)(_sbb.ToString()));
 //BA.debugLineNum = 56;BA.debugLine="cpt = cpt + 1";
_cpt = (int) (_cpt+1);
 //BA.debugLineNum = 58;BA.debugLine="CallSub3(Main, \"GPS_LocationChanged\", Location1,";
anywheresoftware.b4a.keywords.Common.CallSubNew3(processBA,(Object)(mostCurrent._main.getObject()),"GPS_LocationChanged",(Object)(_location1),(Object)(_distance));
 //BA.debugLineNum = 59;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Public rp As RuntimePermissions";
_rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 10;BA.debugLine="Public gps As GPS";
_gps = new anywheresoftware.b4a.gps.GPS();
 //BA.debugLineNum = 11;BA.debugLine="Private gpsStarted As Boolean";
_gpsstarted = false;
 //BA.debugLineNum = 12;BA.debugLine="Public List1 As List";
_list1 = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 13;BA.debugLine="Public StartLocation, EndLocation As Location";
_startlocation = new anywheresoftware.b4a.gps.LocationWrapper();
_endlocation = new anywheresoftware.b4a.gps.LocationWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Public LatitudeReal, LongitudeReal As Double";
_latitudereal = 0;
_longitudereal = 0;
 //BA.debugLineNum = 15;BA.debugLine="Public LatitudeStart, LongitudeEnd As Double";
_latitudestart = 0;
_longitudeend = 0;
 //BA.debugLineNum = 16;BA.debugLine="Public listLocations As List";
_listlocations = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 17;BA.debugLine="Public Distance As Double = 0";
_distance = 0;
 //BA.debugLineNum = 18;BA.debugLine="Public cpt As Int = 0";
_cpt = (int) (0);
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _service_create() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Service_Create";
 //BA.debugLineNum = 22;BA.debugLine="Log(\"DANS ServiceCreate\")";
anywheresoftware.b4a.keywords.Common.Log("DANS ServiceCreate");
 //BA.debugLineNum = 25;BA.debugLine="gps.Initialize(\"gps\")";
_gps.Initialize("gps");
 //BA.debugLineNum = 26;BA.debugLine="listLocations.Initialize";
_listlocations.Initialize();
 //BA.debugLineNum = 27;BA.debugLine="List1.Initialize";
_list1.Initialize();
 //BA.debugLineNum = 28;BA.debugLine="End Sub";
return "";
}
public static String  _service_destroy() throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Sub Service_Destroy";
 //BA.debugLineNum = 92;BA.debugLine="Log(\"DANS STARTER Service_destroy\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER Service_destroy");
 //BA.debugLineNum = 93;BA.debugLine="StopGps";
_stopgps();
 //BA.debugLineNum = 94;BA.debugLine="End Sub";
return "";
}
public static String  _service_start(anywheresoftware.b4a.objects.IntentWrapper _startingintent) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub Service_Start (StartingIntent As Intent)";
 //BA.debugLineNum = 77;BA.debugLine="Log(\"DANS STARTER Servicestart(vide)\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER Servicestart(vide)");
 //BA.debugLineNum = 79;BA.debugLine="End Sub";
return "";
}
public static String  _startgps() throws Exception{
 //BA.debugLineNum = 61;BA.debugLine="Public Sub StartGps";
 //BA.debugLineNum = 62;BA.debugLine="Log(\"DANS STARTER StartGPS\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER StartGPS");
 //BA.debugLineNum = 63;BA.debugLine="If gpsStarted = False Then";
if (_gpsstarted==anywheresoftware.b4a.keywords.Common.False) { 
 //BA.debugLineNum = 64;BA.debugLine="gps.Start(0, 0)";
_gps.Start(processBA,(long) (0),(float) (0));
 //BA.debugLineNum = 65;BA.debugLine="gpsStarted = True";
_gpsstarted = anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 67;BA.debugLine="End Sub";
return "";
}
public static String  _stopgps() throws Exception{
 //BA.debugLineNum = 69;BA.debugLine="Public Sub StopGps";
 //BA.debugLineNum = 70;BA.debugLine="Log(\"DANS STARTER StopGPS\")";
anywheresoftware.b4a.keywords.Common.Log("DANS STARTER StopGPS");
 //BA.debugLineNum = 71;BA.debugLine="If gpsStarted Then";
if (_gpsstarted) { 
 //BA.debugLineNum = 72;BA.debugLine="gps.Stop";
_gps.Stop();
 //BA.debugLineNum = 73;BA.debugLine="gpsStarted = False";
_gpsstarted = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 75;BA.debugLine="End Sub";
return "";
}
}
