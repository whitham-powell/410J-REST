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
   * Dumps an appointment book to some destination.
   *
   * @param book The appointment book whose contents are to be dumped
   * @throws IOException Something went wrong while dumping the appointment book
   */
  @Override
  public void dump(AbstractAppointmentBook book) throws IOException {
    StringBuffer sb = new StringBuffer();
    sb.append("Owner: ").append(book.getOwnerName()).append("\n");

    SortedSet<Appointment> sortedBook = ((AppointmentBook) book).getSortedSet();
    Iterator<Appointment> sortedAppointments = sortedBook.iterator();

    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG);

    while (sortedAppointments.hasNext()) {
      Appointment appointment = sortedAppointments.next();
      sb.append("=====================================================\n")
              .append("Description:\n").append(appointment.getDescription()).append("\n");

      // Get the begin and end times
      Date beginTime = appointment.getBeginTime();
      Date endTime = appointment.getEndTime();

      // If either is null duration is 0
      if (beginTime != null && endTime != null) {
        long hours = ChronoUnit.HOURS.between(beginTime.toInstant(), endTime.toInstant());
        long minutes = ChronoUnit.MINUTES.between(beginTime.toInstant(), endTime.toInstant());
        sb.append("From: ").append(df.format(beginTime)).append("\n")
                .append("Until: ").append(df.format(endTime)).append("\n");
        sb.append("Duration: ").append(hours).append(" hours, ").append((int) minutes % 60).append(" minutes\n");
      }
    }
    bw.write(sb.toString());
    bw.close();
  }


}
