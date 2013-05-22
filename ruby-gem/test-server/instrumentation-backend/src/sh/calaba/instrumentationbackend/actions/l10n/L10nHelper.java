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
   * @param pckg Optional package to find the resource, defaults to the application's package if null
   * @return The translated value.
   */
  public static String getValue(String l10nKey, String pckg) {
    
    if(pckg == null){
        pckg = InstrumentationBackend.solo.getCurrentActivity().getPackageName();
    }
      
    int resourceId =
        InstrumentationBackend.solo
            .getCurrentActivity()
            .getResources()
            .getIdentifier(l10nKey, "string", pckg);

    String localizedString =
        InstrumentationBackend.solo.getCurrentActivity().getResources().getString(resourceId);

    return localizedString;
  }
}
