package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;


public class Fly extends Entity{

    public Fly(float x, float y) {
        super(x, y, 72, 72);

        Texture trashTexture = new Texture("Fly.png");
        TextureRegion trashFrame = new TextureRegion(trashTexture);

        Array<TextureRegion> singleFrameArray = new Array<>();
        singleFrameArray.add(trashFrame);

        Animation<TextureRegion> staticAnimation = new Animation<>(1f, singleFrameArray, Animation.PlayMode.NORMAL);
        setAnimation(staticAnimation);
    }

    public boolean isClicked(float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width &&
            mouseY >= y && mouseY <= y + height;
    }
}
