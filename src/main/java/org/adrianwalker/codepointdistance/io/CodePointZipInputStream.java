package org.adrianwalker.codepointdistance.io;

import org.adrianwalker.codepointdistance.parser.CodePointParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.adrianwalker.codepointdistance.model.CodePoint;

public final class CodePointZipInputStream {

  public static final String DATA_FILE_EXTENSION = ".csv";
  public static final String DATA_DIRECTORY = "Data/CSV";

  private final ZipInputStream zis;
  private final CodePointParser cpp;

  public CodePointZipInputStream(final ZipInputStream zis) throws IOException {

    this.zis = zis;
    this.cpp = new CodePointParser(new InputStreamReader(zis));
  }

  public Iterable<CodePoint> parse() throws IOException {

    return cpp.parse();
  }

  public ZipEntry getNextCsvEntry() throws IOException {

    ZipEntry entry = zis.getNextEntry();

    if (null == entry) {
      return null;
    }

    while (!isCsvEntry(entry)) {
      entry = zis.getNextEntry();
    }

    return entry;
  }

  private boolean isCsvEntry(final ZipEntry entry) {

    return entry.getName().contains(DATA_DIRECTORY) && entry.getName().endsWith(DATA_FILE_EXTENSION);
  }
}
