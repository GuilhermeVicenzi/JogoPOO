package Project.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class GameScreen {
    private SpriteBatch batch;
    private ShapeRenderer debug;
    private Texture bg;

    private Tile[][] grid;

    public GameScreen() {
        batch = new SpriteBatch();
        debug = new ShapeRenderer();

        // Carrega sua imagem de fundo (coloque ela na pasta assets/)
        bg = new Texture("House.png");

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
        batch.end();

        debug.begin(ShapeRenderer.ShapeType.Line);
        for (Tile[] row : grid) {
            for (Tile t : row) {
                debug.rect(t.x, t.y, t.width, t.height);
            }
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
                        tile.onClick();
                        return;
                    }
                }
            }
        }
    }
}
