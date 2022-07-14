package app.persist;

import app.cache.Entry;

import java.io.*;
import java.nio.ByteBuffer;

public class WAL {

  private FileOutputStream outputStream;
  private static volatile WAL INSTANCE;

  private WAL(String walFileLocation) throws FileNotFoundException {
    outputStream = new FileOutputStream(walFileLocation, true);
  }

  public static WAL getInstance(String walFileLocation) throws FileNotFoundException {
    if(INSTANCE == null) {
      INSTANCE = new WAL(walFileLocation);
    }
    return INSTANCE;
  }

  public void append(Entry entry) throws IOException {
    int keySize = entry.getKey().getBytes().length;
    int valueSize = entry.getValue().getBytes().length;
    outputStream.write(ByteBuffer.allocate(Integer.BYTES).putInt(keySize).array());
    outputStream.write(entry.getKey().getBytes());
    outputStream.write(ByteBuffer.allocate(Integer.BYTES).putInt(valueSize).array());
    outputStream.write(entry.getValue().getBytes());
    outputStream.getFD().sync();
  }

  public FileInputStream prepareForRead(String walFileLocation) throws FileNotFoundException {
    return new FileInputStream(walFileLocation);
  }

  public Entry readEntry(InputStream inputStream) throws IOException {

    int keySize = ByteBuffer.wrap(inputStream.readNBytes(Integer.BYTES)).getInt();
    String key = new String(inputStream.readNBytes(keySize));
    int valueSize = ByteBuffer.wrap(inputStream.readNBytes(Integer.BYTES)).getInt();
    String value = new String(inputStream.readNBytes(valueSize));

    return new Entry(key, value);
  }
}
