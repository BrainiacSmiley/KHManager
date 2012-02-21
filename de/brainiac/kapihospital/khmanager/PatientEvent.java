/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khmanager;

import java.util.EventObject;

public class PatientEvent extends EventObject {
    public static final int VIEW = 1;
    public static final int PROCEED = 2;
    public static final int DELETE = 3;
    public static final int CLOSE = 4;

    private int _eventType;
    private int _patID;
    
    public PatientEvent(Object source, int eventType, int patID) {
        super(source);
        _eventType = eventType;
        _patID = patID;
    }

    public int getEventType() {
        return _eventType;
    }

    public int getPatID() {
        return _patID;
    }
}