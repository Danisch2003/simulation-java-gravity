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
    Point pointStart = null;
    Point pointEnd   = null;
    Planet pl;
    double xDifference = 0;
    double yDifference = 0;
    double distance = 0;

    private Space() {
      this.planeten = new ArrayList<>();
      this.setBackground(Color.WHITE);

      // CONTROLLED SPAWN OF PLANETS
      addMouseListener(new MouseAdapter() {
          public void mousePressed(MouseEvent e) {
              pointStart = e.getPoint();
              pl = new Planet(10, pointStart.x, pointStart.y);
              // repaint();
          }

          public void mouseReleased(MouseEvent e) {
              planeten.add(pl);
              repaint();
              pointStart = null;
          }
      });
      addMouseMotionListener(new MouseMotionAdapter() {
          public void mouseMoved(MouseEvent e) {
              pointEnd = e.getPoint();
          }

          public void mouseDragged(MouseEvent e) {
              pointEnd = e.getPoint();
              xDifference = pointEnd.x - pointStart.x;
              yDifference = pointEnd.y - pointStart.y;
              distance = Math.sqrt(xDifference * xDifference + yDifference * yDifference);
              pl.applyForce(xDifference/distance*0.005,yDifference/distance*0.005);
          }
      });
    }

    public void paint(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); // Die Ellipsen rund zeichnen

      if (pointStart != null) {
        g.setColor(Color.GRAY);
        g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);

        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(pl.getX(), pl.getY(), pl.getX() + pl.getMass()/4, pl.getY() + pl.getMass()/4);
        g2.setColor(Color.BLACK);
        g2.fill(circle);
      }
      /**
       * Für jeden Planet soll die Gravitaionskraft mit jeden anderen Planet berechnet werden
       */
      for (Planet planet : planeten) {
        for (Planet other : planeten) {
          if (planet != other) { // Der andere Planet darf nicht der eigene Planet sein
            double[] force = planet.attract(other);
            double forceX = force[0];
            double forceY = force[1];
            double distance = force[2];

            /**
             * Wenn die Distanz zwischen zwei Planeten unter 5 ist, soll der Planet den anderen Planeten aufnehmen
             */
            if ((distance - planet.getMass()/4) < 2) {
              planet.setMass(other.getMass(), other.velocityX, other.velocityY); // dabei wird die Masse vom anderen Planeten aufgenommen
              planeten.remove(other); // der andere Planet wird aus der Liste entfernt
              repaint();
              break;
            } else {
              planet.applyForce(forceX,forceY);
              planet.update(); // Die berechnete Beschleunigung wird die x und y Position des eigenen Planeten anpassen
            }

          }
        }

        /**
         * Für jeden Planet sollen einfache Ellipsen gezeichnet werden
         */
        Ellipse2D circle = new Ellipse2D.Double();
        circle.setFrameFromCenter(planet.getX(), planet.getY(), planet.getX() + planet.getMass()/4, planet.getY() + planet.getMass()/4);
        g2.setColor(Color.BLACK);
        g2.fill(circle);

      }

      repaint(); // die paint() Methode erneut ausführen
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
