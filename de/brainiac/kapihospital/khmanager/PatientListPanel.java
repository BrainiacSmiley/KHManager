/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khmanager;

import de.brainiac.kapihospital.khvalues.Disease;
import de.brainiac.kapihospital.khvalues.KHValues;
import de.brainiac.kapihospital.khvalues.Patient;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class PatientListPanel extends PatientPanel implements MouseListener, MouseMotionListener {
    private List<PatientListListener> _patientListListeners;
    private ImageIcon _patientButtons, _patientButtonDeleteSelected, _patientButtonProceedSelected, _patientButtonViewSelected;
    private ImageIcon _sortingUp, _sortingDown;
    private List<Patient> _patientList;
    private int _patientIndex;
    private KHValues _khValues;
    private Double[] _countedListElements;
    private int _hoverdPatient, _hoverdAction;
    private int _sortingCriteria, _sortingDirection;
    
    public PatientListPanel(List<Patient> newPatientList) {
        super();
        _patientListListeners = new ArrayList<PatientListListener>();
        initComponents();
        _khValues = new KHValues();
        _hoverdPatient = -1;
        _hoverdAction = -1;
        _patientIndex = 0;
        _patientList = newPatientList;
        _countedListElements = new Double[]{0.0,0.0};
        countPatientListElements();
        _sortingCriteria = PatientListEvent.ID;
        _sortingDirection = PatientListEvent.ASC;
    }

    @Override
    public void paintComponent(Graphics g){
        int column1 = 30;
        int column2 = 180;
        int column3 = 280;
        int column4 = 420;
        int column5 = 520;
        int yOffset = 133;
        DecimalFormat hTFormat = new DecimalFormat("#,##0.00 hT");
        DecimalFormat diseasesFormat = new DecimalFormat("#,##0");
        Font toppingFont = new Font("Verdana", Font.BOLD, 11);
        Font patientFont = new Font("Verdana", Font.PLAIN, 11);

        super.paintComponent(g);
        g.drawImage(_background.getImage(), 0, 0, this);
        //Toppings
        g.setColor(Color.BLACK);
        g.setFont(toppingFont);
        int toppingOffset = yOffset-15;
        int arrowOffset = toppingOffset-7;
        drawStringCentered(g, "Patientenname", 100, toppingOffset);
        //SortingArrows
        if (_sortingCriteria == PatientListEvent.NAME) {
            if (_sortingDirection == PatientListEvent.ASC) {
                g.drawImage(_sortingUp.getImage(), 150, arrowOffset, this);
            } else {
                g.drawImage(_sortingDown.getImage(), 150, arrowOffset-2, this);
            }
        }
        if (_sortingCriteria == PatientListEvent.DISEASE) {
            drawStringCentered(g, "Krankheiten", 218, toppingOffset);
            if (_sortingDirection == PatientListEvent.ASC) {
                g.drawImage(_sortingUp.getImage(), 260, arrowOffset, this);
            } else {
                g.drawImage(_sortingDown.getImage(), 260, arrowOffset-2, this);
            }
        } else {
            drawStringCentered(g, "Krankheiten", 225, toppingOffset);
        }
        if (_sortingCriteria == PatientListEvent.ROOM) {
            drawStringCentered(g, "Behandlungsraum", 338, toppingOffset);
            if (_sortingDirection == PatientListEvent.ASC) {
                g.drawImage(_sortingUp.getImage(), 400, arrowOffset, this);
            } else {
                g.drawImage(_sortingDown.getImage(), 400, arrowOffset-2, this);
            }
        } else {
            drawStringCentered(g, "Behandlungsraum", 345, toppingOffset);
        }
        drawStringCentered(g, "Verdienst", 465, toppingOffset);
        if (_sortingCriteria == PatientListEvent.PRICE) {
            if (_sortingDirection == PatientListEvent.ASC) {
                g.drawImage(_sortingUp.getImage(), 498, arrowOffset, this);
            } else {
                g.drawImage(_sortingDown.getImage(), 498, arrowOffset-2, this);
            }            
        }
        g.drawString("Aktion", column5, toppingOffset);
        //Lines
        g.drawLine(column1-5, yOffset-14, column5+60, yOffset-14);
        g.drawLine(column2-5, yOffset-25, column2-5, yOffset+290);
        g.drawLine(column3-5, yOffset-25, column3-5, yOffset+290);
        g.drawLine(column4-5, yOffset-25, column4-5, yOffset+290);
        g.drawLine(column5-5, yOffset-25, column5-5, yOffset+290);
        
        //Paint the Patients
        g.setFont(patientFont);
        for (int i = 0+_patientIndex; i < _patientList.size() && i-_patientIndex < 20; i++) {
            Patient actualPatient = _patientList.get(i+_patientIndex);
            //Name
            g.drawString(actualPatient.getName(), column1, yOffset + 15*i);
            //Diseases Pictures
            Disease[] actualDiseases = actualPatient.getDiseases();
            for (int x = 0; x < actualDiseases.length; x++) {
                if (!actualDiseases[x].isTreated()) {
                    g.drawImage(_khValues.getDiseasesImage(15, _khValues.getDiseaseById(actualDiseases[x].getId()).getPictureOffset()).getImage(), column2+15*x, yOffset-12 + 15*i, this);
                }
            }
            //Price
            drawStringRightBound(g, hTFormat.format(actualPatient.getMaxPrice()), column5-10, yOffset + 15*i);
            //Buttons for PatientActions
            g.drawImage(_patientButtons.getImage(), column5, yOffset-12 + 15*i, this);
        }

        //#Patienten
        if (_patientList.size() == 1) {
            g.drawString(_patientList.size() + " Patient", column1, yOffset+305);
        } else {
            g.drawString(_patientList.size() + " Patienten", column1, yOffset+305);
        }
        //#Krankheiten
        if (_countedListElements[0] == 1.0) {
            drawStringCentered(g, diseasesFormat.format(_countedListElements[0]) + " Krankheit", 225, yOffset+305);
        } else {
            drawStringCentered(g, diseasesFormat.format(_countedListElements[0]) + " Krankheiten", 225, yOffset+305);            
        }
        //Summe Verdienst
        drawStringRightBound(g, hTFormat.format(_countedListElements[1]), column5-10, yOffset+305);
        
        //hover Buttons
        if (_hoverdAction == 0) {
            g.drawImage(_patientButtonViewSelected.getImage(), column5, yOffset-12 + 15*_hoverdPatient, this);
        } else if (_hoverdAction == 1) {
            g.drawImage(_patientButtonProceedSelected.getImage(), column5+15, yOffset-12 + 15*_hoverdPatient, this);
        } else if (_hoverdAction == 2) {
            g.drawImage(_patientButtonDeleteSelected.getImage(), column5+30, yOffset-12 + 15*_hoverdPatient, this);
        }
    }

    private void initComponents() {
        String path = "images/patientListPanel/";
        _background = new ImageIcon(getClass().getResource(path + "background.jpg"));
        _patientButtons = new ImageIcon(getClass().getResource(path + "patient_buttons.jpg"));
        _patientButtonProceedSelected = new ImageIcon(getClass().getResource(path + "patient_proceed_selected.jpg"));
        _patientButtonViewSelected = new ImageIcon(getClass().getResource(path + "patient_view_selected.jpg"));
        _patientButtonDeleteSelected = new ImageIcon(getClass().getResource(path + "patient_delete_selected.jpg"));
        _sortingUp = new ImageIcon(getClass().getResource(path + "sorting_arrow_up.gif"));
        _sortingDown = new ImageIcon(getClass().getResource(path + "sorting_arrow_down.gif"));
        setPreferredSize(new Dimension(_background.getIconWidth(), _background.getIconHeight()));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    private void drawStringCentered(Graphics g, String text, int centerPos, int y) {
        g.drawString(text, centerPos - (getFontMetrics(g.getFont()).stringWidth(text)) / 2, y);
    }

    private void drawStringRightBound(Graphics g, String text, int rightPos, int y) {
        g.drawString(text, rightPos - (getFontMetrics(g.getFont()).stringWidth(text)), y);
    }

    public void countPatientListElements() {
        _countedListElements[0] = 0.0;
        _countedListElements[1] = 0.0;
        for (int x = 0; x < _patientList.size(); x++) {
            Patient actualPatient = _patientList.get(x);
            _countedListElements[0] += actualPatient.getDiseases().length;
            _countedListElements[1] += actualPatient.getMaxPrice();
        }
    }

    private void setHoverd(int patient, int action) {
        boolean repaint = false;
        if (_hoverdPatient != patient) {
            _hoverdPatient = patient;
            repaint = true;
        }
        if (_hoverdAction != action) {
            _hoverdAction = action;
            repaint = true;
        }
        if (repaint) {
            repaint();
        }
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (mouseX >= 520 && mouseX <= 565 && mouseY >= 121 && mouseY < 121 + 15*_patientList.size()) {
            setHoverd((mouseY-121)/15, (mouseX-520)/15);
        } else {
            setHoverd(-1, -1);
        }
    }

    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        //Table Toppings
        if (mouseX >= 25 && mouseX <= 565 && mouseY >= 103 && mouseY < 118) {
            if (mouseX >= 25 && mouseX <= 170) {
                setSorting(PatientListEvent.NAME);
            } else if (mouseX >= 180 && mouseX <= 270) {
                setSorting(PatientListEvent.DISEASE);
            } else if (mouseX >= 280 && mouseX <= 410) {
                setSorting(PatientListEvent.ROOM);
            } else if (mouseX >= 420 && mouseX <= 510) {
                setSorting(PatientListEvent.PRICE);
            }
        }
        //PatienActionButtons
        if (mouseX >= 520 && mouseX <= 565 && mouseY >= 121 && mouseY < 121 + 15*_patientList.size()) {
            PatientEvent pe = new PatientEvent(this, _hoverdAction+1, _patientList.get(_hoverdPatient+_patientIndex).getID());
            for (PatientListener listener : _patientListeners) {
                listener.patientEventPerformed(pe);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void addPatientListListener(PatientListListener pll){
      if (_patientListListeners.contains(pll))
            return;
      _patientListListeners.add(pll);
    }

    public void removePatientListListener(PatientListListener pll){
      if (_patientListListeners.contains(pll))
            _patientListListeners.remove(pll);
    }

    private void setSorting(int sortingCriteria) {
        if (_sortingCriteria != sortingCriteria) {
            _sortingCriteria = sortingCriteria;
            _sortingDirection = PatientListEvent.ASC;
        } else {
            if (_sortingDirection == PatientListEvent.ASC) {
                _sortingDirection = PatientListEvent.DESC;
            } else {
                _sortingCriteria = PatientListEvent.ID;
            }
        }
        PatientListEvent ple = new PatientListEvent(this, _sortingCriteria, _sortingDirection);
        for (PatientListListener listener : _patientListListeners) {
            listener.patientListEventPerformed(ple);
        }
    }
}