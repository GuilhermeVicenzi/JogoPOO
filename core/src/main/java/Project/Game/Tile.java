package Project.Game;

public class Tile {
    public float x, y;
    public boolean occupied = false;
    public final int width = 95;
    public final int height = 95;

    public Tile(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public void onClick() {
        System.out.println("Tile clciado em: " + x + y);
        if (!occupied) {
            System.out.println("Tile vazio");
        } else {
            System.out.println("Tile ocupado");
        }
    }
}
