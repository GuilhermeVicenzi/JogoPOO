package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TankFrog extends Entity {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> damagedAnimation;

    public TankFrog(float x, float y) {
        super(x, y, 96, 96);
        this.health = 500;

        Texture idleSheet = new Texture("FrogTank/Frog-Tank-FullLife.png");
        Texture damagedSheet = new Texture("FrogTank/Frog-Tank-Damaged.png");

        TextureRegion[][] framesIdle = TextureRegion.split(idleSheet, 96, 96);
        TextureRegion[][] framesDamaged = TextureRegion.split(damagedSheet, 96, 96);

        Array<TextureRegion> idleFrames = new Array<>();
        Array<TextureRegion> damagedFrames = new Array<>();

        for (int i = 0; i < framesIdle[0].length; i++) {
            idleFrames.add(framesIdle[0][i]);
        }

        for (int i = 0; i < framesDamaged[0].length; i++) {
            damagedFrames.add(framesDamaged[0][i]);
        }

        idleAnimation = new Animation<>(0.15f, idleFrames, Animation.PlayMode.LOOP);
        damagedAnimation = new Animation<>(0.15f, damagedFrames, Animation.PlayMode.LOOP);

        setAnimation(idleAnimation);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        if (health < 250) {
            setAnimation(damagedAnimation);
        }
    }

}
