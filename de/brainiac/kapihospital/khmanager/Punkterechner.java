/**
 * @author Brainiac
 */

package de.brainiac.kapihospital.khmanager;

import java.awt.BorderLayout;
import javax.swing.JApplet;

public class Punkterechner extends JApplet {
    private PointsPanel _pointsPanel;

    @Override
    public void init() {
        _pointsPanel = new PointsPanel();
        getContentPane().add(_pointsPanel, BorderLayout.CENTER);
        setVisible(true);
    }
}