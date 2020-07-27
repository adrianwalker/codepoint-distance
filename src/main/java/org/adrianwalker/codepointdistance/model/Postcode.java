package org.adrianwalker.codepointdistance.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Objects;
import java.util.regex.Pattern;
import org.adrianwalker.codepointdistance.rest.PostcodeSerializer;

@JsonSerialize(using = PostcodeSerializer.class)
public final class Postcode {

  private static final Pattern WHITESPACE = Pattern.compile("\\s+");
  private static final String SPACE = " ";
  private static final String EMPTY = "";

  private String value;

  public String getValue() {
    return value;
  }

  public Postcode setValue(final String value) {
    this.value = value;
    return this;
  }

  public Postcode format() {

    value = WHITESPACE.matcher(value).replaceAll(EMPTY);

    int length = value.length();

    if (length < 3) {
      return this;
    }

    String outward = value.substring(0, length - 3);
    String inward = value.substring(length - 3, length);

    value = outward;

    if (!value.isEmpty()) {
      value = value + SPACE;
    }

    value = value + inward;
    value = value.toUpperCase();

    return this;
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  public int hashCode() {

    int hash = 7;
    hash = 97 * hash + Objects.hashCode(this.value);

    return hash;
  }

  @Override
  public boolean equals(final Object obj) {

    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    return Objects.equals(this.value, ((Postcode) obj).value);
  }
}
