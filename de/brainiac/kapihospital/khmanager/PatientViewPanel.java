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
import java.text.DecimalFormat;
import javax.swing.ImageIcon;

public class PatientViewPanel extends PatientPanel implements MouseListener {
    private Patient _actualPatient;
    private KHValues _KHValues;
    private int _actualLevel;

    public PatientViewPanel(int actualLevel) {
        super();
        _actualLevel = actualLevel;
        initComponents();
        _KHValues = new KHValues();
    }

    private void initComponents() {
        String path = "images/patientViewPanel/";
        _background = new ImageIcon(getClass().getResource(path + "background.png"));
        setPreferredSize(new Dimension(_background.getIconWidth(), _background.getIconHeight()));
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g){
        DecimalFormat hTFormat = new DecimalFormat("#,##0.00 hT");
        Font nameFont = new Font("Verdana", Font.BOLD, 18);
        Font normalFont = new Font("Verdana", Font.PLAIN, 11);
        Font diseaseFont = new Font("Verdana", Font.BOLD, 11);
        Font moneyFont = new Font("Verdana", Font.BOLD, 24);
        int yOffset = 10;
        super.paintComponent(g);
        g.drawImage(_background.getImage(), 0, 0, this);
        
        //PatientStats
        Color actualColor = g.getColor();
        g.setColor(Color.BLACK);
        g.setFont(nameFont);
        drawStringCentered(g, _actualPatient.getName(), 297, 47+yOffset);
        g.setFont(normalFont);
        g.setColor(actualColor);
        g.drawString("Geburtsdatum", 150, 74+yOffset);
        g.drawString(_actualPatient.getDOB(), 270, 74+yOffset);
        g.drawString("Geburtsort", 150, 94+yOffset);
        g.drawString(_actualPatient.getPOB(), 270, 94+yOffset);
        g.drawString("Beruf", 150, 114+yOffset);
        g.drawString(_actualPatient.getJob(), 270, 114+yOffset);
        g.drawString("Größe", 150, 134+yOffset);
        g.drawString(_actualPatient.getHeight(), 270, 134+yOffset);
        g.drawString("Gewicht", 370, 134+yOffset);
        g.drawString(_actualPatient.getWeight(), 490, 134+yOffset);
        g.drawString("Hobbies", 150, 154+yOffset);
        g.drawString(_actualPatient.getHobbies(), 270, 154+yOffset);
        
        //Diseases Pictures
        Disease[] patientDiseases = _actualPatient.getDiseases();
        for (int x = 0; x < patientDiseases.length; x++) {
            Disease actualDisease = patientDiseases[x];
            //x + 215
            //y + 55
            g.drawImage(_KHValues.getDiseasesImage(50, actualDisease.getPictureOffset()).getImage(), 75 + x%2*215, 200 + 55*(x/2), this);
            g.setFont(diseaseFont);
            g.drawString(actualDisease.getName(), 130 + x%2*215, 211 + 55*(x/2));
            g.setFont(normalFont);
            g.drawString("Punkte: " + actualDisease.getPointsAsString(_actualLevel), 130 + x%2*215, 211 + 55*(x/2)+15);
            if (!actualDisease.isTreated()) {
                g.drawString("Dauer: " + actualDisease.getDurationAsString(), 130 + x%2*215, 211 + 55*(x/2)+25);
                g.drawString("nicht behandelt", 130 + x%2*215, 211 + 55*(x/2)+35);
            } else {
                g.drawString("behandelt", 130 + x%2*215, 211 + 55*(x/2)+35);                
            }
        }
        g.drawString("zahlt", 145, 430);
        g.drawString("gibt", 145, 450);
        g.setFont(moneyFont);
        g.setColor(Color.RED);
        g.drawString(hTFormat.format(_actualPatient.getMinPrice()) + " - " + hTFormat.format(_actualPatient.getMaxPrice()), 185, 430);
        //g.setColor(actualColor);
        g.drawString(_actualPatient.getPoints(), 185, 460);
   }

    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (mouseX >= 565 && mouseX <= 600 && mouseY >= 35 && mouseY < 70) {
            PatientEvent pe = new PatientEvent(this, PatientEvent.CLOSE, _actualPatient.getID());
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

    public void setActualPatient(Patient newPatient) {
        _actualPatient = newPatient;
    }

    private void drawStringCentered(Graphics g, String text, int centerPos, int y) {
        g.drawString(text, centerPos - (getFontMetrics(g.getFont()).stringWidth(text)) / 2, y);
    }
}