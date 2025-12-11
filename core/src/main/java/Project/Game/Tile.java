package Project.Game;

import Project.Game.Entities.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
    public float x, y;
    public boolean occupied = false;
    public final int width = 96;
    public final int height = 96;

    Entity placed;

    // 🛑 Nota: Se você tem uma textura para o Tile/Lixo, ela deve ser carregada aqui.
    // Exemplo: private Texture trashTexture;

    public Tile(float x, float y) {
        this.x = x;
        this.y = y;
        // Exemplo: this.trashTexture = new Texture("Lixo.png");
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
        int money = GameManager.getMoney();

        // As coordenadas x, y aqui são as do Tile, que o sapo usa como base.
        if (id == 1 && money >= 50) {placed = new Trash(x, y); GameManager.spendMoney(50);}
        if (id == 2 && money >= 100) {placed = new AttackFrog(x, y); GameManager.spendMoney(100);}
        if (id == 3 && money >= 150) {placed = new ExplosiveFrog(x, y); GameManager.spendMoney(150);}
        if (id == 4 && money >= 100) {placed = new TankFrog(x, y);GameManager.spendMoney(100);}

        System.out.println("Agora temos : " + placed + " aqui no tile de " + x + y);
    }



    public void clear() {
        occupied = false;
        System.out.println("Tile limpo");
        placed = null; // 🛑 CRÍTICO: Remove a referência da entidade, liberando a hitbox.
    }

    public void update(float dt) {
        if (placed != null) {
            placed.update(dt);
        }
    }


    public void render(SpriteBatch batch) {
        // 🛑 Aqui você desenharia a textura do Tile/Lixo (se aplicável)
        // Exemplo: batch.draw(trashTexture, x, y, width, height);

        // 🛑 Renderiza a entidade, mesmo que esteja morta e animando.
        if (placed != null) {
            placed.render(batch);
        }
    }
}
