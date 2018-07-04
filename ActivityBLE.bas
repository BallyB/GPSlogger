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
	Public adrs_mac As String
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Dim cd4 As ColorDrawable
	
	Private Label1 As Label
	'Private HorizontalScrollView1 As HorizontalScrollView
	'Private btnReadData As Button
	Private btnDisconnect As Button
	Private btnScan As Button
	Private lblDeviceStatus As Label
	Private lblState As Label
	'Private pbReadData As ProgressBar
	Private ProgressBar1 As ProgressBar
	'Private clv As CustomListView
	Private spinMac As Spinner
	
	Private connect As Button
	Dim Id_to_co As String

End Sub

Sub Activity_Create(FirstTime As Boolean)
	Log("DANS ACTIVITYBLE ActivityCreate")
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("VueBLE")
End Sub

Sub Activity_Resume
	Log("DANS ACTIVITYBLE ActivityResume")
	StateChanged
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Log("DANS ACTIVITYBLE ActivityPause")
End Sub

Sub btnScan_Click
	Log("DANS ACTIVITYBLE ScanClick")
	Starter.rp.CheckAndRequest(Starter.rp.PERMISSION_ACCESS_COARSE_LOCATION)
	Wait For Activity_PermissionResult (Permission As String, Result As Boolean)
	If Result = False Then Return
	ProgressBar1.Visible = True
	CallSub(Starter, "StartScan")
End Sub



Public Sub Addbledevice(Name As String, Id As String)
	Log("DANS ACTIVITYBLE addbledevice")
	Dim arg2 As String
	
	arg2 = Name & "	MAC: " & Id
'	If File.ExternalWritable = True Then
'		If Id.CompareTo(Id_to_co) = 0 Then
			'CallSub2(Starter, "Connect", Id_to_co)
'			mac_ok_Click
'		End If
'	End If
	'If Name.CompareTo("ZTTrack") = 0 Then
		spinMac.Add(arg2)
	'End If
	'BubbleSort(Page3ListViewD, lvd)
	
End Sub


Public Sub StateChanged
	Log("DANS ACTIVITYBLE StateChanged")
	lblState.Text = Starter.currentStateText
	
	btnDisconnect.Enabled = Starter.connected
	spinMac.Visible = Not(Starter.connected)
	connect.Enabled = Not(Starter.connected)
	'pbReadData.Visible = False
	ProgressBar1.Visible = False
	'btnReadData.Enabled = Starter.connected
	btnScan.Enabled = (Starter.currentState = Starter.manager.STATE_POWERED_ON) And Starter.connected = False
	If Starter.connected Then
		lblDeviceStatus.Text = "Connected: " & adrs_mac
		Sleep(2000)
		StartActivity(BLEdata)
	Else
		lblDeviceStatus.Text = "Not connected"
	End If
End Sub

Sub connect_Click
	Log("DANS ACTIVITYBLE connect_Click")
	'Starter.manager.StopScan
	'connect.Visible = True
	spinMac.Visible = False
	connect.Enabled = False
	cd4.Initialize(Colors.ARGB(200,255,102,0),0)
	'connect_start.TextColor=Colors.ARGB(200,255,255,255)
	'connect_start.Text="START"
	'connect_start.Background=cd4
	'stockage_mac
	CallSub2(Starter, "Connect", Id_to_co)
End Sub
Sub btnDisconnect_Click
	Log("DANS MAIN DisconnectClick")
	CallSub(Starter, "Disconnect")
End Sub
Sub spinMac_ItemClick (Position As Int, Value As Object)
	Log("DANS ACTIVITYBLE spinMac_Click")
	adrs_mac = spinMac.GetItem(Position)
	Id_to_co = adrs_mac.SubString2((adrs_mac.IndexOf(":") + 2), adrs_mac.Length)
End Sub