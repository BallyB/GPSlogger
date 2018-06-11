B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=8
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Public rp As RuntimePermissions
	Public gps As GPS
	Private gpsStarted As Boolean
	Public List1 As List
	Public StartLocation, EndLocation As Location
	Public LatitudeReal, LongitudeReal As Double
	Public LatitudeStart, LongitudeEnd As Double
	Public listLocations As List
	Public Distance As Double = 0
	Public cpt As Int = 0
End Sub

Sub Service_Create
	Log("DANS ServiceCreate")
	'This is the program entry point.
	'This is a good place to load resources that are not specific to a single activity.
	gps.Initialize("gps")
	listLocations.Initialize
	List1.Initialize
End Sub

Sub GPS_LocationChanged (Location1 As Location)
	Log("DANS STARTER LocationChanged")
	Dim locations As LatLng
	Dim sbb As StringBuilder
	locations.Initialize(Location1.Latitude,Location1.Longitude)
	listLocations.Add(locations)
	LatitudeReal   = Location1.Latitude
	LongitudeReal  = Location1.Longitude
	If cpt == 0 Then
		StartLocation.Initialize2(LatitudeReal, LongitudeReal)
	End If
	'StartLocation.Initialize2
	'StartLocation.Initialize2(LatitudeReal, LongitudeReal)
	EndLocation.Initialize2(LatitudeReal, LongitudeReal)
	'If StartLocation.DistanceTo(EndLocation) < 15  Then
	Distance = Distance + StartLocation.DistanceTo(EndLocation)
	'Log(Distance)  'Why not
       	
	'Reset to start
	LatitudeStart = LatitudeReal
	LongitudeEnd = LongitudeReal
	StartLocation.Initialize2(LatitudeStart, LongitudeEnd)
	sbb.Initialize
	sbb.Append("timestamp," & Chr(34) & (Round(DateTime.Now/1000)-631065600) & Chr(34) & ",s,altitude,"&Chr(34)&(Location1.Altitude)&Chr(34)&",m,distance,"&Chr(34) & Distance & Chr(34) & ",m,speed," & Chr(34) & $"$1.2{Location1.Speed}"$ & Chr(34) & ",m/s,position_lat,"& Chr(34) & (Round(Location1.Latitude*11930464.7111)) & Chr(34) & ",position_long,"&Chr(34) & (Round(Location1.Longitude*11930464.7111))&Chr(34))
	'sbb.Append(CRLF)
	List1.Add(sbb.ToString)
	cpt = cpt + 1
	
	CallSub3(Main, "GPS_LocationChanged", Location1, Distance)
End Sub

Public Sub StartGps
	Log("DANS STARTER StartGPS")
	If gpsStarted = False Then
		gps.Start(0, 0)
		gpsStarted = True
	End If
End Sub

Public Sub StopGps
	Log("DANS STARTER StopGPS")
	If gpsStarted Then
		gps.Stop
		gpsStarted = False
	End If
End Sub
Sub Service_Start (StartingIntent As Intent)
	Log("DANS STARTER Servicestart(vide)")

End Sub
Sub GPS_GpsStatus (Satellites As List)
	Log("DANS STARTER Gps_status")
	CallSub2(Main, "GpsStatus", Satellites)
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Log("DANS STARTER AppliError")
	Return True
End Sub

Sub Service_Destroy
	Log("DANS STARTER Service_destroy")
	StopGps
End Sub








