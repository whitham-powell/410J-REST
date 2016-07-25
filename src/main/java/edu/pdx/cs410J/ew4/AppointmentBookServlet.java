package edu.pdx.cs410J.ew4;

import com.google.common.annotations.VisibleForTesting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet {
  private ConcurrentHashMap<String, AppointmentBook> appointmentBooks = new ConcurrentHashMap<>();

  /**
   * Instantiates a new Appointment book servlet.
   */
  public AppointmentBookServlet() {
  }

  /**
   * Create test appointment book.
   */
  @VisibleForTesting
  void createTestAppointmentBook() {
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

    AppointmentBook book;
    String owner = getParameter("owner", request);
    String beginTime = getParameter("beginTime", request);
    String endTime = getParameter("endTime", request);

    if (owner == null) {
      listAllBookOwners(response);
      return;
    }

    boolean byRange = false;
    Date beginDateRange = null;
    Date endDateRange = null;

    if (beginTime != null && endTime != null) {
      try {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        beginDateRange = df.parse(beginTime);
        endDateRange = df.parse(endTime);
      } catch (ParseException e) {
        invalidDateTime(response, e.getMessage());
        return;
      }
      byRange = true;
    }

    if (beginTime == null && endTime != null) {
      missingRequiredParameter(response, "beginTime");
      return;
    }

    if (beginTime != null && endTime == null) {
      missingRequiredParameter(response, "endTime");
      return;
    }

    book = getAppointmentBook(owner);

    if (book == null) {
      bookNotFoundForOwner(response, owner);
      listAllBookOwners(response);
      return;
    }

    if (byRange) {
      prettyPrintRange(book, response.getWriter(), beginDateRange, endDateRange);
      response.setStatus(HttpServletResponse.SC_OK);
    } else {
      prettyPrint(book, response.getWriter());
      response.setStatus(HttpServletResponse.SC_OK);
    }
  }

  private void prettyPrintRange(AppointmentBook book, PrintWriter pw, Date beginDateRange, Date endDateRange) throws IOException {
    PrettyPrinter pp = new PrettyPrinter(pw);
    pp.dumpRange(book, beginDateRange, endDateRange);
    pw.flush();
  }

  //TODO document invalidDateTime
  private void invalidDateTime(HttpServletResponse response, String eMessage) throws IOException {
    String message = Messages.dateTimeFailedToParse(eMessage);
    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
  }

  /**
   * Handles AppointmentBook for owner not found case.
   *
   * @param response <code>HttpServletResponse</code>
   * @param owner    of book attempted to be found.
   * @throws IOException problem communicating with server.
   */
  private void bookNotFoundForOwner(HttpServletResponse response, String owner) throws IOException {
    response.getWriter().println(Messages.noBookForOwner(owner));
    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Conducts the pretty print for an AppointmentBook.
   *
   * @param book to be 'Pretty' printed.
   * @param pw   where to write the 'Pretty' printed book.
   * @throws IOException problem communicating with server.
   */
  private void prettyPrint(AppointmentBook book, PrintWriter pw) throws IOException {
    PrettyPrinter pp = new PrettyPrinter(pw);
    pp.dump(book);
    pw.flush();
  }

  /**
   * Gets appointment book.
   *
   * @param owner the owner.
   * @return the appointment book.
   */
  @VisibleForTesting
  AppointmentBook getAppointmentBook(String owner) {
    return this.appointmentBooks.get(owner);
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

    //TODO Error checking
    String description = getParameter("description", request);
    String beginTime = getParameter("beginTime", request);
    String endTime = getParameter("endTime", request);

    Appointment toAdd = new Appointment(description, beginTime, endTime);

    if (book == null) {
      book = new AppointmentBook(owner);
    }

    book.addAppointment(toAdd);
    putAppointmentBook(owner, book);
    response.setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Adds an AppointmentBook to the HashMap of AppointmentBooks.
   *
   * @param owner Owner key.
   * @param book  to put.
   * @return AppointmentBook replaced by put if copy found.
   */
  private AppointmentBook putAppointmentBook(String owner, AppointmentBook book) {
    return this.appointmentBooks.put(owner, book);
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
   * Writes all of the book owners to the HTTP response.
   * <p>
   * The text of the message is formatted with
   * {@link Messages#formatOwnerListing(String)}
   */
  private void listAllBookOwners(HttpServletResponse response) throws IOException {
    PrintWriter pw = response.getWriter();
    pw.println(Messages.getMappingCount(appointmentBooks.size()));

    for (Map.Entry<String, AppointmentBook> entry : this.appointmentBooks.entrySet()) {
      pw.println(Messages.formatOwnerListing(entry.getKey()));
    }

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

  /**
   * Create test appointment book with multiple appointments.
   */
  @VisibleForTesting
  void createTestAppointmentBookWithMultipleAppointments() {
    String ownerName = "TestOwner";
    AppointmentBook book = new AppointmentBook(ownerName);
    book.addAppointment(new Appointment("Test appointment 1", "1/2/2016 1:00 PM", "1/2/2016 1:30 PM"));
    book.addAppointment(new Appointment("Test appointment 2", "1/3/2016 8:30 AM", "1/3/2016 10:00 AM"));
    book.addAppointment(new Appointment("Test appointment 3", "1/5/2016 8:30 AM", "1/6/2016 10:00 AM"));
    this.appointmentBooks.put(ownerName, book);
  }
}
