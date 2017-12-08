package com.akolb;

import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.thrift.TException;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static com.akolb.Util.addManyPartitions;
import static com.akolb.Util.createSchema;
import static com.akolb.Util.getServerUri;
import static com.akolb.Util.makeTable;

@State(Scope.Thread)
public class JMHBenchmark {
  private static final Logger LOG = LoggerFactory.getLogger(JMHBenchmark.class);

  private static final String TEST_TABLE = "bench_table1";
  private static final int NOBJECTS = 1000;
  private static final String PROP_HOST = "hms.host";
  private static final String PROP_DATABASE = "db.name";
  private static final String PROP_TABLE = "table.name";


  private HMSClient client;
  private String dbName;
  private String tableName;
  List<FieldSchema> tableSchema;
  List<FieldSchema> partitionSchema;
  private Table table;

  public static void main(String[] args) throws RunnerException, TException {

    HMSClient client = null;
    String host = System.getProperty(PROP_HOST);
    if (host == null) {
      LOG.error("Missing hostname");
      System.exit(1);
    }


    LOG.info("host = {}", host);
    try {
      client = new HMSClient(getServerUri(host));
    } catch (IOException e) {
      LOG.error("Failed to connect to HMS", e);
      System.exit(1);
    } catch (InterruptedException e) {
      LOG.error("Interrupted while connecting to HMS", e);
      System.exit(1);
    } catch (LoginException e) {
      LOG.error("Failed to perform Kerberos login", e);
    } catch (URISyntaxException e) {
      LOG.error("Invalid URI syntax", e);
    }
    String dbName = System.getProperty(PROP_DATABASE);
    String tableName = System.getProperty(PROP_TABLE);

    if (dbName == null || dbName.isEmpty()) {
      throw new RuntimeException("Missing DB name");
    }
    if (tableName == null || tableName.isEmpty()) {
      throw new RuntimeException("Missing Table name");
    }

    LOG.info("Using table '{}.{}'", dbName, tableName);

    if (!client.dbExists(dbName)) {
      client.createDatabase(dbName);
    }

    if (client.tableExists(dbName, tableName)) {
      client.dropTable(dbName, tableName);
    }

    Options opt = new OptionsBuilder()
        .include(JMHBenchmark.class.getSimpleName())
        .forks(1)
        .verbosity(VerboseMode.NORMAL)
        .mode(Mode.AverageTime)
        .build();

    new Runner(opt).run();
  }

  @Setup
  public void setup() throws TException, IOException, InterruptedException, LoginException, URISyntaxException {
    Logger LOG = LoggerFactory.getLogger(JMHBenchmark.class);
    tableName = System.getProperty(PROP_TABLE);
    dbName = System.getProperty(PROP_DATABASE);
    String server = System.getProperty(PROP_HOST);
    LOG.info("Using server " + server + " table '" + dbName + "." + tableName + "'");
    client = new HMSClient(getServerUri(server));
    table = makeTable(dbName, tableName, null, null);
    LOG.info("Create partitioned table {}.{}", dbName, TEST_TABLE);
    client.createTable(makeTable(dbName, TEST_TABLE,
        createSchema(Collections.singletonList("name:string")),
        createSchema(Collections.singletonList("date"))));

  }

  @TearDown
  public void teardown() throws Exception {
    Logger LOG = LoggerFactory.getLogger(JMHBenchmark.class);
    LOG.info("dropping table {}.{}", dbName, TEST_TABLE);
    client.dropTable(dbName, TEST_TABLE);
  }

  @Benchmark
  public void createTable() throws TException {
    client.createTable(table);
    client.dropTable(dbName, tableName);
  }

  @Benchmark
  public void getAllDatabases() {
    client.getAllDatabasesNoException();
  }

  @Benchmark
  public void getAllTables() {
    client.getAllTablesNoException(dbName);
  }

  @Benchmark
  public void createDropPartitions() {
    try {
      addManyPartitions(client, dbName, TEST_TABLE,
          Collections.singletonList("d"), NOBJECTS);
      client.dropPartitions(dbName, TEST_TABLE, null);
    } catch (TException e) {
      e.printStackTrace();
    }
  }

}
