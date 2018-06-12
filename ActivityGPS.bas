B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=8
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.

End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private MapFragment1 As MapFragment
	Private gmap As GoogleMap
	Private Save As Button
	Private shared As String
	Private Dist As Label
	Private Lati As Label
	Private Longi As Label
	Private Satellit As Label
	Private Speed As Label
	Private Altitude As Label
	Dim Awake As PhoneWakeState
	'Private mapPanel As Panel
	Private Reset As Button
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Log("DANS ACT_GPS Activitycreate")
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("VueGPS")
	
	If MapFragment1.IsGooglePlayServicesAvailable = False Then
		ToastMessageShow("Google Play services not available.", True)
	End If
End Sub

Sub SaveStringExample
	Log("DANS ACT_GPS SaveStringExample")
	File.WriteList(shared, "PositionsGPS.txt", Starter.List1)
End Sub
Sub Activity_Resume
	Log("DANS ACT_GPS ActivityResume ")
	Do Until MapFragment1.GetMap.IsInitialized
		Sleep(100)
	Loop
'	Log("DANS Resume après map")
	If Starter.gps.GPSEnabled = False Then
		ToastMessageShow("Please enable the GPS device.", True)
		'Activity.GetStartingIntent
		StartActivity(Starter.gps.LocationSettingsIntent) 'Will open the relevant settings screen.
	Else
		
		Starter.rp.CheckAndRequest(Starter.rp.PERMISSION_ACCESS_FINE_LOCATION)
		Wait For Activity_PermissionResult (Permission As String, Result As Boolean)
		If Result Then CallSubDelayed(Starter, "StartGPS")
	End If
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Log("DANS ACT_GPS ActivityPause")
	If UserClosed Then
		Awake.ReleaseKeepAlive
	End If
	'CallSubDelayed(Starter, "StopGPS")
End Sub

Sub MapFragment1_Ready
	Log("DANS ACT_GPS Map_ready"&Chr(0xF23C))
	gmap = MapFragment1.GetMap
	'If gmap.IsInitialized = False Then
	'	ToastMessageShow("Error initializing map.", True)
	'Else
	gmap.MyLocationEnabled = True
	'End If
	'Starter.rp.CheckAndRequest(Starter.rp.PERMISSION_ACCESS_FINE_LOCATION)
	'Wait For Activity_PermissionResult (Permission As String, Result As Boolean)
	
	
	'Result
	'Dim m1 As Marker = gmap.AddMarker(10,30,"test")
	'm1.Snippet = "This is the snippet"
End Sub

Sub Save_Click
	Log("DANS ACT_GPS SaveClick")
	If File.ExternalWritable = False Then
		Msgbox("No rights to write"," ")
		Return
	Else
		shared = Starter.rp.GetSafeDirDefaultExternal("test")
		Starter.rp.CheckAndRequest(Starter.rp.PERMISSION_READ_EXTERNAL_STORAGE)
		Starter.rp.CheckAndRequest(Starter.rp.PERMISSION_WRITE_EXTERNAL_STORAGE)
		Wait For Activity_PermissionResult(Permission As String, Result As Boolean)
		If Result Then SaveStringExample
	End If
'	MapFragment1.Initialize("Map",Activity)
	'Panel1.Initialize("")
'	Wait For MapFragment1_Ready
	'If gmap.IsInitialized = False Then
	'	gmap = MapFragment1.GetMap
	'	gmap.MyLocationEnabled = True
	Dim pl As Polyline = gmap.AddPolyline
	pl.Points = Starter.listLocations
	pl.Color=Colors.Blue
	pl.ZIndex=3
	'Else
	'	Dim pl As Polyline = gmap.AddPolyline
	'	pl.Points = Starter.listLocations
	'	pl.Color=Colors.Blue
	'	pl.ZIndex=3
	'End If
	
	Save.Enabled = False
	'If MapFragment1.IsGooglePlayServicesAvailable = False Then
	'	ToastMessageShow("Google Play services not available.", True)
'	Else
	'	MapFragment1.Initialize("MapFragment1", mapPanel)
'	End If
End Sub

Public Sub GpsStatus (Satellites As List)
	Log("DANS ACT_GPS GPS_Status")
	'Starter.gps.Initialize
	Dim sb As StringBuilder
	sb.Initialize
	'sb.Append("Satellites: ")
	For i = 0 To Satellites.Size - 1
		Dim Satellite As GPSSatellite = Satellites.Get(i)
		sb.Append(CRLF).Append(Satellite.Prn).Append($" $1.2{Satellite.Snr}"$).Append(" ").Append(Satellite.UsedInFix)
		sb.Append(" ").Append($" $1.2{Satellite.Azimuth}"$).Append($" $1.2{Satellite.Elevation}"$)
	Next
	Satellit.Text = sb.ToString
End Sub

Public Sub GPS_LocationChanged (Location1 As Location,Distance As Double)
	Log("DANS ACT_GPS LocationChanged")
	Awake.KeepAlive(True)
	'Dim sbb As StringBuilder
	'Dim unixTime As Long = currentTimeMillis() / 1000L;
	Altitude.Text = Location1.Altitude
	
	'Distance.Text = Loc
	Lati.Text = Round(Location1.Latitude*11930464.7111)
	Longi.Text = Round(Location1.Longitude*11930464.7111)
	Speed.Text = $"$1.1{Location1.Speed*3.6} km/h "$
	'Timestamp de Garmin currenttimestamp - 631065600
	'currenttimestamp - 631065600  : Log(Round(DateTime.Now/1000)-631065600)
	
	'End If
	Dist.Text = Distance
	
	
	
	
	Awake.ReleaseKeepAlive
End Sub

Sub Reset_LongClick
	Log("DANS ACT_GPS Reset")
	Starter.Distance = 0
	Dist.Text = 0
	Starter.cpt = 0
	Save.Enabled = True
	Starter.listLocations.Clear
	Starter.List1.Clear
End Sub