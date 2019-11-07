prunsrv //DS//totemdmn
prunsrv //IS//totemdmn --Startup=auto --StdOutput=out.txt --StdError=error.txt --DisplayName="Servicio de tótem MGSM" --Jvm=auto --StartMode=jvm --StopMode=jvm --Classpath=totem-daemon.jar --StartClass=totem.daemon.Launcher --StopClass=totem.daemon.Launcher ++JvmOptions="-Djava.util.logging.config.file=logging.properties;-Dfile.encoding=UTF-8;"
net start totemdmn
