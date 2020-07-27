package org.adrianwalker.codepointdistance.parser;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import org.adrianwalker.codepointdistance.model.CodePoint;
import org.adrianwalker.codepointdistance.model.Postcode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public final class CodePointParser {

  public static final int POSTCODE_INDEX = 0;
  public static final int EASTINGS_INDEX = 2;
  public static final int NORTHINGS_INDEX = 3;

  private final Reader reader;

  public CodePointParser(final Reader reader) {

    this.reader = reader;
  }

  public Iterable<CodePoint> parse() throws IOException {

    Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
    Iterator<CSVRecord> it = records.iterator();

    return () -> new Iterator<CodePoint>() {

      @Override
      public boolean hasNext() {

        return it.hasNext();
      }

      @Override
      public CodePoint next() {

        CSVRecord record = it.next();

        return new CodePoint()
          .setPostcode(new Postcode().setValue(record.get(POSTCODE_INDEX)))
          .setEastings(Long.valueOf(record.get(EASTINGS_INDEX)))
          .setNorthings(Long.valueOf(record.get(NORTHINGS_INDEX)));
      }
    };
  }
}
