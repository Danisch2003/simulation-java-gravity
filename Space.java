import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


import java.awt.event.*;
import java.awt.geom.Ellipse2D;


/**
 * Die Klasse Space beinhaltet die Planeten
 *
 * @author Danisch
 * @author Mathilda
 * @author Emil
 * @version 1.0
 */

public class Space extends JPanel {

    private ArrayList<Planet> planeten;

    private Space() {
      this.planeten = new ArrayList<>();
      this.setBackground(Color.BLACK);

      // CONTROLLED SPAWN OF PLANETS
      addMouseListener(new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
              int x = e.getX();
              int y = e.getY();

              Planet planet = new Planet(5, (double)x, (double)y);
              planeten.add(planet);
              repaint();
          }
      });

      // // RANDOM SPAWN OF PLANETS
      // Random rand = new Random();
      // for (int i = 0; i < 4; i++) {
      //   Planet pl = new Planet(1+rand.nextInt(6), 100+rand.nextInt(300), 50+rand.nextInt(300));
      //   planeten.add(pl);
      // }
      // repaint();
    }

    public void paint(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;

      /**
       * Für jeden Planet soll die Gravitaionskraft mit jeden anderen Planet berechnet werden
       */
      for (Planet planet : planeten) {
        for (Planet other : planeten) {
          if (planet != other) { // Der andere Planet darf nicht der eigene Planet sein
            double[] force = other.attract(planet);
            double forceX = force[0];
            double forceY = force[1];
            double distance = force[2];

            planet.applyForce(forceX,forceY);
            /**
             * Wenn die Distanz zwischen zwei Planeten unter 5 ist, soll der Planet den anderen Planeten aufnehmen
             */
            // BUG Wenn der Planet kollidiert dann wird er schneller!
            if (distance < 5) {
              planet.setMass(other.getMass()); // dabei wird die Masse vom anderen Planeten aufgenommen
              planeten.remove(other); // der andere Planet wird aus der Liste entfernt
              repaint();
              // break;
            }
          }
        }
        planet.update(); // Die berechnete Beschleunigung wird die x und y Position des eigenen Planeten anpassen

        /**
         * Für jeden Planet sollen einfache Ellipsen gezeichnet werden
         */
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(planet.getX(), planet.getY(), planet.getX() + planet.getMass()/4, planet.getY() + planet.getMass()/4);
        g2.setColor(Color.white);
        g2.fill(circle);
        // Die Ellipsen rund zeichnen
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
      }

      repaint(); // die paint() Methode erneut starten
    }

    public static void main(String[] args) {
      // Eine Grafischeoberfläsche wirde initialisiert
      JFrame frame = new JFrame();
      frame.setTitle("Gravitational field");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(400, 400);
      frame.setVisible(true);
      frame.add(new Space());
    }
}
