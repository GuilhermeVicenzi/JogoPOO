package Project.Game;

import Project.Game.Entities.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
    public float x, y;
    public boolean occupied = false;
    public final int width = 95;
    public final int height = 95;

    Entity placed;

    public Tile(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    public void onClick(int Id) {
        if (occupied && Id == 5) {
            clear();
            return;
        } else if (Id > 0 && Id < 5 && !occupied) {
            place(Id);
            return;
        }
    }

    public void place(int id) {
        occupied = true;
        System.out.println("Plantando oq tem em: " + id);
        if (id == 1) {placed = new Trash(x, y);}
        if (id == 2) {placed = new AttackFrog(x + 14, y);}
        if (id == 3) {placed = new ExplosiveFrog(x + 15, y);}
        if (id == 4) {placed = new TankFrog(x, y);}

        System.out.println("Agora temos : " + placed + " aqui no tile de " + x + y);
    }



    public void clear() {
        occupied = false;
        System.out.println("Tile limpo");
        placed = null;
    }

    public void update(float dt) {
        if (placed != null) {
            placed.update(dt);
        }
    }


    public void render(SpriteBatch batch) {
        if (placed != null) {
            placed.render(batch);
        }
    }

}
