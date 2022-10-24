@echo off
echo[
set CLASS_PATH=%~dp0lib\*;%~dp0settings\
echo CLASS_PATH = %CLASS_PATH%
chcp 65001
${jre.bin.path} -Dprism.order=sw -Dprism.verbose=true -Dfile.encoding=UTF-8 -Dlogback.configurationFile=./settings/logback.xml -Dlogs_dir=./logs -cp %CLASS_PATH% ashev.flowers_calendar.Main %*