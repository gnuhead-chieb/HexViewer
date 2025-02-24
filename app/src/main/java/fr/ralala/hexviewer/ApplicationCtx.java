package fr.ralala.hexviewer;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import java.util.Locale;

import androidx.emoji.bundled.BundledEmojiCompatConfig;
import androidx.emoji.text.EmojiCompat;
import androidx.preference.PreferenceManager;

import fr.ralala.hexviewer.models.ListSettings;
import fr.ralala.hexviewer.models.RecentlyOpened;
import fr.ralala.hexviewer.models.SettingsKeys;

/**
 * ******************************************************************************
 * <p><b>Project HexViewer</b><br/>
 * Main application context
 * </p>
 *
 * @author Keidan
 * <p>
 * License: GPLv3
 * </p>
 * ******************************************************************************
 */
public class ApplicationCtx extends Application implements SettingsKeys {
  private SharedPreferences mSharedPreferences;
  private boolean mDefaultSmartInput;
  private boolean mDefaultOverwrite;
  private static ApplicationCtx instance;
  private String mLanguage = null;
  private boolean mDefaultLinesNumber;
  private ListSettings mListSettingsHexPortrait;
  private ListSettings mListSettingsHexLandscape;
  private ListSettings mListSettingsHexLineNumbersPortrait;
  private ListSettings mListSettingsHexLineNumbersLandscape;
  private ListSettings mListSettingsPlainPortrait;
  private ListSettings mListSettingsPlainLandscape;
  private ListSettings mListSettingsLineEditPortrait;
  private ListSettings mListSettingsLineEditLandscape;
  private String mDefaultScreenOrientation;
  private String mDefaultNbBytesPerLine;
  private RecentlyOpened mRecentlyOpened;
  private boolean mSequential = false;
  private String mDefaultMemoryThreshold;
  private boolean mDefaultPartialOpenButWholeFileIsOpened;

  public static ApplicationCtx getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    mDefaultSmartInput = Boolean.parseBoolean(getString(R.string.default_smart_input));
    mRecentlyOpened = new RecentlyOpened(this);
    mDefaultLinesNumber = Boolean.parseBoolean(getString(R.string.default_lines_number));
    mDefaultOverwrite = Boolean.parseBoolean(getString(R.string.default_overwrite));
    mDefaultScreenOrientation = getString(R.string.default_screen_orientation);
    mDefaultNbBytesPerLine = getString(R.string.default_nb_bytes_per_line);
    mDefaultMemoryThreshold = getString(R.string.default_memory_threshold);
    mDefaultPartialOpenButWholeFileIsOpened = Boolean.parseBoolean(getString(R.string.default_partial_open_but_whole_file_is_opened));

    mListSettingsHexPortrait = new ListSettings(this,
        CFG_PORTRAIT_HEX_DISPLAY_DATA,
        CFG_PORTRAIT_HEX_ROW_HEIGHT, CFG_PORTRAIT_HEX_ROW_HEIGHT_AUTO, CFG_PORTRAIT_HEX_FONT_SIZE,
        R.string.default_hex_display_data_portrait,
        R.string.default_hex_row_height_portrait, R.string.default_hex_row_height_auto_portrait,
        R.string.default_hex_font_size_portrait);
    mListSettingsHexLandscape = new ListSettings(this,
        CFG_LANDSCAPE_HEX_DISPLAY_DATA,
        CFG_LANDSCAPE_HEX_ROW_HEIGHT, CFG_LANDSCAPE_HEX_ROW_HEIGHT_AUTO, CFG_LANDSCAPE_HEX_FONT_SIZE,
        R.string.default_hex_display_data_landscape,
        R.string.default_hex_row_height_landscape, R.string.default_hex_row_height_auto_landscape,
        R.string.default_hex_font_size_landscape);

    mListSettingsHexLineNumbersPortrait = new ListSettings(this,
        CFG_PORTRAIT_HEX_DISPLAY_DATA_LINE_NUMBERS,
        CFG_PORTRAIT_HEX_ROW_HEIGHT_LINE_NUMBERS, CFG_PORTRAIT_HEX_ROW_HEIGHT_AUTO_LINE_NUMBERS, CFG_PORTRAIT_HEX_FONT_SIZE_LINE_NUMBERS,
        R.string.default_hex_display_data_portrait_lines_numbers,
        R.string.default_hex_row_height_portrait_lines_numbers, R.string.default_hex_row_height_auto_portrait_lines_numbers,
        R.string.default_hex_font_size_portrait_lines_numbers);
    mListSettingsHexLineNumbersLandscape = new ListSettings(this,
        CFG_LANDSCAPE_HEX_DISPLAY_DATA_LINE_NUMBERS,
        CFG_LANDSCAPE_HEX_ROW_HEIGHT_LINE_NUMBERS, CFG_LANDSCAPE_HEX_ROW_HEIGHT_AUTO_LINE_NUMBERS, CFG_LANDSCAPE_HEX_FONT_SIZE_LINE_NUMBERS,
        R.string.default_hex_display_data_landscape_lines_numbers,
        R.string.default_hex_row_height_landscape_lines_numbers, R.string.default_hex_row_height_auto_landscape_lines_numbers,
        R.string.default_hex_font_size_landscape_lines_numbers);

    mListSettingsPlainPortrait = new ListSettings(this,
        null,
        CFG_PORTRAIT_PLAIN_ROW_HEIGHT, CFG_PORTRAIT_PLAIN_ROW_HEIGHT_AUTO, CFG_PORTRAIT_PLAIN_FONT_SIZE,
        0, R.string.default_plain_row_height_portrait, R.string.default_plain_row_height_auto_portrait,
        R.string.default_plain_font_size_portrait);
    mListSettingsPlainLandscape = new ListSettings(this,
        null,
        CFG_LANDSCAPE_PLAIN_ROW_HEIGHT, CFG_LANDSCAPE_PLAIN_ROW_HEIGHT_AUTO, CFG_LANDSCAPE_PLAIN_FONT_SIZE,
        0, R.string.default_plain_row_height_landscape, R.string.default_plain_row_height_auto_landscape,
        R.string.default_plain_font_size_landscape);

    mListSettingsLineEditPortrait = new ListSettings(this,
        null,
        CFG_PORTRAIT_LINE_EDIT_ROW_HEIGHT, CFG_PORTRAIT_LINE_EDIT_ROW_HEIGHT_AUTO, CFG_PORTRAIT_LINE_EDIT_FONT_SIZE,
        0, R.string.default_line_edit_row_height_portrait, R.string.default_line_edit_row_height_auto_portrait,
        R.string.default_line_edit_font_size_portrait);
    mListSettingsLineEditLandscape = new ListSettings(this,
        null,
        CFG_LANDSCAPE_LINE_EDIT_ROW_HEIGHT, CFG_LANDSCAPE_LINE_EDIT_ROW_HEIGHT_AUTO, CFG_LANDSCAPE_LINE_EDIT_FONT_SIZE,
        0, R.string.default_line_edit_row_height_landscape, R.string.default_line_edit_row_height_auto_landscape,
        R.string.default_line_edit_font_size_landscape);
    /* EmojiCompat */
    EmojiCompat.Config config = new BundledEmojiCompatConfig(this);
    EmojiCompat.init(config);
    loadDefaultLocal();
    setApplicationLanguage(mLanguage);
  }

  public SharedPreferences getPref(final Context context) {
    if (mSharedPreferences == null)
      mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    return mSharedPreferences;
  }

  /**
   * Changes the sequential flag.
   *
   * @param seq The new value
   */
  public void setSequential(boolean seq) {
    mSequential = seq;
  }

  /**
   * Test the sequential flag.
   *
   * @return boolean
   */
  public boolean isSequential() {
    return mSequential;
  }

  /* ---------- Settings ---------- */

  /**
   * Returns the memory threshold.
   *
   * @return int
   */
  public int getMemoryThreshold() {
    try {
      String s = getPref(this).getString(CFG_MEMORY_THRESHOLD, mDefaultMemoryThreshold);
      if (s.startsWith("~"))
        s = s.substring(1);
      if (s.endsWith("%"))
        s = s.substring(0, s.length() - 1);
      return Integer.parseInt(s);
    } catch (Exception ignore) {
      return Integer.parseInt(mDefaultMemoryThreshold);
    }
  }

  /**
   * Changes the default state specifying whether the source section (LineEdit) is expanded or collapsed.
   *
   * @param expanded The new value.
   */
  public void setLineEditSrcExpanded(boolean expanded) {
    SharedPreferences.Editor e = getPref(this).edit();
    e.putBoolean(CFG_LINE_EDIT_SRC_EXPAND, expanded);
    e.apply();
  }

  /**
   * If a partial opening is requested but the whole file is opened, it will be considered as a normal opening.
   *
   * @return boolean
   */
  public boolean isPartialOpenButWholeFileIsOpened() {
    return getPref(this).getBoolean(CFG_PARTIAL_OPEN_BUT_WHOLE_FILE_IS_OPENED, mDefaultPartialOpenButWholeFileIsOpened);
  }

  /**
   * Returns the default state specifying whether the source section (LineEdit) is expanded or collapsed.
   *
   * @return boolean
   */
  public boolean isLineEditSrcExpanded() {
    return getPref(this).getBoolean(CFG_LINE_EDIT_SRC_EXPAND, true);
  }

  /**
   * Changes the default state specifying whether the result section (LineEdit) is expanded or collapsed.
   *
   * @param expanded The new value.
   */
  public void setLineEditRstExpanded(boolean expanded) {
    SharedPreferences.Editor e = getPref(this).edit();
    e.putBoolean(CFG_LINE_EDIT_RST_EXPAND, expanded);
    e.apply();
  }

  /**
   * Returns the default state specifying whether the result section (LineEdit) is expanded or collapsed.
   *
   * @return boolean
   */
  public boolean isLineEditRstExpanded() {
    return getPref(this).getBoolean(CFG_LINE_EDIT_RST_EXPAND, true);
  }

  /**
   * Sets the number of bytes per line.
   *
   * @param nb The new value.
   */
  public void setNbBytesPerLine(String nb) {
    SharedPreferences.Editor e = getPref(this).edit();
    e.putString(CFG_NB_BYTES_PER_LINE, nb);
    e.apply();
  }

  /**
   * Returns the number of bytes per line.
   *
   * @return SysHelper.MAX_BY_ROW_8 or SysHelper.MAX_BY_ROW_16
   */
  public int getNbBytesPerLine() {
    try {
      return Integer.parseInt(getPref(this).getString(CFG_NB_BYTES_PER_LINE, mDefaultNbBytesPerLine));
    } catch (Exception ignore) {
      return Integer.parseInt(mDefaultNbBytesPerLine);
    }
  }

  /**
   * Returns the orientation of the screen according to the configuration.
   *
   * @return SCREEN_ORIENTATION_LANDSCAPE,
   * SCREEN_ORIENTATION_PORTRAIT or
   * SCREEN_ORIENTATION_UNSPECIFIED
   */
  public String getScreenOrientationStr() {
    return getPref(this).getString(CFG_SCREEN_ORIENTATION, mDefaultScreenOrientation);
  }

  /**
   * Returns the orientation of the screen according to the configuration.
   *
   * @param ref The reference value, if it is null, the value stored in the parameters will be used.
   * @return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE,
   * ActivityInfo.SCREEN_ORIENTATION_PORTRAIT or
   * ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
   */
  public int getScreenOrientation(final String ref) {
    String s = ref != null ? ref : getScreenOrientationStr();
    if (s.equals("SCREEN_ORIENTATION_LANDSCAPE"))
      return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    if (s.equals("SCREEN_ORIENTATION_PORTRAIT"))
      return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
  }

  /**
   * Tests if the line numbers are displayed or not.
   *
   * @return bool
   */
  public boolean isLineNumber() {
    try {
      return getPref(this).getBoolean(CFG_LINES_NUMBER, mDefaultLinesNumber);
    } catch (Exception ignore) {
      return mDefaultLinesNumber;
    }
  }

  /**
   * Show/Hide the line numbers.
   *
   * @param mode The new mode.
   */
  public void setLineNumber(boolean mode) {
    SharedPreferences.Editor e = getPref(this).edit();
    e.putBoolean(CFG_LINES_NUMBER, mode);
    e.apply();
  }

  /**
   * Returns the list of recently opened files.
   *
   * @return RecentlyOpened
   */
  public RecentlyOpened getRecentlyOpened() {
    return mRecentlyOpened;
  }

  /**
   * Test if smart input is enabled or not.
   *
   * @return bool
   */
  public boolean isSmartInput() {
    try {
      return getPref(this).getBoolean(CFG_SMART_INPUT, mDefaultSmartInput);
    } catch (Exception ignore) {
      return mDefaultSmartInput;
    }
  }

  /**
   * Enable/Disable the smart input.
   *
   * @param mode The new mode.
   */
  public void setSmartInput(boolean mode) {
    SharedPreferences.Editor e = getPref(this).edit();
    e.putBoolean(CFG_SMART_INPUT, mode);
    e.apply();
  }

  /**
   * Test if overwrite is enabled or not.
   *
   * @return bool
   */
  public boolean isOverwrite() {
    try {
      return getPref(this).getBoolean(CFG_OVERWRITE, mDefaultOverwrite);
    } catch (Exception ignore) {
      return mDefaultOverwrite;
    }
  }

  /**
   * Enable/Disable the overwrite mode.
   *
   * @param mode The new mode.
   */
  public void setOverwrite(boolean mode) {
    SharedPreferences.Editor e = getPref(this).edit();
    e.putBoolean(CFG_OVERWRITE, mode);
    e.apply();
  }

  /**
   * Returns the list settings for the hex list (portrait).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsHexPortrait() {
    return mListSettingsHexPortrait;
  }

  /**
   * Returns the list settings for the hex list (landscape).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsHexLandscape() {
    return mListSettingsHexLandscape;
  }

  /**
   * Returns the list settings for the hex list with line numbers(portrait).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsHexLineNumbersPortrait() {
    return mListSettingsHexLineNumbersPortrait;
  }

  /**
   * Returns the list settings for the hex list with line numbers(landscape).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsHexLineNumbersLandscape() {
    return mListSettingsHexLineNumbersLandscape;
  }

  /**
   * Returns the list settings for the plain text list(portrait).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsPlainPortrait() {
    return mListSettingsPlainPortrait;
  }

  /**
   * Returns the list settings for the plain text list(landscape).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsPlainLandscape() {
    return mListSettingsPlainLandscape;
  }

  /**
   * Returns the list settings for the LineEdit view list(portrait).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsLineEditPortrait() {
    return mListSettingsLineEditPortrait;
  }

  /**
   * Returns the list settings for the LineEdit view list(landscape).
   *
   * @return ListSettings
   */
  public ListSettings getListSettingsLineEditLandscape() {
    return mListSettingsLineEditLandscape;
  }
  /*-------------------- LOCALE --------------------*/

  /**
   * Change the application language.
   *
   * @param activity The activity to restart.
   */
  public void applyApplicationLanguage(Activity activity) {
    String cfg = getApplicationLanguage(this);
    String cfgLang = cfg.replace('-', '_');
    Locale locale = Locale.getDefault();
    if (!locale.toString().equals(cfgLang))
      activity.recreate();
  }

  /**
   * Sets the application language (config only).
   *
   * @param lang The new language.
   */
  public void setApplicationLanguage(final String lang) {
    SharedPreferences sp = getPref(this);
    SharedPreferences.Editor e = sp.edit();
    e.putString(ApplicationCtx.CFG_LANGUAGE, lang);
    e.apply();
  }

  /**
   * Returns the application language.
   *
   * @param context Context.
   * @return String.
   */
  public String getApplicationLanguage(final Context context) {
    return getPref(context).getString(CFG_LANGUAGE, mLanguage);
  }

  /**
   * Set the base context for this ContextWrapper.
   * All calls will then be delegated to the base context.
   * Throws IllegalStateException if a base context has already been set.
   *
   * @param base The new base context for this wrapper.
   */
  @Override
  protected void attachBaseContext(Context base) {
    super.attachBaseContext(onAttach(base));
  }

  /**
   * Loads the default locale.
   */
  private void loadDefaultLocal() {
    if (mLanguage == null) {
      Locale loc = Locale.getDefault();
      mLanguage = loc.getLanguage();
      loc.getCountry();
      if (!loc.getCountry().isEmpty())
        mLanguage += "-" + loc.getCountry();
    }
  }

  /**
   * This method must be called in attachBaseContext.
   *
   * @param context Context
   * @return The new cfg context.
   */
  public Context onAttach(Context context) {
    loadDefaultLocal();
    String lang = getApplicationLanguage(context);
    mLanguage = lang;
    String[] split = lang.split("-");
    Locale locale;
    if (split.length == 2)
      locale = new Locale(split[0], split[1]);
    else
      locale = new Locale(split[0]);

    Locale.setDefault(locale);

    Configuration configuration = context.getResources().getConfiguration();
    configuration.setLocale(locale);
    configuration.setLayoutDirection(locale);

    return context.createConfigurationContext(configuration);
  }
}
