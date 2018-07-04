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
	Dim const UUID_SERVICE_W 	= "99ddcda5-a80c-4f94-be5d-c66b9fba40cf" As String
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	
	Dim datacount As Int
	Dim Const FLASH_DATA    	As String = "99dd0004-a80c-4f94-be5d-c66b9fba40cf"
	Dim Const COMMANDES 		As String = "99dd0014-a80c-4f94-be5d-c66b9fba40cf"
	Dim Const BATT_LEVEL As String = "99dd0016-a80c-4f94-be5d-c66b9fba40cf"
	Dim Const GET_CADENCE As String = "99dd0305-a80c-4f94-be5d-c66b9fba40cf"
	Dim Const REAL_TIME_VALUE 	As String = "99dd0108-a80c-4f94-be5d-c66b9fba40cf"
	Dim Const STATES_SHADOWS 	As String = "99dd0109-a80c-4f94-be5d-c66b9fba40cf"
	Dim Const GENERAL_STATES 	As String = "99dd0110-a80c-4f94-be5d-c66b9fba40cf"
	Private btnReadBatt As Button
	Private ldlData As Label
End Sub

Sub Activity_Create(FirstTime As Boolean)
	Log("DANS BLEDATA activitycreate")
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Activity.LoadLayout("lytMain")
End Sub

Sub Activity_Resume
	Log("DANS BLEDATA activityresume")
End Sub

Public Sub DataAvailable(Services As String, Characteristics As Map)
	
	Log("DANS BLEDATA dataavailable")
	Dim bc As ByteConverter
	Dim Hex1 As Int
	Dim Hex2 As Int
	Dim Hex3 As Int
	Dim Hex4 As Int
	
	For Each k As String In Characteristics.Keys
		
		Dim Value() As Byte = Characteristics.Get(k)
		'Dim temp_DATA() As Int  = bc.IntsFromBytes(Value)
		Dim temp_DATA As String  = bc.HexFromBytes(Value)
	'	If k = BATT_LEVEL Then
			'Dim Value() As Byte = Characteristics.Get(k)
	'	End If
		
		If k = GET_CADENCE Then
			'Dim Value() As Byte = Characteristics.Get(k)
			'Dim temp_DATA As String  = bc.HexFromBytes(Value)
			'Dim chaine As String = BytesToString(Value,0,18,"UTF-8")
			'datacount = datacount+1
			Log("LOL "&temp_DATA)
			'ldlData.Text = temp_DATA(1) & " - " & datacount
		End If
		
		
		If k = BATT_LEVEL Then
			Dim Value() As Byte = Characteristics.Get(k)
			temp_DATA = bc.HexFromBytes(Value)
			Hex1 = Bit.ParseInt(temp_DATA.SubString2(0, 2), 16)
		'VBAT1 = Hex1
			Hex2 = Bit.ParseInt(temp_DATA.SubString2(2, 4), 16)
			'Hex3 = Bit.ParseInt(temp_DATA.SubString2(4, 6), 16)
		'VBAT2 = Hex3
			'Hex4 = Bit.ParseInt(temp_DATA.SubString2(6, 8), 16)
			Log(Hex1 & " %" & Hex2 & " %")
		End If
		
		'Log("received from device: " & temp_DATA)
	Next

End Sub
Sub btnReadBatt_Click
	Log("DANS BLEDATA readclick")
	'CallSub2(Starter, "ReadData", BAS_SERVICE)
	'CallSub2(Starter, "ReadData", UUID_SERVICE_W)
	Starter.manager.SetNotify(UUID_SERVICE_W, GET_CADENCE,True)
	Starter.manager.SetNotify(UUID_SERVICE_W, BATT_LEVEL,True)
	
	Sleep(1000)
	Starter.manager.WriteData(UUID_SERVICE_W, COMMANDES, Array As Byte(0x01))
	Sleep(1000)
	CallSub2(Starter, "ReadData", UUID_SERVICE_W)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
	Log("DANS BLEDATA pause")
End Sub
