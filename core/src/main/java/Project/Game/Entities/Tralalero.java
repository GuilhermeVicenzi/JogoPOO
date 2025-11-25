package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Tralalero extends Entity{

    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> walkingAnimation;
    private Animation<TextureRegion> dyingAnimation;

    public Tralalero(float x, float y) {
        super(x, y, 144, 72);

        Texture attackSheet = new Texture("Tralalero/TralaleroTralala-Attacking.png");
        Texture walkingSheet = new Texture("Tralalero/TralaleroTralala-Walking.png");
        Texture dyingSheet = new Texture("Tralalero/TralaleroTralala-Dying.png");

        TextureRegion[][] framesAttacking = TextureRegion.split(attackSheet, 144, 72);
        TextureRegion[][] framesWalking = TextureRegion.split(walkingSheet, 144, 72);
        TextureRegion[][] framesDying = TextureRegion.split(dyingSheet, 144, 72);


        Array<TextureRegion> attackFrames = new Array<>();
        Array<TextureRegion> walkFrames = new Array<>();
        Array<TextureRegion> dieFrames = new Array<>();

        for (int i = 0; i < framesAttacking[0].length; i++) {
            attackFrames.add(framesAttacking[0][i]);
        }

        for (int i = 0; i < framesWalking[0].length; i++) {
            walkFrames.add(framesWalking[0][i]);
        }

        for (int i = 0; i < framesDying[0].length; i++) {
            dieFrames.add(framesDying[0][i]);
        }

        attackingAnimation = new Animation<>(0.15f, attackFrames, Animation.PlayMode.LOOP);
        walkingAnimation = new Animation<>(0.15f, walkFrames, Animation.PlayMode.LOOP);
        dyingAnimation = new Animation<>(0.15f, dieFrames, Animation.PlayMode.LOOP);

        setAnimation(walkingAnimation);
    }
}
