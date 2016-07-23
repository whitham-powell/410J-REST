package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.AbstractAppointment;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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

  @Test
  public void defaultActionIfNoParametersIsPrintListOfBookKeys() throws IOException, ServletException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);
    servlet.doGet(request, response);

    int expectedMappings = 1;
    verify(pw).println(Messages.getMappingCount(expectedMappings));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }

  //TODO @Test public void ownerBookNotFoundPrintsListOfOwnerKeys()

  //TODO @Test public void doPostWithOwnerNotYetInDatabaseCreatesNewBookAndAddsAppointment()

  /*@Test
  public void initiallyServletContainsNoKeyValueMappings() throws ServletException, IOException {
    AppointmentBookServlet servlet = new AppointmentBookServlet();

    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter pw = mock(PrintWriter.class);

    when(response.getWriter()).thenReturn(pw);

    servlet.doGet(request, response);

    int expectedMappings = 0;
    verify(pw).println(Messages.getMappingCount(expectedMappings));
    verify(response).setStatus(HttpServletResponse.SC_OK);
  }*/

}
