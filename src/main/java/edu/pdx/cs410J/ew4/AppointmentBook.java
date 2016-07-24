package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * This class extends <code>AbstractAppointmentBook</code> and implements a concrete <code>AppointmentBook</code>
 *
 * @author Elijah Whitham-Powell
 */
public class AppointmentBook extends AbstractAppointmentBook<AbstractAppointment> {
  final private Collection<AbstractAppointment> appointments;
  private String ownerName = "< unspecified >";

  /**
   * Instantiates a new Appointment book.
   *
   * @param that the that
   */
  public AppointmentBook(AppointmentBook that) {
    this.ownerName = that.getOwnerName();
    this.appointments = that.getAppointments();
  }

  /**
   * Creates an instance of an <code>AppointmentBook</code> stores a <code>Collection</code> of {@link Appointment} objects
   *
   * @param ownerName The name of the owner of <code>AppointmentBook</code>. Of type <code>String</code>
   */
  public AppointmentBook(String ownerName) {
    this.ownerName = ownerName;
    this.appointments = new ArrayList<>();
  }

  /**
   * Gets sorted set.
   *
   * @return the sorted set
   */
  public SortedSet<Appointment> getSortedSet() {
    SortedSet<Appointment> sortedAppointments = new TreeSet<>(new Comparator<Appointment>() {
      @Override
      public int compare(Appointment appointment, Appointment that) {
        return appointment.compareTo(that);
      }
    });
    for (Object fromCollection : this.appointments) {
      AtomicReference<Appointment> toSorted = new AtomicReference<>((Appointment) fromCollection);
      sortedAppointments.add(toSorted.get());
    }
    return sortedAppointments;
  }

  /**
   * Returns the the owner of the <code>AppointmentBook</code> as a <code>String</code>
   */
  @Override
  public String getOwnerName() {
    return this.ownerName;
  }

  /**
   * Returns the collection of <code>AbstractAppointments</code> if it is not empty otherwise
   * returns <code>null</code>
   */
  @Override
  public Collection<AbstractAppointment> getAppointments() {
    return this.appointments;
  }

  /**
   * Adds an <code>AbstractAppointment</code> object to the collection of appointments.
   *
   * @param appointment an <code>AbstractAppointment</code> object to be added to the collection.
   */
  @Override
  public void addAppointment(AbstractAppointment appointment) {
    appointments.add(appointment);
  }

  /**
   * Returns a brief textual description of this appointment book
   */
  @Override
  public String toString() {
    return getOwnerName();
  }

  /**
   * Size int.
   *
   * @return the int
   */
  public int size() {
    return appointments.size();
  }


  /**
   * By range sorted set.
   *
   * @param beginTimeRange the begin time range
   * @param endTimeRange   the end time range
   * @return the sorted set
   */
  public SortedSet<Appointment> byRange(Date beginTimeRange, Date endTimeRange) {

    SortedSet<Appointment> s = new TreeSet<>(Appointment::compareTo);

    s.addAll(getSortedSet()
            .parallelStream()
            .filter(app -> (app.getBeginTime().after(beginTimeRange) && app.getBeginTime().before(endTimeRange)
                    || app.getBeginTime().equals(beginTimeRange)))
            .collect(Collectors.toSet()));

    return s;
  }
}

