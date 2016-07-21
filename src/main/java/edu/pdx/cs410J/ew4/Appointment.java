package edu.pdx.cs410J.ew4;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * This class extends <code>AbstractAppointment</code> and implements a concrete <code>Appointment</code>.
 *
 * @author Elijah Whitham-Powell
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {
  private String description = "< empty >";
  private String beginTimeString;
  private String endTimeString;
  private Date beginTime;
  private Date endTime;

  /**
   * Instantiates a new Appointment.
   */
  public Appointment() {
  }

  /**
   * Creates a new <code>Appointment</code>
   *
   * @param appointmentInfo an array of strings that will be stored into data fields
   */
  public Appointment(String[] appointmentInfo) {
    this.description = appointmentInfo[0];
//    this.beginTimeString = appointmentInfo[1] + " " + appointmentInfo[2];
//    this.endTimeString = appointmentInfo[3] + " " + appointmentInfo[4];
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    try {
      Date date;
      date = df.parse(appointmentInfo[1] + " " + appointmentInfo[2]);
      this.beginTime = date;
      this.beginTimeString = df.format(this.beginTime);
    } catch (ParseException e) {
      this.beginTime = null;
    }
    try {
      Date date;
      date = df.parse(appointmentInfo[3] + " " + appointmentInfo[4]);
      this.endTime = date;
      this.endTimeString = df.format(this.endTime);
    } catch (ParseException e) {
      this.endTime = null;
    }
  }

  /**
   * Creates a new <code>Appointment</code>
   *
   * @param description     The appointment description, defaults to <code>" empty "</code>
   * @param beginTimeString The time and date the appointment begins as a <code>String</code>.
   * @param endTimeString   The time and date the appointment ends as a <code>String</code>.
   */
  public Appointment(String description, String beginTimeString, String endTimeString) {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    try {
      Date date;
      date = df.parse(beginTimeString);
      this.beginTime = date;
      this.beginTimeString = df.format(this.beginTime);
    } catch (ParseException e) {
      this.beginTime = null;
    }
    try {
      Date date;
      date = df.parse(endTimeString);
      this.endTime = date;
      this.endTimeString = df.format(this.endTime);
    } catch (ParseException e) {
      this.endTime = null;
    }
    this.description = description;
  }

  /**
   * Instantiates a new Appointment.
   *
   * @param description the description
   * @param beginTime   the begin time
   * @param endTime     the end time
   */
  public Appointment(String description, Date beginTime, Date endTime) {
    this.description = description;
    this.beginTime = beginTime;
    this.endTime = endTime;
  }

  /**
   * Returns the beginning time and date of an <code>Appointment</code> as a <code>String</code>
   *
   * @return <code>this.beginTimeString</code>
   */
  @Override
  public String getBeginTimeString() {
//    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
    try {
//      df.format(this.beginTime);
      return df.format(this.beginTime);
    } catch (NullPointerException e) {
      return "";
    }
  }

  /**
   * Sets this <code>Appointment</code> beginning time as a <code>String</code>
   *
   * @param beginTimeString - the time of an <code>Appointment</code> as a <code>String</code>
   * @throws IllegalArgumentException - if a null is passed in it raises exception.
   */
  @Deprecated
  void setBeginTimeString(String beginTimeString) {
    if (beginTimeString == null)
      throw new IllegalArgumentException("beginTimeString cannot be null");
    this.beginTimeString = beginTimeString;
  }

  /**
   * Returns the ending time and date of an <code>Appointment</code> as a <code>String</code>
   *
   * @return <code>this.endTimeString</code>
   */
  @Override
  public String getEndTimeString() {
//    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.US);
    try {
//      df.format(this.endTime);
      return df.format(this.endTime);
    } catch (NullPointerException e) {
      return "";
    }
  }

  /**
   * Sets this <code>Appointment</code> end time as a <code>String</code>
   *
   * @param endTimeString - the end time of an <code>Appointment</code> as a <code>String</code>
   * @throws IllegalArgumentException - if a null is passed in it raises exception.
   */
  @Deprecated
  void setEndTimeString(String endTimeString) {
    if (endTimeString == null)
      throw new IllegalArgumentException("endTimeString cannot be null");
    this.endTimeString = endTimeString;
  }

  /**
   * Returns the description of an <code>Appointment</code> as a <code>String</code>*
   *
   * @return <code>this.description</code>
   */
  @Override
  public String getDescription() {
    return this.description;
  }


  /**
   * Sets this <code>Appointment</code> end time as a <code>String</code>
   *
   * @param description - the description of an <code>Appointment</code> as a <code>String</code>
   * @throws IllegalArgumentException - if a null is passed in it raises exception.
   */
  void setDescription(String description) {
    if (description == null)
      throw new IllegalArgumentException("description cannot be null");
    this.description = description;
  }

  /**
   * Returns the Appointment beginning time as a Date object.
   */
  @Override
  public Date getBeginTime() {
    return this.beginTime;
  }


  /**
   * Returns the Appointment ending time as a Date object.
   */
  @Override
  public Date getEndTime() {
    return this.endTime;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Appointment that = (Appointment) o;
    return Objects.equals(description, that.description) &&
            Objects.equals(beginTimeString, that.beginTimeString) &&
            Objects.equals(endTimeString, that.endTimeString) &&
            Objects.equals(beginTime, that.beginTime) &&
            Objects.equals(endTime, that.endTime);
  }


  @Override
  public int hashCode() {
    return Objects.hash(description, beginTimeString, endTimeString, beginTime, endTime);
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.
   * <p>
   * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
   * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
   * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
   * <tt>y.compareTo(x)</tt> throws an exception.)
   * <p>
   * <p>The implementor must also ensure that the relation is transitive:
   * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
   * <tt>x.compareTo(z)&gt;0</tt>.
   * <p>
   * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
   * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
   * all <tt>z</tt>.
   * <p>
   * <p>It is strongly recommended, but <i>not</i> strictly required that
   * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
   * class that implements the <tt>Comparable</tt> interface and violates
   * this condition should clearly indicate this fact.  The recommended
   * language is "Note: this class has a natural ordering that is
   * inconsistent with equals."
   * <p>
   * <p>In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of
   * <i>expression</i> is negative, zero or positive.
   *
   * @param that the object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   * is less than, equal to, or greater than the specified object.
   * @throws NullPointerException if the specified object is null
   * @throws ClassCastException   if the specified object's type prevents it
   *                              from being compared to this object.
   */
  @Override
  public int compareTo(Appointment that) {
    if (this == that) return 0;
    if (that == null) {
      throw new NullPointerException("Cannot compare null appointment");
    }
    if (this.beginTime.compareTo(that.beginTime) == 0) {
      if (this.endTime.compareTo(that.endTime) == 0) {
        return this.description.compareTo(that.description);
      } else {
        return this.endTime.compareTo(that.endTime);
      }
    } else {
      return this.beginTime.compareTo(that.beginTime);
    }
  }
}
