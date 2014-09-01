package com.optima.avaya;


import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: smatino
 * Date: 02/04/14
 * Time: 8.55
 * To change this template use File | Settings | File Templates.
 */
public class CalendarBean {

    private String confRoom;
    private String pin;
    private Date start;
    private Date end;
    private String oraInizio;
    private String oraFine;
    private String email;
    private String title;
    private String result;

    public String getOraFine() {
        return oraFine;
    }

    public void setOraFine(String oraFine) {
        this.oraFine = oraFine;
    }

    public String getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(String oraInizio) {
        this.oraInizio = oraInizio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfRoom() {
        return confRoom;
    }

    public void setConfRoom(String confRoom) {
        this.confRoom = confRoom;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
