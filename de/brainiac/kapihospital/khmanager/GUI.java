/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khmanager;

import de.brainiac.kapihospital.khvalues.KHValues;
import de.brainiac.kapihospital.khvalues.Patient;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorEvent;
import java.awt.datatransfer.FlavorListener;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class GUI extends JFrame implements ActionListener, FlavorListener, PatientListener, PatientListListener {
    private ImageIcon _toggleParseImage;
    private JToggleButton _toggleParseButton;
    private JToolBar _buttonBar;
    private KHValues _khValues;
    private List<Patient> _patientList;
    private Boolean _parsing;
    private PatientListPanel _patientListPanel;
    private PatientViewPanel _patientViewPanel;
    private DatabaseHandler _dbHandler;
    private JPanel _cardPanel;
    private TrayIcon _trayIcon;
    private Image _trayImage;
    private PopupMenu _trayPopupMenu;
    private MenuItem _trayExitMenuItem;
    private CheckboxMenuItem _trayToggleParseButton;

    public GUI() {
        _patientList = new ArrayList<Patient>();
        _dbHandler = new DatabaseHandler("khmanager", "khmanager", "manager");
        _patientList = _dbHandler.getPatients();
        _parsing = false;

        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public void flavorsChanged(FlavorEvent e) {
        if (_parsing) {
            try {
                Transferable actualClipboardContent = ((Clipboard)e.getSource()).getContents(null);
                if (actualClipboardContent.isDataFlavorSupported(new DataFlavor("text/html;class=java.lang.String"))) {
                    String patientFromClipboard = ((Clipboard)e.getSource()).getData(new DataFlavor("text/html;class=java.lang.String")).toString();
                    Patient actualPatient = _khValues.parsePatientFromHTML(patientFromClipboard);
                    if (!_patientList.contains(actualPatient)) {
                        actualPatient.setID(_dbHandler.insertPatient(actualPatient));
                        _patientList.add(actualPatient);
                        _patientListPanel.countPatientListElements();
                        repaint();
                    }
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("Prof.Dr.Mule"), null);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        
        if (actionCommand.equalsIgnoreCase("toggleParse")) {
            toggleParsing();
        }
    }

    private void initComponents() {
        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(this);
        _khValues = new KHValues();
        _patientListPanel = new PatientListPanel(_patientList);
        _patientListPanel.addPatientListener(this);
        _patientListPanel.addPatientListListener(this);

        _toggleParseImage = new ImageIcon(getClass().getResource("images/buttons/button3.gif"));

        _buttonBar = new JToolBar();
        _toggleParseButton = new JToggleButton(_toggleParseImage);
        _toggleParseButton.setActionCommand("toggleParse");
        _toggleParseButton.addActionListener(this);
        _buttonBar.add(_toggleParseButton);
        _buttonBar.setFloatable(false);
        
        getContentPane().add(_buttonBar, BorderLayout.PAGE_START);
        _cardPanel = new JPanel();
        _cardPanel.setLayout(new CardLayout());
        _cardPanel.add(_patientListPanel, "LIST");
        _patientViewPanel = new PatientViewPanel(23);
        _patientViewPanel.addPatientListener(this);
        _cardPanel.add(_patientViewPanel, "PATIENT");
        getContentPane().add(_cardPanel, BorderLayout.CENTER);
        setTitle("KHManager");
        
        //Tray Menu
        _trayImage = Toolkit.getDefaultToolkit().getImage("src/de/brainiac/kapihospital/khmanager/images/icon.png");
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            MouseListener mouseListener = new MouseListener() {

                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        setVisible(!isVisible());
                    }
                }

                public void mouseEntered(MouseEvent e) {
                }

                public void mouseExited(MouseEvent e) {
                }

                public void mousePressed(MouseEvent e) {
                }

                public void mouseReleased(MouseEvent e) {
                }
            };

            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };

            _trayPopupMenu = new PopupMenu();
            _trayToggleParseButton = new CheckboxMenuItem("Toggle Parsing");
            _trayToggleParseButton.setActionCommand("toggleParse");
            _trayToggleParseButton.addActionListener(this);
            _trayPopupMenu.add(_trayToggleParseButton);
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            _trayPopupMenu.add(defaultItem);

            _trayIcon = new TrayIcon(_trayImage, "KH Manager", _trayPopupMenu);

            _trayIcon.setImageAutoSize(true);
            _trayIcon.addMouseListener(mouseListener);

            try {
                tray.add(_trayIcon);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        } else {
            _trayIcon = new TrayIcon(_trayImage);
        }
        pack();
    }

    public void patientEventPerformed(PatientEvent pe) {
        int patID = pe.getPatID();
        int event = pe.getEventType();
        Patient actualPatient = null;
        for (int x = 0; x < _patientList.size(); x++) {
            if (_patientList.get(x).getID() == patID) {
                actualPatient = _patientList.get(x);
                break;
            }
        }
        if (event == PatientEvent.VIEW) {
            _patientViewPanel.setActualPatient(actualPatient);
            CardLayout cl = (CardLayout)(_cardPanel.getLayout());
            cl.next(_cardPanel);
        } else if (event == PatientEvent.PROCEED) {
            
        } else if (event == PatientEvent.DELETE) {
            deletePatient(actualPatient);
        } else if (event == PatientEvent.CLOSE) {
            CardLayout cl = (CardLayout)(_cardPanel.getLayout());
            cl.next(_cardPanel);
        }
    }

    private void deletePatient(Patient actualPatient) {
        String question = "Wollen Sie wirklich den Patienten " + actualPatient.getName() + " löschen?";
        if (JOptionPane.showConfirmDialog(this, question, "Patient wirklich löschen?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            _patientList.remove(actualPatient);
            _dbHandler.deletePatient(actualPatient);
        }
    }

    public void patientListEventPerformed(PatientListEvent ple) {
        int sortingCriteria = ple.getSortingCriteria();
        int sortingDirection = ple.getSortingDirection();
        
        // hier wird sortiert
        Comparator<Patient> comparator = new PatientComparator(sortingCriteria, sortingDirection);
        Collections.sort(_patientList, comparator);
        repaint();
    }

    private void toggleParsing() {
        _parsing = !_parsing;
        _toggleParseButton.setSelected(!_parsing);
        _trayToggleParseButton.setState(_parsing);
        if (_parsing) {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("Prof.Dr.Mule"), null);
        }
        repaint();
    }
}