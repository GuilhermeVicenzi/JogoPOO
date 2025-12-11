package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Fly extends Entity {

    private static final float FALL_SPEED = -150.0f;

    // 🛑 NOVO: Flag para controlar se a mosca está em queda livre
    private boolean isFalling = false;

    // 🛑 NOVO: Posição Y onde a mosca deve parar (apenas para as moscas que caem)
    private float targetY = 0.0f;

    // Construtor 1 (Para moscas INICIAIS, que nascem PARADAS)
    public Fly(float x, float y) {
        // As moscas iniciais NÃO CAEM.
        this(x, y, false, y);
    }

    // Construtor 2 (Para moscas que caem do céu)
    // O targetY será definido no GameScreen
    public Fly(float x, float y, float targetY) {
        // As moscas do céu COMEÇAM CAINDO.
        this(x, y, true, targetY);
    }

    // Construtor principal para inicialização
    private Fly(float x, float y, boolean startFalling, float targetY) {
        super(x, y, 72, 72);

        this.isFalling = startFalling;
        this.targetY = targetY; // Define o ponto de parada

        this.damage = 0;
        this.health = 1;

        Texture trashTexture = new Texture("Fly.png");
        TextureRegion trashFrame = new TextureRegion(trashTexture);

        Array<TextureRegion> singleFrameArray = new Array<>();
        singleFrameArray.add(trashFrame);

        Animation<TextureRegion> staticAnimation = new Animation<>(1f, singleFrameArray, Animation.PlayMode.NORMAL);
        setAnimation(staticAnimation);

        this.moveSpeed = 0.0f;
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        // 🛑 LÓGICA DE QUEDA CONTROLADA
        if (isFalling) {
            // Se a mosca ainda está acima do ponto de parada
            if (this.y > targetY) {
                // Continua caindo
                this.y += FALL_SPEED * dt;

                // Se a queda a levou abaixo do ponto de parada, PARE exatamente no ponto
                if (this.y <= targetY) {
                    this.y = targetY;
                    isFalling = false; // 🛑 PAROU DE CAIR
                    System.out.println("Mosca parou em Y: " + targetY);
                }
            } else {
                // Caso contrário (já está na posição ou abaixo), pare
                isFalling = false;
            }
        }

        this.bounds.x = this.x;
        this.bounds.y = this.y;
    }

    @Override
    public float getDefaultSpeed() {
        return 0.0f;
    }

    public boolean isClicked(float mouseX, float mouseY) {
        return this.bounds.contains(mouseX, mouseY);
    }
}
