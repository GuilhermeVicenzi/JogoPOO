package Project.Game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;

public class Main extends ApplicationAdapter {

    private GameScreen game;

    @Override
    public void create() {
        game = new GameScreen();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        game.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
