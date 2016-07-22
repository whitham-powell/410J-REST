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
   */
  public Response getAllKeysAndValues() throws IOException {
    return get(this.url);
  }

  /**
   * Returns all values for the given key
   */
  public Response getValues(String key) throws IOException {
    return get(this.url, "key", key);
  }

  public Response addKeyValuePair(String key, String value) throws IOException {
    return postToMyURL("key", key, "value", value);
  }

  @VisibleForTesting
  Response postToMyURL(String... keysAndValues) throws IOException {
    return post(this.url, keysAndValues);
  }

  public Response removeAllMappings() throws IOException {
    return delete(this.url);
  }

  public Response prettyPrintAppointmentBook(String owner) throws IOException {
//  throw new UnsupportedOperationException("not implemented yet");
    return get(this.url, "owner", owner);
  }

  public Response createAppointment(String owner, String description, String endTimeString, String beginTimeString) throws IOException {
//  throw new UnsupportedOperationException("not implemented yet");
    return post(this.url, "owner", owner, "description", description, "beginTime", beginTimeString, "endTime", endTimeString);
  }

}
