/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khmanager;

import java.util.EventObject;

public class PatientListEvent extends EventObject {
    //Asceding or Descending sorting
    public static final int ASC = 1;
    public static final int DESC = -1;
    
    //sorting by criterias
    public static final int ID = 1;
    public static final int NAME = 2;
    public static final int DISEASE = 3;
    public static final int ROOM = 4;
    public static final int PRICE = 5;

    private int _sortingDirection;
    private int _sortingCriteria;
    
    public PatientListEvent(Object source, int sortingCriteria, int sortingDirection) {
        super(source);
        _sortingDirection = sortingDirection;
        _sortingCriteria = sortingCriteria;
    }

    public int getSortingDirection() {
        return _sortingDirection;
    }

    public int getSortingCriteria() {
        return _sortingCriteria;
    }
}