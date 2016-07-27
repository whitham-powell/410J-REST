package edu.pdx.cs410J.ew4;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper {
  private static final String WEB_APP = "apptbook";
  private static final String SERVLET = "appointments";

  private final String url;


  /**
   * Creates a client to the appointment book REST service running on the given host and port
   *
   * @param hostName The name of the host
   * @param port     The port
   */
  public AppointmentBookRestClient(String hostName, int port) {
    this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET);
  }

  /**
   * Returns all keys and values from the server
   *
   * @return the all keys and values
   * @throws IOException the io exception
   */
  public Response getAllKeysAndValues() throws IOException {
    return get(this.url);
  }

  /**
   * Returns all values for the given key
   *
   * @param key the key
   * @return the values
   * @throws IOException the io exception
   */
  public Response getValues(String key) throws IOException {
    return get(this.url, "key", key);
  }

  /**
   * Add key value pair response.
   *
   * @param key   the key
   * @param value the value
   * @return the response
   * @throws IOException the io exception
   */
  public Response addKeyValuePair(String key, String value) throws IOException {
    return postToMyURL("key", key, "value", value);
  }

  /**
   * Post to my url response.
   *
   * @param keysAndValues the keys and values
   * @return the response
   * @throws IOException the io exception
   */
  @VisibleForTesting
  Response postToMyURL(String... keysAndValues) throws IOException {
    return post(this.url, keysAndValues);
  }

  /**
   * Remove all mappings response.
   *
   * @return the response
   * @throws IOException the io exception
   */
  public Response removeAllMappings() throws IOException {
    return delete(this.url);
  }

  /**
   * Pretty print appointment book response.
   *
   * @param owner the owner
   * @return the response
   * @throws IOException the io exception
   */
  public Response prettyPrintAppointmentBook(String owner) throws IOException {
    return get(this.url, "owner", owner);
  }

  /**
   * Pretty print appointment book by range response.
   *
   * @param owner     the owner
   * @param beginTime the begin time
   * @param endTime   the end time
   * @return the response
   * @throws IOException the io exception
   */
  public Response prettyPrintAppointmentBookByRange(String owner, String beginTime, String endTime) throws IOException {
    return get(this.url, "owner", owner, "beginTime", beginTime, "endTime", endTime);
  }

  /**
   * Create appointment response.
   *
   * @param owner           the owner
   * @param description     the description
   * @param beginTimeString the begin time string
   * @param endTimeString   the end time string
   * @return the response
   * @throws IOException the io exception
   */
  public Response createAppointment(String owner, String description, String beginTimeString, String endTimeString) throws IOException {
    return post(this.url, "owner", owner, "description", description, "beginTime", beginTimeString, "endTime", endTimeString);
  }

}
