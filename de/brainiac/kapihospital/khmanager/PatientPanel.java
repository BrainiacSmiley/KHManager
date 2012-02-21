/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khmanager;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PatientPanel extends JPanel {
    protected List<PatientListener> _patientListeners;
    protected ImageIcon _background;

    public PatientPanel() {
        super();
        _patientListeners = new ArrayList<PatientListener>();
    }

    public void addPatientListener(PatientListener pl){
      if (_patientListeners.contains(pl))
            return;
      _patientListeners.add(pl);
    }

    public void removePatientListener(PatientListener pl){
      if (_patientListeners.contains(pl))
            _patientListeners.remove(pl);
    }
}
