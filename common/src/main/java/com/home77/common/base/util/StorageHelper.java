package com.home77.common.base.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import com.home77.common.base.debug.Check;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public final class StorageHelper {
  /**
   * fetch main sdcard full file path. usually is internal storage space of
   * device
   *
   * @return path of main sd card.
   */
  public static String getMainSDCard() {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }

  /**
   * fetch ext sdcard full file path. usually is an external sdcard.
   * <p/>
   * <pre>
   *  TODO this time will not support multi-external sdcard
   * </pre>
   *
   * @return path of external sd card.
   */
  public static String getExtSDCard() {
    Map<String, String> map = System.getenv();
    if (map != null) {
      String SECONDARY_STORAGE = map.get("SECONDARY_STORAGE");
      if (SECONDARY_STORAGE == null) {
        return null;
      }
      if (SECONDARY_STORAGE.contains(":")) {
        String[] sdcards = SECONDARY_STORAGE.split(":");
        if (sdcards.length > 0) {
          return sdcards[0];
        }
      } else {
        return SECONDARY_STORAGE;
      }
    }

    return null;
  }

  /**
   * Fetch all external storage in current device.
   *
   * @return all
   */
  public static Collection<String> getExternalSDCards() {
    ArrayList<String> l = new ArrayList<>();

    String mainSDCard = getMainSDCard();
    if (!TextHelper.isEmptyOrSpaces(mainSDCard)) {
      l.add(mainSDCard);
    }

    // ). handle multi sdcard case.
    if (StorageHelper.hasMultiSDCard()) {
      l.add(getExtSDCard());
    }

    return l;
  }

  public static Collection<String> getWritableExternalSDCards() {
    Collection<String> l = getExternalSDCards();
    ArrayList<String> finalResult = new ArrayList<>();

    if (!l.isEmpty()) {
      for (String p : l) {
        // Test if have write permission on secondary storage.
        // Here can not use |canWrite()| in File.
        // Because in POSIX permission system, we have write bit in
        // POSIX permission, but actually cannot write due to Android's
        // restriction.
        if (FileHelper.touch(new File(p, ".test"))) {
          finalResult.add(p);
        }
      }
    }

    return finalResult;
  }

  /**
   * is external storage mounted
   *
   * @return true if external storage is mounted
   */
  public static boolean externalMounted() {
    return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
  }

  /**
   * is has multi sd card
   *
   * @return true if has multi sd card
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @SuppressWarnings("deprecation")
  public static boolean hasMultiSDCard() {
    if (!externalMounted()) {
      return false;
    }

    String sdcard1 = getExtSDCard();
    if (sdcard1 == null) {
      return false;
    }

    String sdcard0 = getMainSDCard();
    if (sdcard0.equals(sdcard1)) {
      return false;
    }

    String mediaMounted = "";
    File sdcard1FD = new File(sdcard1);
    if (CompatHelper.sdk(21)) {
      mediaMounted = Environment.getExternalStorageState(sdcard1FD);
    } else if (CompatHelper.sdk(19)) {
      mediaMounted = Environment.getStorageState(sdcard1FD);
    }

    if (!TextHelper.isEmptyOrSpaces(mediaMounted) //
        && !Environment.MEDIA_MOUNTED.equals(mediaMounted)) {
      return false;
    } else if (!sdcard1FD.canRead()) {
      return false;
    }

    return true;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  @SuppressWarnings("deprecation")
  private static long getAvailableSize(String path) {
    long value = 0L;
    try {
      StatFs stat = new StatFs(path);
      long blockSize = CompatHelper.sdk(18) ? stat.getBlockSizeLong() : stat.getBlockSize();
      long availableBlocks =
          CompatHelper.sdk(18) ? stat.getAvailableBlocksLong() : stat.getAvailableBlocks();
      value = availableBlocks * blockSize;
    } catch (Exception e) {
      Check.d(e);
    }
    return value;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  @SuppressWarnings("deprecation")
  private static long getTotalSize(String path) {
    long value = 0L;
    try {
      StatFs stat = new StatFs(path);
      long blockSize = CompatHelper.sdk(19) ? stat.getBlockSizeLong() : stat.getBlockSize();
      long totalBlocks = CompatHelper.sdk(19) ? stat.getBlockCountLong() : stat.getBlockCount();
      value = totalBlocks * blockSize;
    } catch (Exception e) {
      Check.d(e);
    }
    return value;
  }

  /**
   * fetch available size associate to |path|
   */
  public static long getFileAvailableSize(String path) {
    return getAvailableSize(path);
  }

  /**
   * fetch total size associate to |path|
   */
  public static long getFileTotalSize(String path) {
    return getTotalSize(path);
  }

  /**
   * fetch rom available size
   */
  public static long getAvailableInternalMemorySize() {
    File path = Environment.getDataDirectory();
    return getAvailableSize(path.getAbsolutePath());
  }

  /**
   * fetch rom total size
   */
  public static long getTotalInternalMemorySize() {
    File path = Environment.getDataDirectory();
    return getTotalSize(path.getAbsolutePath());
  }

  /**
   * fetch external available size
   */
  public static long getAvailableExternalMemorySize() {
    if (!externalMounted()) {
      return 0L;
    }
    File path = Environment.getExternalStorageDirectory();
    return getAvailableSize(path.getAbsolutePath());
  }

  /**
   * fetch external total size
   */
  public static long getTotalExternalMemorySize() {
    if (!externalMounted()) {
      return 0L;
    }
    File path = Environment.getExternalStorageDirectory();
    return getTotalSize(path.getAbsolutePath());
  }
}
