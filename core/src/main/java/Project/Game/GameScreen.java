package Project.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.*;
import java.util.ArrayList;

public class GameScreen {
    private SpriteBatch batch;
    private ShapeRenderer debug;
    private Texture bg;

    private Texture ui;
    Rectangle explosivoUI = new Rectangle(240, 561, 96, 96);
    Rectangle lixoUI = new Rectangle(336, 561, 96, 96);
    Rectangle normalUI = new Rectangle(432, 561, 96, 96);
    Rectangle tankUI = new Rectangle(528, 561, 96, 96);
    Rectangle sal = new Rectangle(624, 586, 96, 75);

    private ArrayList<Rectangle> slots = new ArrayList<Rectangle>();

    private int selectedId;

    private Tile[][] grid;

    public GameScreen() {
        batch = new SpriteBatch();
        debug = new ShapeRenderer();

        bg = new Texture("House.png");
        ui = new Texture("UI.png");

        slots.add(lixoUI);
        slots.add(normalUI);
        slots.add(explosivoUI);
        slots.add(tankUI);
        slots.add(sal);

        createGrid();
    }

    private void createGrid() {
        grid = new Tile[5][9];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                float x = 327 + col * 96;
                float y = 57 + row * 96;

                grid[row][col] = new Tile(x, y);
            }
        }
    }

    public void update(float dt) {
        if (Gdx.input.justTouched()) {

            int mx = Gdx.input.getX();
            int my = Gdx.graphics.getHeight() - Gdx.input.getY();

            System.out.println("Clique em: " + mx + ", " + my);
        }
    }

    public void render(float dt) {
        handleInput();

        update(dt);

        batch.begin();
        batch.draw(bg, 0, 0);
        batch.draw(ui, 0, 0);
        batch.end();

        debug.begin(ShapeRenderer.ShapeType.Line);
        for (Tile[] row : grid) {
            for (Tile t : row) {
                debug.rect(t.x, t.y, t.width, t.height);
            }
        }
        for (Rectangle r : slots) {
            debug.rect(r.x, r.y, r.width, r.height);
        }

        debug.end();
    }

    public void dispose() {
        batch.dispose();
        bg.dispose();
        debug.dispose();
    }

    public void handleInput() {
        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 9; c++) {
                    Tile tile = grid[r][c];
                    if (tile.contains(screenX, screenY)) {
                        if (tile.occupied && selectedId == 5) {
                            tile.clear();
                            return;
                        } else if (selectedId > 0 && selectedId < 5) {
                            tile.place(selectedId);
                            return;
                        }
                    }
                }
            }
            for (int i = 0; i < slots.size(); i++) {
                if (slots.get(i).contains(screenX, screenY)) {
                    selectedId = i + 1;
                    System.out.println("Clicou no slot: " + (i+1));
                    return;
                }
                selectedId = 0;
            }

        }
    }
}
