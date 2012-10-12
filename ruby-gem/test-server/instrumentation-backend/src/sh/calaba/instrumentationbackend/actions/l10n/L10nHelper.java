package sh.calaba.instrumentationbackend.actions.l10n;

import sh.calaba.instrumentationbackend.InstrumentationBackend;

/**
 * Helper to access Android L10n files.
 * 
 * @author Dominik Dary
 * 
 */
public class L10nHelper {
  /**
   * get the translated value based on the current active locale.
   * 
   * @param l10nKey The l10n key to use
   * @return The translated value.
   */
  public static String getValue(String l10nKey) {
    int resourceId =
        InstrumentationBackend.solo
            .getCurrentActivity()
            .getResources()
            .getIdentifier(l10nKey, "string",
                InstrumentationBackend.solo.getCurrentActivity().getPackageName());

    String localizedString =
        InstrumentationBackend.solo.getCurrentActivity().getResources().getString(resourceId);

    return localizedString;
  }
}
