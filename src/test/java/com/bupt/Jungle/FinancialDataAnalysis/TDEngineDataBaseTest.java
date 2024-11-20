package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.service.impl.MeterTestServiceImpl;
import com.taosdata.jdbc.TSDBDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.*;
import java.util.Properties;

@SpringBootTest(classes = FinancialDataAnalysisBoot.class, properties = "personal.database.name=test")
@Slf4j
@ActiveProfiles("test")
class TDEngineDataBaseTest {
    private final String jdbcUrl;

    private final MeterTestServiceImpl meterTestService;

    @Autowired
    public TDEngineDataBaseTest(
            @Value("${spring.datasource.url}") String jdbcUrl,
            MeterTestServiceImpl meterTestService,
            @Value("${personal.database.name}") String personalDatabaseName
    ) {
        log.info("jdbcUrl: {}, database.name: {}", jdbcUrl, personalDatabaseName);
        this.jdbcUrl = jdbcUrl;
        this.meterTestService = meterTestService;
    }

    private boolean canConnTaoSiDataConnect() {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");

        log.info("try connect tao si database: TDengine");
        try (Connection conn = DriverManager.getConnection(jdbcUrl, connProps)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeQuery("SELECT SERVER_VERSION()");
                ResultSet rs = stmt.getResultSet();
                if (rs.next()) {
                    String version = rs.getString(1);
                    log.info("connected success! TD-engine server version: {}", version);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("can't connection TD-engine database, the database test will not exec");
            return false;
        }
    }

    @Test
    @EnabledIf("canConnTaoSiDataConnect")
    @Tag("TaoSiDataBaseConnect")
    public void TestTaoSiDataConnect() {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");

        try (Connection ignore = DriverManager.getConnection(jdbcUrl, connProps)) {
            log.info("Connected to TDengine successfully.");
        } catch (Exception ex) {
            log.error("Failed to connect to TDengine, {}ErrMessage: {}",
                    ex instanceof SQLException sqlException ? "ErrCode: " + sqlException.getErrorCode() + ", " : "",
                    ex.getMessage());
            throw new RuntimeException("Failed to connect to TDengine, " + ex.getMessage());
        }
    }

    @Test
    @EnabledIf("canConnTaoSiDataConnect")
    @Tag("TaoSiDataBaseOperation")
    public void TestTaoSiDataBaseSelectAllAndLimit() {
        log.info("select result:{}", meterTestService.find());
    }

    @Test
    @Disabled
    @Tag("TaoSiDataBaseOperation")
    public void TestTaoSiDataBaseSelectLastRow() {
        log.info("{}", meterTestService.lastRow());
    }
}
