package com.bupt.Jungle.FinancialDataAnalysis;

import com.taosdata.jdbc.TSDBDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.Properties;

@SpringBootTest
@Slf4j
class FinancialDataAnalysisBootApplicationTests {
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    public boolean canConnTaoSiDataConnect() {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");

        try (Connection conn = DriverManager.getConnection(jdbcUrl, connProps)) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeQuery("select server_version();");
                ResultSet rs = stmt.getResultSet();
                while (rs.next()) {
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
    public void TestTaoSiDataConnect() {
        Properties connProps = new Properties();
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");
        connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");

        try (Connection ignore = DriverManager.getConnection(jdbcUrl, connProps)) {
            log.info("Connected to {} successfully.", jdbcUrl);
        } catch (Exception ex) {
            log.error("Failed to connect to {}, {}ErrMessage: {}",
                    jdbcUrl,
                    ex instanceof SQLException ? "ErrCode: " + ((SQLException) ex).getErrorCode() + ", " : "",
                    ex.getMessage());
        }
    }

}
