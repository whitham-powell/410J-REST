package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.AbstractAppointment;
import edu.pdx.cs410J.AbstractAppointmentBook;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


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
//    if (appointments.isEmpty()) {
//      return null;
//    } else {
      return this.appointments;
//    }
  }

  /**
   * Adds an <code>AbstractAppointment</code> object to the collection of appointments.
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

}

