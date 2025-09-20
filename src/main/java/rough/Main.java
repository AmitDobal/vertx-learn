package rough;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Main {

  public static void main(String[] args) {
    Packet p1 = new Packet(1, 2, 100);
    Packet p2 = new Packet(1, 2, 200);
    Packet p3 = new Packet(1, 2, 100);

    Set<Packet> s = new HashSet();
    s.add(p1);
    s.add(p2);
    s.add(p3);

    System.out.println(p1.equals(p2));
    System.out.println(p1.equals(p3));
    System.out.println(s.size());

  }
}

class Packet {
  int source;
  int destination;
  int timestamp;

  Packet(int source, int destination, int timestamp) {
    this.source = source;
    this.destination = destination;
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Packet packet = (Packet) o;
    return source == packet.source && destination == packet.destination && timestamp == packet.timestamp;
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, destination, timestamp);
  }
}
