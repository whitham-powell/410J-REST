package edu.pdx.cs410J.ew4;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet {
  //  private final Map<String, AppointmentBook> appointmentBooks = Collections.synchronizedMap(new HashMap<>());
  private ConcurrentHashMap<String, AppointmentBook> appointmentBooks = new ConcurrentHashMap<>();

  public AppointmentBookServlet() {
    createTestAppointmentBook();
  }

  private void createTestAppointmentBook() {
    String ownerName = "TestOwner";
    AppointmentBook book = new AppointmentBook(ownerName);
    this.appointmentBooks.put(ownerName, book);
  }

  /**
   * Handles an HTTP GET request from a client by writing the value of the key
   * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
   * parameter is not specified, all of the key/value pairs are written to the
   * HTTP response.
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/plain");
    String owner = getParameter("owner", request);
    AppointmentBook book = getAppointmentBook(owner);
    prettyPrint(book, response.getWriter());
    response.setStatus(HttpServletResponse.SC_OK);
  }

  private void prettyPrint(AppointmentBook book, PrintWriter pw) throws IOException {
    PrettyPrinter pp = new PrettyPrinter(pw);
    pp.dump(book);
  }

  @VisibleForTesting
  AppointmentBook getAppointmentBook(String owner) {
  return this.appointmentBooks.getOrDefault(owner, new AppointmentBook("owner not found"));
  }

  /**
   * Handles an HTTP POST request by storing the key/value pair specified by the
   * "key" and "value" request parameters.  It writes the key/value pair to the
   * HTTP response.
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    response.setContentType("text/plain");

    String owner = getParameter("owner", request);
    AppointmentBook book = getAppointmentBook(owner);

    String description = getParameter("description", request);
    String beginTime = getParameter("beginTime", request);
    String endTime = getParameter("endTime", request);
      book.addAppointment(new Appointment(description, beginTime, endTime));
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().println("null book in do post");
  }

  /**
   * Handles an HTTP DELETE request by removing all key/value pairs.  This
   * behavior is exposed for testing purposes only.  It's probably not
   * something that you'd want a real application to expose.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

  }

  /**
   * Writes an error message about a missing parameter to the HTTP response.
   * <p>
   * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
   */
  private void missingRequiredParameter(HttpServletResponse response, String parameterName)
          throws IOException {
    String message = Messages.missingRequiredParameter(parameterName);
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Writes all of the key/value pairs to the HTTP response.
   * <p>
   * The text of the message is formatted with
   * {@link Messages#formatKeyValuePair(String, String)}
   */
  private void writeAllMappings(HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();
    pw.println(Messages.getMappingCount(appointmentBooks.size()));

//    for (Map.Entry<String, String> entry : this.appointmentBooks.entrySet()) {
//      pw.println(Messages.formatKeyValuePair(entry.getKey(), entry.getValue()));
//    }

    pw.flush();

    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Returns the value of the HTTP request parameter with the given name.
   *
   * @return <code>null</code> if the value of the parameter is
   * <code>null</code> or is the empty string
   */
  private String getParameter(String name, HttpServletRequest request) {
    String value = request.getParameter(name);
    if (value == null || "".equals(value)) {
      return null;

    } else {
      return value;
    }
  }

  @VisibleForTesting
  String getValueForKey(String key) {
//    return this.appointmentBooks.get(key);
    throw new UnsupportedOperationException("I don't work yet anymore!");
  }
}
