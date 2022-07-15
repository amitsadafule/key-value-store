package app.cache;

import app.persist.WAL;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class Cache {

  private static WAL wal;
  private static Cache cache = new Cache();
  private static Map<String, String> memTable;
  private static final String WAL_FILE_LOCATION = "wal.txt";

  private Cache() {
    try {
      wal = WAL.getInstance(WAL_FILE_LOCATION);
      memTable = new ConcurrentHashMap<>();
      populateMemTable();
    } catch (IOException e) {
      log.error("Error while opening wal file", e);
      System.exit(1);
    }
  }

  public static Cache getInstance() {
    return cache;
  }

  public String get(String key) {
    return memTable.get(key);
  }

  public String set(String key, String value) throws IOException {
    wal.append(new Entry(key, value));
    memTable.put(key, value);
    return value;
  }

  private void populateMemTable() throws IOException {
    InputStream inputStream = wal.prepareForRead(WAL_FILE_LOCATION);
    while (inputStream.available() != 0) {
      Entry entry = wal.readEntry(inputStream);
      memTable.put(entry.getKey(), entry.getValue());
    }
  }
}
