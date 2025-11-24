package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AttackFrog extends Entity{

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> attackingAnimation;


    public AttackFrog (float x, float y) {
        super(x, y, 288, 72);

        Texture idleSheet = new Texture("FrogNormal/Frog-Normal-Idle.png");
        Texture attackSheet = new Texture("FrogNormal/Frog-Normal-Attack.png");

        TextureRegion[][] framesIdle = TextureRegion.split(idleSheet, 288, 72);
        TextureRegion[][] framesAttacking = TextureRegion.split(attackSheet, 288, 72);

        System.out.println("Idle Sheet carregada. Largura: " + idleSheet.getWidth() + ", Altura: " + idleSheet.getHeight());

        Array<TextureRegion> idleFrames = new Array<>();
        Array<TextureRegion> attackFrames = new Array<>();

        for (int i = 0; i < framesIdle[0].length; i++) {
            idleFrames.add(framesIdle[0][i]);
        }

        for (int i = 0; i < framesAttacking[0].length; i++) {
            attackFrames.add(framesAttacking[0][i]);
        }

        idleAnimation = new Animation<>(0.15f, idleFrames, Animation.PlayMode.LOOP);
        attackingAnimation = new Animation<>(0.15f, attackFrames, Animation.PlayMode.LOOP);

        setAnimation(idleAnimation);
    }
}
