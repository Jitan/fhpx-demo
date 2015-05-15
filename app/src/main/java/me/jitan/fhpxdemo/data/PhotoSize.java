package me.jitan.fhpxdemo.data;

public enum PhotoSize {
  THUMB("3"),
  NORMAL("1080"),
  LARGE("2048");

  private final String value;

  PhotoSize(String value) {
    this.value = value;
  }

  @Override public String toString() {
    return value;
  }
}
