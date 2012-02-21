/**
 * @author Brainiac
 */
package de.brainiac.kapihospital.khmanager;

import de.brainiac.kapihospital.khvalues.Disease;
import de.brainiac.kapihospital.khvalues.KHValues;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class PointsPanel extends JPanel implements ActionListener {
    private JComboBox _levelSelector;
    private JEditorPane _pointsList;
    private KHValues _khValues;
    private String[] _columnNames;
    private Object[][] _tableData;
    private JTable _diseaseTable;
    private JScrollPane _diseaseScrollPane;

    public PointsPanel() {
        super();
        _khValues = new KHValues();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        String[] allLevel = {"Blutiger Anfänger (1)", "Nachttisch-Reiniger (2)", "Urinbeutelwechsler (3)", "Bettpfannenreiniger (4)", "Pförtner (5)", "Nachtwächter (6)", "Wärmflaschenbefüller (7)", "Spritzenputzer (8)", "Kinderschreck (9)", "Blinddarm-Binder (10)", "Praktikant (11)", "Aushilfe (12)", "Schleimhaut-Schnippler (13)", "Gipsbemaler (14)", "Brechbecherreiniger (15)", "Essenswagenfahrer (16)", "Teddydoktor (17)", "Krankenhaus-Clown (18)", "Blutspender (19)", "Ampullenöffner (20)", "Holzbeinverlängerer (21)", "Arztkittelbügler (22)", "Skalpellschärfer (23)", "Narko...*schnarch* (24)", "Krankenbettenschieber (25)", "Hustensaftschmuggler (26)", "Party-Psychologe (27)", "Tropfsteinleger (28)", "Tablettenbutler (29)", "Hypochonderschreck (30)", "Schöner Chirurg (31)", "Schwesternschreck (32)", "Infusionsbeutelhänger (33)", "Leukozytenzähler (34)", "Organspender (35)", "Zahnsteinmetz (36)", "Medizinmann (37)", "Hämoglobinfärber (38)", "Hexendoktor (39)", "Schonkostkoster (40)", "Voodoo-Priester (41)", "Knochenbrecher (42)", "Sanitäter (43)", "Pflasterkleber (44)", "Stationsschwester (45)", "Zäpfchenkönig (46)", "Blumendoktor (47)", "Pillenmeister (48)", "Veterinär (49)", "Salbenfee (50)", "Krankenkassen-Kassierer (51)", "Bandagenkasper (52)", "Rettungswagenfahrer (53)", "Furunkelfakir (54)", "Erste-Hilfe-Leister (55)", "Schleimbeutelscheich (56)", "Medizinstudent (57)", "Gallensteinmurmler (58)", "Erste-Hilfe-Ausbilder (59)", "Pickelprüfer (60)", "Allgemeinmediziner (61)", "Vorkammerjäger (62)", "Wunderheiler (63)", "Bettenüberzieher (64)", "Dr. Frankenstein (65)", "Schockhocker (66)", "El Doctore (67)", "Tablettenschubse (68)", "verrückter Professor (69)", "Hospital Chef (70)"};
        _columnNames = new String[]{"Krankheiten", "Punkte", "(ppm)"};
        _levelSelector = new JComboBox(allLevel);
        _levelSelector.addActionListener(this);
        add(_levelSelector, BorderLayout.NORTH);
        
        setDiseases(1);
        _diseaseTable = new JTable(_tableData, _columnNames);
        _diseaseScrollPane = new JScrollPane(_diseaseTable);
        _diseaseTable.setFillsViewportHeight(true);
        add(_diseaseScrollPane, BorderLayout.CENTER);
        setPreferredSize(new Dimension(500,600));
    }

    private void setDiseases(int level) {
        int numberOfDiseases = 0;
        Disease[] allDiseases = _khValues.getAllDiseases();
        for (Disease actualDisease : allDiseases) {
            if (actualDisease.getPoints(level) > -1) {
                numberOfDiseases++;
            }
        }
        _tableData = new Object[numberOfDiseases][3];
        int actualRow = 0;
        for (Disease actualDisease : allDiseases) {
            if (actualDisease.getPoints(level) > -1) {
                _tableData[actualRow][0] = actualDisease.getName();
                _tableData[actualRow][1] = actualDisease.getPointsAsString(level);
                _tableData[actualRow][2] = 0;
                actualRow++;
            }
        }
        if (_diseaseScrollPane != null) {
            remove(_diseaseScrollPane);
        }
        _diseaseTable = new JTable(_tableData, _columnNames);
        _diseaseScrollPane = new JScrollPane(_diseaseTable);
        _diseaseTable.setFillsViewportHeight(true);
        add(_diseaseScrollPane, BorderLayout.CENTER);
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        setDiseases(cb.getSelectedIndex()+1);
    }
}