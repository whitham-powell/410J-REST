package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.text.DateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;


/**
 * Pretty Prints an {@link AppointmentBook} to a text file, or standard out if no file name provided.
 * Output is sorted.
 */
public class PrettyPrinter implements AppointmentBookDumper {
  private BufferedWriter bw;
  private PrintWriter pw;

  /**
   * Instantiates a new Pretty printer.for writing to Standard Out.
   */
  public PrettyPrinter() {
    this(new BufferedWriter(new OutputStreamWriter(System.out)));
  }

  /**
   * Instantiates a new Pretty printer from String.
   *
   * @param fileName the file name
   * @throws IOException the io exception
   */
  public PrettyPrinter(String fileName) throws IOException {
    this(new File(fileName));
  }

  /**
   * Instantiates a new Pretty printer from File.
   *
   * @param file the file
   * @throws IOException the io exception
   */
  public PrettyPrinter(File file) throws IOException {
    this(new FileWriter(file));
  }

  /**
   * Instantiates a new Pretty printer from FileWriter
   *
   * @param fileWriter the file writer
   */
  public PrettyPrinter(FileWriter fileWriter) {
    this(new BufferedWriter(fileWriter));
  }

  /**
   * Instantiates a new Pretty printer.
   *
   * @param bufferedWriter the buffered writer
   */
  public PrettyPrinter(BufferedWriter bufferedWriter) {
    this.bw = bufferedWriter;
  }

  /**
   * Instantiates a new Pretty printer.
   *
   * @param printWriter the print writer
   */
  public PrettyPrinter(PrintWriter printWriter) {
    this.pw = printWriter;
  }


  /**
   * Dumps an appointment book to some destination.
   *
   * @param book The appointment book whose contents are to be dumped
   * @throws IOException Something went wrong while dumping the appointment book
   */
  @Override
  public void dump(AbstractAppointmentBook book) throws IOException {
    SortedSet<Appointment> sortedBook = ((AppointmentBook) book).getSortedSet();
    printAppointments(book, sortedBook);
  }

  /**
   * Dump range.
   *
   * @param book           the book
   * @param beginDateRange the begin date range
   * @param endDateRange   the end date range
   */
  public void dumpRange(AbstractAppointmentBook book, Date beginDateRange, Date endDateRange) {
    SortedSet<Appointment> sortedBook = ((AppointmentBook) book).byRange(beginDateRange, endDateRange);
    printAppointments(book, sortedBook);
  }


  private void printAppointments(AbstractAppointmentBook book, SortedSet<Appointment> sortedBook) {
    Iterator<Appointment> sortedAppointments = sortedBook.iterator();

    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG);

    pw.println(book.getOwnerName());

    while (sortedAppointments.hasNext()) {
      Appointment appointment = sortedAppointments.next();

      // Get the begin and end times
      Date beginTime = appointment.getBeginTime();
      Date endTime = appointment.getEndTime();

      // Get appointment duration
      // If either Date is null duration is 0
      long hours = 0;
      long minutes = 0 ;
      if (beginTime != null && endTime != null) {
        hours = ChronoUnit.HOURS.between(beginTime.toInstant(), endTime.toInstant());
        minutes = ChronoUnit.MINUTES.between(beginTime.toInstant(), endTime.toInstant());
      }

      pw.println("=====================================================");
      pw.format("Description: %s %n", appointment.getDescription());
      pw.format("From: %s %n", df.format(beginTime));
      pw.format("Until: %s %n", df.format(endTime));
      pw.format("Duration: %d hours, %d minutes %n", hours, minutes % 60);
    }
  }

}
