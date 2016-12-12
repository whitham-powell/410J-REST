package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.InvokeMainTestCase;
import edu.pdx.cs410J.web.HttpRequestHelper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * An integration test for {@link Project4} that invokes its main method with
 * various arguments
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Project4IT extends InvokeMainTestCase {
  private static final String HOSTNAME = "localhost";
  private static final String PORT = System.getProperty("http.port", "8080");

  /**
   * Test 0 remove all mappings.
   *
   * @throws IOException the io exception
   */
  @Test
  public void test0RemoveAllMappings() throws IOException {
    AppointmentBookRestClient client = new AppointmentBookRestClient(HOSTNAME, Integer.parseInt(PORT));
    HttpRequestHelper.Response response = client.removeAllMappings();
    assertThat(response.getContent(), response.getCode(), equalTo(200));
  }

  /**
   * Test 1 no command line arguments.
   */
  @Test
  public void test1NoCommandLineArguments() {
    MainMethodResult result = invokeMain(Project4.class);
    assertThat(result.getExitCode(), equalTo(1));
    assertThat(result.getErr(), containsString(Project4.MISSING_ARGS));
  }

  /**
   * Test 2 empty server.
   */
  @Test
  public void test2EmptyServer() {
    MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT);
    assertThat(result.getErr(), result.getExitCode(), equalTo(1));
//    String out = result.getOut();
//    assertThat(out, out, containsString(Messages.getMappingCount(0)));
  }

  /**
   * Test 3 non existent owner gets added to server.
   */
  @Test
  public void test3nonExistentOwnerGetsAddedToServer() {
    MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "TestOwner", "Project4 - first appointment", "1/1/2016", "12:00", "AM", "1/2/2016", "12:00", "AM");
    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
//    String out = result.getOut();
//    assertThat(out, out, containsString("1"));
  }

  /**
   * Test 4 print option displays new added appointment.
   */
  @Test
  public void test4printOptionDisplaysNewAddedAppointment() {
    MainMethodResult result = invokeMain(Project4.class, "-print", "-host", HOSTNAME, "-port", PORT, "TestOwner", "Project4 - second appointment", "1/3/2016", "12:00", "AM", "1/4/2016", "12:00", "AM");
    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
    assertThat(result.getOut(), result.getOut(), containsString("TestOwner"));
    assertThat(result.getOut(), result.getOut(), containsString("Project4 - second appointment"));

  }

  /**
   * Test 5 missing host and port creates a local book and appointment.
   */
  @Test
  public void test5missingHostAndPortCreatesALocalBookAndAppointment() {
    MainMethodResult result = invokeMain(Project4.class, "-print", "TestOwner", "Project4 - appointment not on server", "1/5/2016", "12:00", "AM", "1/6/2016", "12:00", "AM");
    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
    assertThat(result.getOut(), result.getOut(), containsString("TestOwner"));
    assertThat(result.getOut(), result.getOut(), containsString("Project4 - appointment not on server"));

  }

  /**
   * Test 6 search option displays appointments within given range for supplied owner.
   */
  @Test
  public void test6searchOptionDisplaysAppointmentsWithinGivenRangeForSuppliedOwner() {
    MainMethodResult result = invokeMain(Project4.class, "-print", "-host", HOSTNAME, "-port", PORT, "TestOwner", "Project4 - third appointment", "1/7/2016", "12:00", "AM", "1/8/2016", "12:00", "AM");
    assertThat(result.getErr(), result.getExitCode(), equalTo(0));

    assertThat(result.getOut(), result.getOut(), containsString("TestOwner"));
    assertThat(result.getOut(), result.getOut(), containsString("Project4 - third appointment"));

    result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", "TestOwner", "1/1/2016", "12:00", "AM", "1/4/2016", "12:00", "AM");
    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
    assertThat(result.getOut(), result.getOut(), containsString("Project4 - first appointment"));
    assertThat(result.getOut(), result.getOut(), containsString("Project4 - second appointment"));
    assertThat(result.getOut(), result.getOut(), not(containsString("Project4 - third appointment")));

//    System.out.print(result.getOut());
  }

  /**
   * Test 7 search option requires owner begin time and end time error otherwise.
   */
  @Test
  public void test7searchOptionRequiresOwnerBeginTimeAndEndTimeErrorOtherwise() {
    MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", "TestOwner", "12:00", "AM", "1/4/2016", "12:00", "AM");

    assertThat(result.getErr(), result.getExitCode(), equalTo(1));
//    System.out.println(result.getErr());
  }

  /**
   * Test 8 search with unknown owner fails nicely.
   */
  @Test
  public void test8searchWithUnknownOwnerFailsNicely() {
    MainMethodResult result = invokeMain(Project4.class, "-host", HOSTNAME, "-port", PORT, "-search", "NotTestOwner", "1/1/2016", "12:00", "AM", "1/4/2016", "12:00", "AM");
//    System.out.println(result.getErr());
//    System.out.println(result.getOut());
    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
    assertThat(result.getOut(), result.getOut(), containsString("NotTestOwner"));
    assertThat(result.getOut(), result.getOut(), containsString("not found"));
  }
//TODO @Test public void READMEOptionPrintsUsageAndReadMe()


//  @Test
//  public void test3NoValues() {
//    String key = "KEY";
//    MainMethodResult result = invokeMain(Project4.class, HOSTNAME, PORT, key);
//    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
//    String out = result.getOut();
//    assertThat(out, out, containsString(Messages.getMappingCount(0)));
//    assertThat(out, out, containsString(Messages.formatKeyValuePair(key, null)));
//  }
//
//  @Test
//  public void test4AddValue() {
//    String key = "KEY";
//    String value = "VALUE";
//
//    MainMethodResult result = invokeMain(Project4.class, HOSTNAME, PORT, key, value);
//    assertThat(result.getErr(), result.getExitCode(), equalTo(0));
//    String out = result.getOut();
//    assertThat(out, out, containsString(Messages.mappedKeyValue(key, value)));
//
//    result = invokeMain(Project4.class, HOSTNAME, PORT, key);
//    out = result.getOut();
//    assertThat(out, out, containsString(Messages.getMappingCount(1)));
//    assertThat(out, out, containsString(Messages.formatKeyValuePair(key, value)));
//
//    result = invokeMain(Project4.class, HOSTNAME, PORT);
//    out = result.getOut();
//    assertThat(out, out, containsString(Messages.getMappingCount(1)));
//    assertThat(out, out, containsString(Messages.formatKeyValuePair(key, value)));
//  }
}