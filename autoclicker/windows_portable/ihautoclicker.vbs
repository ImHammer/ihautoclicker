' Set WshShell = CreateObject("WScript.Shell")
' WshShell.Run chr(34) & "C:\Caminho\Para\Seu\script.bat" & Chr(34), 0
' Set WshShell = Nothing
Set WshShell = CreateObject("WScript.Shell")
WshShell.Run "cmd /c .\app\bin\com.github.imhammer.ihautoclicker.bat", 0, True