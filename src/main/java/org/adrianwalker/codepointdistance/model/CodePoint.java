package org.adrianwalker.codepointdistance.model;

import java.util.Objects;

public final class CodePoint {

  private Postcode postcode;
  private long eastings;
  private long northings;

  public Postcode getPostcode() {
    return postcode;
  }

  public CodePoint setPostcode(final Postcode postcode) {
    this.postcode = postcode;
    return this;
  }

  public long getEastings() {
    return eastings;
  }

  public CodePoint setEastings(final long eastings) {
    this.eastings = eastings;
    return this;
  }

  public long getNorthings() {
    return northings;
  }

  public CodePoint setNorthings(final long northings) {
    this.northings = northings;
    return this;
  }
  
  @Override
  public String toString() {
    return "postcode=" + postcode + ", eastings=" + eastings + ", northings=" + northings;
  }

  @Override
  public int hashCode() {

    int hash = 5;
    hash = 59 * hash + Objects.hashCode(this.postcode);
    hash = 59 * hash + (int) (this.eastings ^ (this.eastings >>> 32));
    hash = 59 * hash + (int) (this.northings ^ (this.northings >>> 32));

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

    CodePoint other = (CodePoint) obj;

    if (this.eastings != other.eastings) {
      return false;
    }

    if (this.northings != other.northings) {
      return false;
    }

    return Objects.equals(this.postcode, other.postcode);
  }
}
