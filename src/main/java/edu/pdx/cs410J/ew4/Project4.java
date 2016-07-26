package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ListIterator;

import static java.lang.System.err;
import static java.lang.System.out;

/**
 * The main class that parses the command line and communicates with the
 * Appointment Book server using REST.
 */
public class Project4 {
  public static final String MISSING_ARGS = "Missing command line arguments";

  private static final String USAGE =
          "usage: java edu.pdx.edu.cs410J.<login-id>.Project4 [options] <args> \n" +
                  " args are (in this order): \n" +
                  "   owner        The person whose owns the appt book\n" +
                  "   description  A description of the appointment\n" +
                  "   beginTime    When the appt begins (12-hour time)\n" +
                  "   endTime      When the appt ends (12-hour time)\n";

  private static final String README =
          "CS410J Project 4: Storing an Appointment Book in a Text File\n" +
                  "Author: Elijah Whitham-Powell\n" +
                  "Date Completed: July 27th, 2016\n" +
                  "Description: This project is a commandline and servlet application that creates an Appointment\n" +
                  "\t\t\t and adds it to an AppointmentBook based on the arguments provided via the\n" +
                  "\t\t\t command line. Appointment descriptions should be provided in double quotes. \n" +
                  "\t\t\t The rest of the arguments should be provided separated by spaces. \n" +
                  "\t\t\t The appointment book can also be created from and saved to a text file provided\n" +
                  "\t\t\t via the options list.\n";

  public static void main(String... args) {
    String hostName;
    String portString;

    CommandLineParser.Options options = new CommandLineParser.Options();
    options.addOption("host", true, "hostname", "Host of web server.");
    options.addOption("port", true, "port", "Port of web server.");
    options.addOption("search", false, "Appointments should be searched for.");
    options.addOption("print", false, "Prints a description of the new appointment.");
    options.addOption("README", false, "Prints a README for this project along with the usage and exits.");

    // Parse the command line and get the commands
    CommandLineParser commandLine = new CommandLineParser(options, args);
    CommandLineParser.Commands commands = commandLine.parse(8);

    // Special search case
    if (commandLine.getToParse().contains("-search")) {
      commandLine = new CommandLineParser(options, args);
      commands = commandLine.parse(7);

      boolean haveHost = commands.hasOption("host");
      boolean havePort = commands.hasOption("port");
      boolean doPrint = commands.hasOption("print");

      if (haveHost && havePort) {

        hostName = commands.getOptionValue("host");
        portString = commands.getOptionValue("port");

        int port;
        try {
          port = Integer.parseInt(commands.getOptionValue("port"));

        } catch (NumberFormatException ex) {
          usage("Port \"" + portString + "\" must be an integer");
          return;
        }

        ListIterator<String> infoIterator = commandLine.getProvidedArgs().listIterator();
        String appointmentOwner = infoIterator.next();
        if (appointmentOwner.equalsIgnoreCase("-search")) {
          error(USAGE + options + "Dates and times should be in the format: mm/dd/yyyy hh:mm am/pm \n");
          System.exit(1);
        }
        String beginDateTimeString = infoIterator.next() + " " + infoIterator.next() + " " + infoIterator.next();
        String endDateTimeString = infoIterator.next() + " " + infoIterator.next() + " " + infoIterator.next();

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

        HttpRequestHelper.Response response;

        try {
          response = client.prettyPrintAppointmentBookByRange(appointmentOwner, beginDateTimeString, endDateTimeString);
          checkResponseCode(HttpURLConnection.HTTP_OK, response);
          out.print(response.getContent());
          System.exit(0);
        } catch (IOException ex) {
          error("While contacting server: " + ex);
          System.exit(1);
        }

      }
      System.exit(0);
    }

    // Check for README flag special case to exit
    if (commands.hasOption("README")) {
      out.print(README + "\n" + USAGE + "Dates and times should be in the format: mm/dd/yyyy hh:mm am/pm \n");
      out.print(options);
      System.exit(0);
    }

    // Check for errors
    if (commands.hasError()) {
      err.print(commands.getErrorMessage());
      out.print(USAGE + options + "Dates and times should be in the format: mm/dd/yyyy hh:mm am/pm \n");
      System.exit(1);
    }

    boolean haveHost = commands.hasOption("host");
    boolean havePort = commands.hasOption("port");
    boolean doPrint = commands.hasOption("print");


    // Grab Appointment Owner and Appointment Information from Command Line
    ListIterator<String> infoIterator = commandLine.getProvidedArgs().listIterator();
    String appointmentOwner = infoIterator.next();
    String description = infoIterator.next();
    String beginDateTimeString = infoIterator.next() + " " + infoIterator.next() + " " + infoIterator.next();
    String endDateTimeString = infoIterator.next() + " " + infoIterator.next() + " " + infoIterator.next();

    // no host and port means local only appointment and appointment book
    if (!haveHost && !havePort) {
      AppointmentBook book = new AppointmentBook(appointmentOwner);
      // Parse the date
      DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
      Date beginTime = null;
      Date endTime = null;
      try {
        beginTime = df.parse(beginDateTimeString);
        endTime = df.parse(endDateTimeString);
      } catch (ParseException e) {
        err.println("Bad formatting of the time and/or date");
        out.println("Dates and times should be in the format: mm/dd/yyyy hh:mm am/pm\n");
        System.exit(1);
      }

      Appointment appointment = new Appointment(description, beginTime, endTime);
      book.addAppointment(appointment);
      if (doPrint) {
        out.format("Owner: %s %nNewly Added Appointment: %n %s",
                appointmentOwner, appointment);
      }
      System.exit(0);
    }

    if (haveHost && havePort) {

      hostName = commands.getOptionValue("host");
      portString = commands.getOptionValue("port");

      int port;
      try {
        port = Integer.parseInt(commands.getOptionValue("port"));

      } catch (NumberFormatException ex) {
        usage("Port \"" + portString + "\" must be an integer");
        return;
      }

      AppointmentBookRestClient client = new AppointmentBookRestClient(hostName, port);

      HttpRequestHelper.Response response;

      try {


        response = client.createAppointment(appointmentOwner, description, beginDateTimeString, endDateTimeString);
        checkResponseCode(HttpURLConnection.HTTP_OK, response);

      } catch (IOException ex) {
        error("While contacting server: " + ex);
        System.exit(1);
      }


      if (doPrint) {
        out.format("Owner: %s %nNewly Added Appointment: %n %s",
                appointmentOwner, new Appointment(description, beginDateTimeString, endDateTimeString));
      }

      System.exit(0);
    } else if (!haveHost) {
      usage(MISSING_ARGS);
    } else {
      usage("Missing port");
    }
  }


  /**
   * Makes sure that the give response has the expected HTTP status code
   *
   * @param code     The expected status code
   * @param response The response from the server
   */
  private static void checkResponseCode(int code, HttpRequestHelper.Response response) {
    if (response.getCode() != code) {
      error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
              response.getCode(), response.getContent()));
    }
  }

  private static void error(String message) {
    PrintStream err = System.err;
    err.println("** " + message);

    System.exit(1);
  }

  /**
   * Prints usage information for this program and exits
   *
   * @param message An error message to print
   */
  private static void usage(String message) {
    PrintStream err = System.err;
    err.println("** " + message);
    err.println();
    err.println("usage: java Project4 host port [key] [value]");
    err.println("  host    Host of web server");
    err.println("  port    Port of web server");
    err.println("  key     Key to query");
    err.println("  value   Value to add to server");
    err.println();
    err.println("This simple program posts key/value pairs to the server");
    err.println("If no value is specified, then all values are printed");
    err.println("If no key is specified, all key/value pairs are printed");
    err.println();

    System.exit(1);
  }
}