package com.ikhokha.techcheck;

public class Metrics {
  private MetricsType type;
  private String value;
  private Integer length;
  private String key;

  public Metrics(String key, MetricsType type, String value) {
    this.type = type;
    this.value = value;
    this.key = key;
  }

  public Metrics(String key, MetricsType type, Integer count) {
    this.type = type;
    this.length = count;
    this.key = key;
  }

  public String getKey() {
    return this.key;
  }

  public boolean testLine(String line) {
    switch(type) {
      case LESS: 
        return (line.length() < length);
      case GRATER:
        return(line.length() >length);
      case EQUAL:
        return (line.length() == length);
      case CONTAINS:
       return (line.contains(value));
      default: 
        return false;
    }
  }
}

