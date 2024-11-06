package com.bupt.Jungle.FinancialDataAnalysis;

import com.bupt.Jungle.FinancialDataAnalysis.service.MeterServiceTest;
import com.taosdata.jdbc.TSDBDriver;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.*;
import java.util.Properties;

@SpringBootTest(properties = "spring.datasource.url=jdbc:TAOS-RS://${personal.remoteService.ip}:${personal.remoteService.port}/test?useSSL=false&user=${personal.login.name}&password=${personal.login.password}&local=zh_CN.UTF-8&charset=UTF-8&timezone=Asia/Shanghai")
@Slf4j
@MapperScan("com.bupt.Jungle.FinancialDataAnalysis.mapper")
class TDEngineDataBaseTest {
    private final String jdbcUrl;

    private final MeterServiceTest meterServiceTest;

    @Autowired
    public TDEngineDataBaseTest(@Value("${spring.datasource.url}") String jdbcUrl, MeterServiceTest meterServiceTest) {
        this.jdbcUrl = jdbcUrl;
        this.meterServiceTest = meterServiceTest;
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
                    ex instanceof SQLException ? "ErrCode: " + ((SQLException) ex).getErrorCode() + ", " : "",
                    ex.getMessage());
            throw new RuntimeException("Failed to connect to TDengine, " + ex.getMessage());
        }
    }

    @Test
    @EnabledIf("canConnTaoSiDataConnect")
    @Tag("TaoSiDataBaseOperation")
    public void TestTaoSiDataBaseSelectAllAndLimit() {
        log.info("select result:{}", meterServiceTest.find());
    }

    @Test
    @EnabledIf("canConnTaoSiDataConnect")
    @Tag("TaoSiDataBaseOperation")
    public void TestTaoSiDataBaseSelectLastRow() {
        log.info("{}", meterServiceTest.lastRow());
    }
}
