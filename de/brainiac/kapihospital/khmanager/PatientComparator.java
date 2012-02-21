/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khmanager;

import de.brainiac.kapihospital.khvalues.Patient;
import java.text.Collator;
import java.util.Comparator;

public class PatientComparator implements Comparator<Patient> {
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

    public PatientComparator(int sortingCriteria, int sortingDirection) {
        _sortingCriteria = sortingCriteria;
        _sortingDirection = sortingDirection;
    }
    
    public int compare(Patient o1, Patient o2) {
        switch(_sortingCriteria) {
            case ID: //ID ignores SortingDirection, just here to restore original Sorting Criteria
                if (o1.getID() < o2.getID())
                    return -1;
                if (o1.getID() > o2.getID())
                    return 1;
                break;
            case NAME:
                Collator myCollator = Collator.getInstance();
                return myCollator.compare(o1.getName(),o2.getName()) * _sortingDirection;
            case DISEASE:
                if (o1.getNumberOfDiseases() < o2.getNumberOfDiseases())
                    return -1 * _sortingDirection;
                if (o1.getNumberOfDiseases() > o2.getNumberOfDiseases())
                    return 1 * _sortingDirection;
                break;
            case ROOM:
                    //return -1 * sortingDirection;
                    //return 1 * sortingDirection;
                break;
            case PRICE:
                if (o1.getMaxPrice() < o2.getMaxPrice())
                    return -1 * _sortingDirection;
                if (o1.getMaxPrice() > o2.getMaxPrice())
                    return 1 * _sortingDirection;
                break;
        }
        return 0;
    }
}