REM Start selenium hub first and then selenium node
REM start cmd.exe /c
TITLE Console - Node
java -jar E:\AutomationRelated\SeleniumGridSetUp\selenium-server-4.9.0.jar node --detect-drivers true --publish-events tcp://192.168.0.108:4442 --subscribe-events tcp://192.168.0.108:4443
