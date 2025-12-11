package Project.Game.Entities;

import Project.Game.GameManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Null;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class Trash extends Entity{

    private float flySpawnTimer = 0f;
    // A mosca será gerada a cada 15.0 segundos.
    private final float SPAW_INTERVAL = 5.0f;

    public Trash(float x, float y) {
        super(x, y, 96, 96);
        this.moveSpeed = 0.0f;

        // 🛑 AJUSTE: Usando float explicitamente (300.0f) e damage (0.0f) para consistência
        this.health = 300.0f;
        this.damage = 0.0f;

        Texture trashTexture = new Texture("TrashCan.png");
        TextureRegion trashFrame = new TextureRegion(trashTexture);

        Array<TextureRegion> singleFrameArray = new Array<>();
        singleFrameArray.add(trashFrame);

        Animation<TextureRegion> staticAnimation = new Animation<>(1f, singleFrameArray, Animation.PlayMode.NORMAL);
        setAnimation(staticAnimation);

    }

    @Override
    public void update(float dt) {
        // Fazer aqui spawnar as moscas com timer
        super.update(dt);

        flySpawnTimer += dt;

        int offsetX = com.badlogic.gdx.math.MathUtils.random(-60, 60);
        int offsetY = com.badlogic.gdx.math.MathUtils.random(-32, 32);

        if (flySpawnTimer >= SPAW_INTERVAL) {
            flySpawnTimer = 0;

            GameManager.spawnFly(this.x + offsetX, this.y + offsetY);
            System.out.println("Lixeiro gerou uma mosca!");
        }
    }
}
