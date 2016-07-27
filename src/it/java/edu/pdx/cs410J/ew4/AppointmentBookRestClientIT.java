package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.web.HttpRequestHelper.Response;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

/**
 * Integration test that tests the REST calls made by {@link AppointmentBookRestClient}
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppointmentBookRestClientIT {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  private AppointmentBookRestClient newAppointmentBookRestClient() {
    int port = Integer.parseInt(PORT);
    return new AppointmentBookRestClient(HOSTNAME, port);
  }

  /**
   * Test 0 invoking get with just owner name pretty printer owner parameter.
   *
   * @throws IOException the io exception
   */
  @Test
  public void test0invokingGETWithJustOwnerNamePrettyPrinterOwnerParameter() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "TestOwner";
    Response response = client.prettyPrintAppointmentBook(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));
    assertThat(response.getContent(), containsString(owner));

  }

  /**
   * Test 1 invoking post creates an appointment book and appointment.
   *
   * @throws IOException the io exception
   */
  @Test
  public void test1invokingPOSTCreatesAnAppointmentBookAndAppointment() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "TestOwner";
    String description = "My test description";
    String beginTime = "1/2/2016 1:00 PM";
    String endTime = "1/4/2016 2:00 PM";

    Response response = client.createAppointment(owner, description, beginTime, endTime);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.prettyPrintAppointmentBook(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    String prettyBeginTime = "Saturday, January 2, 2016 1:00:00 PM PST";
    String prettyEndTime = "Monday, January 4, 2016 2:00:00 PM PST";

    assertThat(response.getContent(), containsString(owner));
    assertThat(response.getContent(), containsString(description));
    assertThat(response.getContent(), containsString(prettyBeginTime));
    assertThat(response.getContent(), containsString(prettyEndTime));
  }

  /**
   * Test 2 two more appointments to the book.
   *
   * @throws IOException the io exception
   */
  @Test
  public void test2TwoMoreAppointmentsToTheBook() throws IOException {
    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "TestOwner";

    // app 1
    String description1 = "Second test description";
    String beginTime1 = "1/4/2016 4:00 PM";
    String endTime1 = "1/4/2016 5:00 PM";

    String prettyBeginTime1 = "Monday, January 4, 2016 4:00:00 PM PST";
    String prettyEndTime1 = "Monday, January 4, 2016 5:00:00 PM PST";

    Response response = client.createAppointment(owner, description1, beginTime1, endTime1);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.prettyPrintAppointmentBook(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    assertThat("failed at app 1", response.getContent(), containsString(owner));
    assertThat("failed at app 1", response.getContent(), containsString(description1));
    assertThat("failed at app 1", response.getContent(), containsString(prettyBeginTime1));
    assertThat("failed at app 1", response.getContent(), containsString(prettyEndTime1));

    // app 2
    String description2 = "Third test description";
    String beginTime2 = "1/1/2016 3:00 AM";
    String endTime2 = "1/1/2016 4:00 AM";

    String prettyBeginTime2 = "Friday, January 1, 2016 3:00:00 AM PST";
    String prettyEndTime2 = "Friday, January 1, 2016 4:00:00 AM PST";

    response = client.createAppointment(owner, description2, beginTime2, endTime2);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.prettyPrintAppointmentBook(owner);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    assertThat("failed at app 2", response.getContent(), containsString(owner));
    assertThat("failed at app 2", response.getContent(), containsString(description2));
    assertThat("failed at app 2", response.getContent(), containsString(prettyBeginTime2));
    assertThat("failed at app 2", response.getContent(), containsString(prettyEndTime2));


  }

  /**
   * Test 3 do get with search option.
   *
   * @throws IOException the io exception
   */
  @Test
  public void test3DoGetWithSearchOption() throws IOException {

    AppointmentBookRestClient client = newAppointmentBookRestClient();
    String owner = "TestOwner";

    String description1 = "Test appointment 1";
    String beginTimeString1 = "1/2/2016 1:00 PM";
    String endTimeString1 = "1/2/2016 1:30 PM";

    String description2 = "Test appointment 2";
    String beginTimeString2 = "1/3/2016 8:30 AM";
    String endTimeString2 = "1/3/2016 10:00 AM";

    String description3 = "Test appointment 3";
    String beginTimeString3 = "1/5/2016 8:30 AM";
    String endTimeString3 = "1/6/2016 10:00 AM";

    Response response = client.createAppointment(owner, description1, beginTimeString1, endTimeString1);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.createAppointment(owner, description2, beginTimeString2, endTimeString2);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.createAppointment(owner, description3, beginTimeString3, endTimeString3);
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    response = client.prettyPrintAppointmentBookByRange(owner, "1/2/2016 12:00 AM", "1/4/2016 12:00 PM");
    assertThat(response.getContent(), response.getCode(), equalTo(200));

    assertThat(response.getContent(), containsString(description2));

    assertThat(response.getContent(), not(containsString(description3)));

  }

//  @Test
//  public void test1EmptyServerContainsNoMappings() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    Response response = client.getAllKeysAndValues();
//    String content = response.getContent();
//    assertThat(content, response.getCode(), equalTo(200));
//    assertThat(content, containsString(Messages.getMappingCount(0)));
//  }
//
//  @Test
//  public void test2AddOneKeyValuePair() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    String testKey = "TEST KEY";
//    String testValue = "TEST VALUE";
//    Response response = client.addKeyValuePair(testKey, testValue);
//    String content = response.getContent();
//    assertThat(content, response.getCode(), equalTo(200));
//    assertThat(content, containsString(Messages.mappedKeyValue(testKey, testValue)));
//  }
//
//  @Test
//  public void missingRequiredParameterReturnsPreconditionFailed() throws IOException {
//    AppointmentBookRestClient client = newAppointmentBookRestClient();
//    Response response = client.postToMyURL();
//    assertThat(response.getContent(), containsString(Messages.missingRequiredParameter("key")));
//    assertThat(response.getCode(), equalTo(HttpURLConnection.HTTP_PRECON_FAILED));
//  }

}
