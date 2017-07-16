package com.home77.common.net.util;

import com.home77.common.BuildConfig;
import com.home77.common.base.component.ContextManager;
import com.home77.common.base.util.SystemInfoHelper;

import java.util.Locale;

public class UserAgentHelper {

  // browser user agent

  final static String BROWSER_USER_AGENT_TEMPLATE =
      "Mozilla/5.0 (Linux; Android %s; %s-%s; %s Build/%s) " +
      "AppleWebKit/537.16 (KHTML, like Gecko) " + "Version/4.0 Mobile Safari/537.36 OD/%s";

  private static String sBrowserUserAgent;

  public static String browserUserAgent() {
    if (sBrowserUserAgent == null) {
      final Locale currentLocale = ContextManager.locale();
      sBrowserUserAgent = String.format(BROWSER_USER_AGENT_TEMPLATE,
                                        SystemInfoHelper.osVersion(),
                                        currentLocale.getLanguage(),
                                        currentLocale.getCountry(),
                                        SystemInfoHelper.device(),
                                        SystemInfoHelper.buildNumber(),
                                        BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);
    }
    return sBrowserUserAgent;
  }

  final static String APP_USER_AGENT_TEMPLATE = "OD/%s (Linux; Android %s; %s-%s; %s Build/%s)";

  private static String sAppUserAgent;

  public static String appUserAgent() {
    if (sAppUserAgent == null) {
      final Locale currentLocale = ContextManager.locale();
      sAppUserAgent = String.format(APP_USER_AGENT_TEMPLATE,
                                    BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE,
                                    SystemInfoHelper.osVersion(),
                                    currentLocale.getLanguage(),
                                    currentLocale.getCountry(),
                                    SystemInfoHelper.device(),
                                    SystemInfoHelper.buildNumber());
    }
    return sAppUserAgent;
  }
}
