FROM amazoncorretto:17
COPY target /app/FinancialDataAnalysisBoot
WORKDIR /app/FinancialDataAnalysisBoot
RUN mkdir -p /app/log
CMD java -jar FinancialDataAnalysisBoot-0.0.1-SNAPSHOT.jar > /app/log/FinancialDataAnalysisBoot.log 2>&1