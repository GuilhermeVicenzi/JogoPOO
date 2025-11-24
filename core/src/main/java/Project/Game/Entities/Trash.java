package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Trash extends Entity{
    public Trash(float x, float y) {
        super(x, y, 96, 96);

        Texture trashTexture = new Texture("TrashCan.png");
        TextureRegion trashFrame = new TextureRegion(trashTexture);

        Array<TextureRegion> singleFrameArray = new Array<>();
        singleFrameArray.add(trashFrame);

        Animation<TextureRegion> staticAnimation = new Animation<>(1f, singleFrameArray, Animation.PlayMode.NORMAL);
        setAnimation(staticAnimation);

    }

    @Override
    public void update(float dt) {
        // Fazer aqui spawnar as moscas
    }
}
