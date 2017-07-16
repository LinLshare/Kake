package com.home77.common.base.util;

import java.text.DecimalFormat;

public final class UnitHelper {
  public static final long BYTES_PER_KB = 1024L;
  public static final long BYTES_PER_MB = BYTES_PER_KB * BYTES_PER_KB;
  public static final long BYTES_PER_GB = BYTES_PER_KB * BYTES_PER_MB;

  public static String formatBytesInByte(long size, boolean withUnit) {
    final DecimalFormat formatter;
    final double formatSize;
    final String formatUnit;

    if (size >= BYTES_PER_GB) {
      formatter = new DecimalFormat("#0.00");
      formatSize = size / (float) (BYTES_PER_GB);
      formatUnit = "GB";
    } else if (size >= BYTES_PER_MB) {
      formatter = new DecimalFormat("#0.0");
      formatSize = size / (float) (BYTES_PER_MB);
      formatUnit = "MB";
    } else if (size >= BYTES_PER_KB) {
      formatter = new DecimalFormat("#0.0");
      formatSize = size / (float) (BYTES_PER_KB);
      formatUnit = "KB";
    } else {
      formatter = new DecimalFormat("#0");
      formatSize = size;
      formatUnit = "B";
    }

    return formatter.format(formatSize) + (withUnit ? formatUnit : TextHelper.EMPTY);
  }
}
