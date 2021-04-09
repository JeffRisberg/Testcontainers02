package com.company;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class Main {

  @Test
  public void sampleTestMethod() throws SQLException {
    MySQLContainer<?> mysql = new MySQLContainer<>(DockerImageName.parse("mysql"));

    mysql.start();

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

      Assert.assertEquals(100, resultId);
    } catch (Exception e) {
      e.printStackTrace();
    }

    mysql.stop();
  }
}
