package edu.pdx.cs410J.ew4;

/**
 * Class for formatting messages on the server side.  This is mainly to enable
 * test methods that validate that the server returned expected strings.
 */
public class Messages {
  /**
   * Gets mapping count.
   *
   * @param count the count
   * @return the mapping count
   */
  public static String getMappingCount(int count) {
    return String.format("Server contains %d Appointment Books", count);
  }

  /**
   * Format key value pair string.
   *
   * @param key   the key
   * @param value the value
   * @return the string
   */
  public static String formatKeyValuePair(String key, String value) {
    return String.format("  %s -> %s", key, value);
  }

  /**
   * Missing required parameter string.
   *
   * @param parameterName the parameter name
   * @return the string
   */
  public static String missingRequiredParameter(String parameterName) {
    return String.format("The required parameter \"%s\" is missing", parameterName);
  }

  /**
   * Mapped key value string.
   *
   * @param key   the key
   * @param value the value
   * @return the string
   */
  public static String mappedKeyValue(String key, String value) {
    return String.format("Mapped %s to %s", key, value);
  }

  /**
   * All mappings deleted string.
   *
   * @return the string
   */
  public static String allMappingsDeleted() {
    return "All mappings have been deleted";
  }

  /**
   * Format owner listing string.
   *
   * @param owner the owner
   * @return the string
   */
  public static String formatOwnerListing(String owner) {
    return String.format("Appointment Book for %s found", owner);
  }

  /**
   * No book for owner string.
   *
   * @param owner the owner
   * @return the string
   */
  public static String noBookForOwner(String owner) {
    return String.format("Book for owner: %s not found please post a new appointment book first.", owner);
  }

  /**
   * Date time failed to parse string.
   *
   * @param eMessage the e message
   * @return the string
   */
  public static String dateTimeFailedToParse(String eMessage) {
    return String.format("Bad date and/or time formatting: %n %s", eMessage);
  }
}
