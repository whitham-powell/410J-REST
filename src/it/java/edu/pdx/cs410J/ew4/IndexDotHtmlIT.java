package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.web.HttpRequestHelper;
import edu.pdx.cs410J.web.HttpRequestHelper.Response;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * The type Index dot html it.
 */
public class IndexDotHtmlIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  /**
   * Index dot html exists.
   *
   * @throws IOException the io exception
   */
  @Test
  public void indexDotHtmlExists() throws IOException {
    Response indexDotHtml = fetchIndexDotHtml();
    assertThat(indexDotHtml.getCode(), equalTo(200));
  }

  /**
   * Index dot html has resonable content.
   *
   * @throws IOException the io exception
   */
  @Test
  public void indexDotHtmlHasResonableContent() throws IOException {
    Response indexDotHtml = fetchIndexDotHtml();
    assertThat(indexDotHtml.getContent(), containsString("<form"));
  }

  private Response fetchIndexDotHtml() throws IOException {
    int port = Integer.parseInt(PORT);
    return new IndexDotHtmlHelper(HOSTNAME, port).getIndexDotHtml();
  }

  /**
   * The type Index dot html helper.
   */
  static class IndexDotHtmlHelper extends HttpRequestHelper {
    private static final String WEB_APP = "apptbook";
    private final String url;

    /**
     * Instantiates a new Index dot html helper.
     *
     * @param hostName the host name
     * @param port     the port
     */
    IndexDotHtmlHelper(String hostName, int port) {
      this.url = String.format("http://%s:%d/%s/%s", hostName, port, WEB_APP, "index.html");
    }

    /**
     * Gets index dot html.
     *
     * @return the index dot html
     * @throws IOException the io exception
     */
    public Response getIndexDotHtml() throws IOException {
      return get(this.url);
    }
  }
}
