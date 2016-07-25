package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.AbstractAppointment;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

/**
 * A unit test for the {@link AppointmentBookServlet}.  It uses mockito to
 * provide mock http requests and responses.
 */
public class AppointmentBookServletTest {

  /**
   * Do get pretty prints appointment book.
   *
   * @throws ServletException the servlet exception
   * @throws IOException      the io exception
   */
  @Test
  public void doGetPrettyPrintsAppointmentBook() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "TestOwner";
    when(request.getParameter("owner")).thenReturn(ownerName);
    when(response.getWriter()).thenReturn(pw);

    servlet.createTestAppointmentBook();
    servlet.doGet(request, response);

    verify(pw).println(ownerName);
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Post to servlet creates appointment.
   *
   * @throws ServletException the servlet exception
   * @throws IOException      the io exception
   */
  @Test
  public void postToServletCreatesAppointment() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "TestOwner";
    when(request.getParameter("owner")).thenReturn(ownerName);
    String description = "My test description";
    when(request.getParameter("description")).thenReturn(description);
    String beginTime = "1/2/16 1:00 PM";
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    String endTime = "1/4/16 2:00 PM";
    when(request.getParameter("endTime")).thenReturn(endTime);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);

    AppointmentBook book = servlet.getAppointmentBook(ownerName);
    Collection<AbstractAppointment> appointments = book.getAppointments();
    assertThat(appointments.size(), equalTo(1));
    AbstractAppointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));
    assertThat(appointment.getBeginTimeString(), equalTo(beginTime));
    assertThat(appointment.getEndTimeString(), equalTo(endTime));

  }

  /**
   * Default action if no parameters is print list of book keys.
   *
   * @throws IOException      the io exception
   * @throws ServletException the servlet exception
   */
  @Test
  public void defaultActionIfNoParametersIsPrintListOfBookKeys() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    servlet.doGet(request, response);

    int expectedMappings = 0;
    verify(pw).println(Messages.getMappingCount(expectedMappings));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  /**
   * Owner book not found prints list of owner keys.
   *
   * @throws IOException      the io exception
   * @throws ServletException the servlet exception
   */
  @Test
  public void ownerBookNotFoundPrintsListOfOwnerKeys() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String testOwner = "TestOwner";

    String ownerName = "Non Existant Owner";
    when(request.getParameter("owner")).thenReturn(ownerName);

    when(response.getWriter()).thenReturn(pw);
    servlet.createTestAppointmentBook();
    servlet.doGet(request, response);

    verify(pw).println(Messages.noBookForOwner(ownerName));
    verify(response).setStatus((HttpServletResponse.SC_NOT_FOUND));
    verify(pw).println(Messages.formatOwnerListing(testOwner));

  }

  /**
   * Do post with owner not yet in database creates new book and adds appointment.
   *
   * @throws IOException      the io exception
   * @throws ServletException the servlet exception
   */
  @Test
  public void doPostWithOwnerNotYetInDatabaseCreatesNewBookAndAddsAppointment() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "NotTestOwner";
    when(request.getParameter("owner")).thenReturn(ownerName);
    String description = "My test description";
    when(request.getParameter("description")).thenReturn(description);
    String beginTime = "1/2/16 1:00 PM";
    when(request.getParameter("beginTime")).thenReturn(beginTime);
    String endTime = "1/4/16 2:00 PM";
    when(request.getParameter("endTime")).thenReturn(endTime);

    when(response.getWriter()).thenReturn(pw);

    servlet.doPost(request, response);

    verify(response).setStatus(HttpServletResponse.SC_OK);

    AppointmentBook book = servlet.getAppointmentBook(ownerName);
    Collection<AbstractAppointment> appointments = book.getAppointments();
    assertThat(appointments.size(), equalTo(1));
    AbstractAppointment appointment = appointments.iterator().next();
    assertThat(appointment.getDescription(), equalTo(description));
    assertThat(appointment.getBeginTimeString(), equalTo(beginTime));
    assertThat(appointment.getEndTimeString(), equalTo(endTime));
  }

  /**
   * Do get with begin and end time displays all appointments for owner during the supplied interval.
   *
   * @throws ServletException the servlet exception
   * @throws IOException      the io exception
   */
  @Test
  public void doGetWithBeginAndEndTimeDisplaysAllAppointmentsForOwnerDuringTheSuppliedInterval() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    String ownerName = "TestOwner";
    when(request.getParameter("owner")).thenReturn(ownerName);
    when(request.getParameter("beginTime")).thenReturn("1/2/2016 1:00 PM");
    when(request.getParameter("endTime")).thenReturn("1/3/2016 4:00 PM");
    when(response.getWriter()).thenReturn(pw);

    servlet.createTestAppointmentBookWithMultipleAppointments();
    servlet.doGet(request, response);

    Appointment appointment1 = new Appointment("Test appointment 1", "1/2/2016 1:00 PM", "1/2/2016 1:30 PM");
    Appointment appointment2 = new Appointment("Test appointment 2", "1/3/2016 8:30 AM", "1/3/2016 10:00 AM");
    Appointment appointment3 = new Appointment("Test appointment 3", "1/5/2016 8:30 AM", "1/6/2016 10:00 AM");

    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG);


    verify(pw).println(ownerName);
    verify(pw, times(2)).println("=====================================================");

    verify(pw).format("Description: %s %n", appointment1.getDescription());
    verify(pw).format("From: %s %n", df.format(appointment1.getBeginTime()));
    verify(pw).format("Until: %s %n", df.format(appointment1.getEndTime()));

    verify(pw).format("Description: %s %n", appointment2.getDescription());
    verify(pw).format("From: %s %n", df.format(appointment2.getBeginTime()));
    verify(pw).format("Until: %s %n", df.format(appointment2.getEndTime()));

    verify(pw, never()).format("Description: %s %n", appointment3.getDescription());
    verify(pw, never()).format("From: %s %n", df.format(appointment3.getBeginTime()));
    verify(pw, never()).format("Until: %s %n", df.format(appointment3.getEndTime()));

    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

}
