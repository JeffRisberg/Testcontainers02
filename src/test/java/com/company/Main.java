package com.company;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import java.sql.*;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testng.Assert;
import org.testng.annotations.*;

@Slf4j
public class Main {
  private static SystemLambda.WithEnvironmentVariables withEnvironmentVariables;
  private static MySQLContainer<?> mysql;

  @BeforeClass
  public static void setup() throws Exception {

    withEnvironmentVariables = withEnvironmentVariable("TESTCONTAINERS_RYUK_DISABLED", "true");

    withEnvironmentVariables.execute(() -> {
      System.out.println(System.getenv("TESTCONTAINERS_RYUK_DISABLED"));

      mysql = new MySQLContainer<>(DockerImageName.parse("mysql"));

      mysql.start();
    });
  }

  @AfterClass
  public static void teardown() {
    mysql.start();
  }

  @Test
  public void sampleTestMethod1() throws SQLException {
    try (Connection conn = mysql.createConnection("")) {
      Statement stmt = conn.createStatement();

      // create sql table
      String sql =
          "CREATE TABLE registration "
              + "(id INTEGER NOT NULL, "
              + " first VARCHAR(255), "
              + " last VARCHAR(255), "
              + " age INTEGER, "
              + " PRIMARY KEY (id))";

      log.info(sql);
      stmt.executeUpdate(sql);

      // insert records in table 'registration'
      String insertSQL1 = "INSERT INTO registration VALUES (100, 'Luke', 'Skywalker', 20)";
      log.info(insertSQL1);
      stmt.executeUpdate(insertSQL1);

      String insertSQL2 = "INSERT INTO registration VALUES (101, 'Frodo', 'Baggins', 110)";
      log.info(insertSQL2);
      stmt.executeUpdate(insertSQL2);

      // fetching and asserting the record
      ResultSet result = stmt.executeQuery("select id, first from registration");
      result.next();

      int resultId = result.getInt(1);
      String resultFirst = result.getString(2);

      log.info("Result id=" + resultId + ", first=" + resultFirst);

      Assert.assertEquals(resultId, 100);
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void sampleTestMethod2() throws SQLException {
    try (Connection conn = mysql.createConnection("")) {
      Statement stmt = conn.createStatement();

      // create sql table
      String sql =
          "CREATE TABLE data_sources "
              + "(id INTEGER NOT NULL, "
              + " name VARCHAR(255), "
              + " content_type_name VARCHAR(255), "
              + " PRIMARY KEY (id))";

      stmt.executeUpdate(sql);

      // insert records in table 'data_sources'
      String insertSQL1 = "INSERT INTO data_sources VALUES (1, 'SNOW 2785', 'Tickets')";
      stmt.executeUpdate(insertSQL1);

      String insertSQL2 = "INSERT INTO data_sources VALUES (2, 'SNOW 2785', 'Users')";
      stmt.executeUpdate(insertSQL2);

      // fetching and asserting the record
      ResultSet result = stmt.executeQuery("select id, name, content_type_name from data_sources");
      result.next();

      int resultId = result.getInt(1);
      String resultName = result.getString(2);
      String resultContentType = result.getString(3);

      log.info("Result id=" + resultId + ", name=" + resultName + ", contentType=" + resultContentType);

      Assert.assertEquals(resultId, 1);
      Assert.assertEquals(resultName, "SNOW 2785");
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}
