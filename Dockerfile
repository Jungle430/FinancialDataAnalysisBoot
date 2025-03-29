FROM amazoncorretto:17
COPY target /app/com.bupt.Jungle.FinancialDataAnalysis
WORKDIR /app/com.bupt.Jungle.FinancialDataAnalysis
RUN mkdir -p /opt/logs/com.bupt.Jungle.FinancialDataAnalysis
CMD java \
    -server \
    -Dfile.encoding=UTF-8 \
    -Dsun.jnu.encoding=UTF-8 \
    -Djava.io.tmpdir=/tmp \
    -XX:ActiveProcessorCount=2 \
    -Xss512k \
    -Xmx1g -Xms256m \
    -XX:MetaspaceSize=256m \
    -XX:MaxMetaspaceSize=256m \
    -XX:ReservedCodeCacheSize=128m \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:+UseG1GC -XX:G1HeapRegionSize=4M \
    -XX:InitiatingHeapOccupancyPercent=40 \
    -XX:MaxGCPauseMillis=100 \
    -XX:+TieredCompilation \
    -XX:CICompilerCount=2 \
    -Xlog:gc*:file=/opt/logs/com.bupt.Jungle.FinancialDataAnalysis/gc.log:time,uptime:filecount=30,filesize=50M \
    -XX:+PrintFlagsFinal \
    -jar FinancialDataAnalysisBoot-0.0.1-SNAPSHOT.jar > /opt/logs/com.bupt.Jungle.FinancialDataAnalysis/app.log 2>&1