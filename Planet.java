import java.lang.Math;

/**
 * Die Klasse Planet hat die physikalischen Eigenschaften eines Planetens
 *
 * @author Danisch
 * @author Mathilda
 * @author Emil
 * @version 1.0
 */

class Planet {

  private double x;
  private double y;
  public double mass; // Masse des Planetens
  public double accX; // Beschleunigung in X
  public double accY; // Beschleunigung in Y
  public double forceX;
  public double forceY;
  public double velocityX;
  public double velocityY;

  public Planet(double mass, double x, double y) {
    this.x = x;
    this.y = y;
    this.mass = mass;
    this.accX = 0;
    this.accY = 0;
    this.forceX = 0;
    this.forceY = 0;
    this.velocityX = 0;
    this.velocityY = 0;
  }

  // Getter Methoden
  public double getX() {
    return this.x;
  }
  public double getY() {
    return this.y;
  }
  public double getMass() {
    return this.mass;
  }

  // Methode mit der die Masse des Planetens verändert wird
  public void setMass(double addedMass, double otherVelocityX, double otherVelocityY) {
    this.velocityX = (((this.mass * this.velocityX) + (addedMass * otherVelocityX)) / ( this.mass + addedMass));
    this.velocityY = (((this.mass * this.velocityY) + (addedMass * otherVelocityY)) / ( this.mass + addedMass));
    this.accX = 0;
    this.accY = 0;
    this.mass += addedMass;
  }

  // Distanzen zwischen Punkten berechnen
  public double calculateDistX(double x2) {
      return x2 - this.x;
  }
  public double calculateDistY(double y2) {
    return y2 - this.y;
  }
  public double calculateDistanceBetweenPoints(double diffX, double diffY) {
   return Math.sqrt(diffX * diffX + diffY * diffY);
 }

  // Gravitaionskraft zwichen zwei Planeten berechnet
  public double[] attract(Planet planet) {

    double xDifference = 0;
    double yDifference = 0;
    double strength = 0;
    double distance = 0;
    double G = 6.674 * Math.pow(10,-4);

    xDifference = calculateDistX(planet.getX());
    yDifference = calculateDistY(planet.getY());
    distance = calculateDistanceBetweenPoints(xDifference, yDifference);

    // Newtons Gravitaionsgesetz: F = (G*m*M)/r²
    strength = ((G * planet.getMass() * this.mass) / (distance * distance));
    this.forceX = (xDifference/distance) * strength;
    this.forceY = (yDifference/distance) * strength;

    return new double[] {this.forceX, this.forceY, distance};
  }

  // Aus der Gravitaionskraft wird die Beschleunigung des Planetens berechnet
  public void applyForce(double forceX, double forceY) {
    // a = F/m
    this.accX += forceX / this.mass;
    this.accY += forceY / this.mass;
  }

  // Aus der Beschleunigung wird die x und y Position bestimmt
  public void update() {
    this.velocityX += this.accX;
    this.velocityY += this.accY;
    this.x += this.velocityX;
    this.y += this.velocityY;
    this.accX = 0;
    this.accY = 0;
  }
}
