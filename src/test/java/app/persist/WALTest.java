package app.persist;

import app.cache.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WALTest {

  private String walFileLocation = "abc.txt";
  private WAL wal;

  @BeforeEach
  public void setUp() throws FileNotFoundException {
    //File file = new File(walFileLocation);
    //file.delete();
    wal = WAL.getInstance(walFileLocation);
  }

  @Test
  public void shouldReadFromWalFile() throws IOException {

    Entry expectedEntry = new Entry("abc", "xyz");
    wal.append(expectedEntry);

    FileInputStream inputStream = wal.prepareForRead(walFileLocation);
    Entry actualEntry = wal.readEntry(inputStream);

    assertEquals(expectedEntry, actualEntry);
  }

  @Test
  @Disabled
  public void shouldReadOverriddenValueFromWalFile() throws IOException {

    Entry entry1 = new Entry("abc", "xyz");
    wal.append(entry1);
    Entry entry2 = new Entry("abc", "xy4");
    wal.append(entry2);
    Entry entry3 = new Entry("abc", "xy43435252");
    wal.append(entry3);

    FileInputStream inputStream = wal.prepareForRead(walFileLocation);
    Entry actualEntry1 = wal.readEntry(inputStream);
    Entry actualEntry2 = wal.readEntry(inputStream);
    Entry actualEntry3 = wal.readEntry(inputStream);

    assertEquals(entry1, actualEntry1);
    assertEquals(entry2, actualEntry2);
    assertEquals(entry3, actualEntry3);
  }
}