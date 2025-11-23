package Project.Game;

public class Tile {
    public float x, y;
    public boolean occupied = false;
    public final int width = 95;
    public final int height = 95;

    public Entity placed;

    public Tile(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public void onClick(int Id) {
        System.out.println("Tile clciado em: " + x + y);
        if (!occupied && Id != 5 && Id != 0) {
            System.out.println("Tile vazio");
            System.out.println("Plantar oq tem no id " + Id);
        }
    }

    public void place(int id) {
        occupied = true;
        System.out.println("Plantando oq tem em: " + id);
    }



    public void clear() {
        occupied = false;
        placed = null;
        System.out.println("Tile limpo");
    }
}
