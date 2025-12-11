package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Tralalero extends Entity{

    private Animation<TextureRegion> attackingAnimation;
    private Animation<TextureRegion> walkingAnimation;
    // Removendo 'dyingAnimation' localmente para evitar confusão.
    // Usaremos 'this.deathAnimation' da classe Entity.

    // Você não precisa mais dessa variável de velocidade local,
    // pois a velocidade é controlada por this.moveSpeed e getDefaultSpeed().
    // private float speed = 200.0f;

    // Dimensões da textura: 144x72.
    private final float TEXTURE_WIDTH = 144;
    private final float TEXTURE_HEIGHT = 72;


    public Tralalero(float x, float y) {
        // Usando 96x96 para a hitbox (bounds)
        super(x, y, 96, 96);
        this.isAttacking = false;

        this.health = 300;
        this.damage = 50;
        this.attackCooldown = 1.5f; // Exemplo de cooldown

        Texture attackSheet = new Texture("Tralalero/TralaleroTralala-Attacking.png");
        Texture walkingSheet = new Texture("Tralalero/TralaleroTralala-Walking.png");
        Texture dyingSheet = new Texture("Tralalero/TralaleroTralala-Dying.png");

        TextureRegion[][] framesAttacking = TextureRegion.split(attackSheet, (int)TEXTURE_WIDTH, (int)TEXTURE_HEIGHT);
        TextureRegion[][] framesWalking = TextureRegion.split(walkingSheet, (int)TEXTURE_WIDTH, (int)TEXTURE_HEIGHT);
        TextureRegion[][] framesDying = TextureRegion.split(dyingSheet, (int)TEXTURE_WIDTH, (int)TEXTURE_HEIGHT);


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

        this.attackingAnimation = new Animation<>(0.15f, attackFrames, Animation.PlayMode.LOOP);
        this.walkingAnimation = new Animation<>(0.15f, walkFrames, Animation.PlayMode.LOOP);

        // 🛑 CORREÇÃO 1: Usar PlayMode.NORMAL para a morte (roda uma vez)
        Animation<TextureRegion> tempDyingAnimation = new Animation<>(0.15f, dieFrames, Animation.PlayMode.NORMAL);

        // 🛑 CORREÇÃO 2: Atribuir à variável da classe pai (Entity.java)
        this.deathAnimation = tempDyingAnimation;

        setAnimation(walkingAnimation);
    }

    @Override
    public void update(float dt) {
        super.update(dt); // Chama a lógica da Entity (movimento, timers e a transição para Morte)

        // A lógica de transição de morte (isAlive() -> Morte) agora é tratada em Entity.update()

        // LÓGICA DE TRANSIÇÃO DE ANIMAÇÃO (APENAS SE ESTIVER VIVO)
        if (isAlive()) {
            if (isAttacking) {
                // 1. Ataque
                if (currentAnimation != attackingAnimation) {
                    setAnimation(attackingAnimation);
                }
            } else {
                // 2. Andar (Padrão para quem está se movendo)
                if (currentAnimation != walkingAnimation) {
                    setAnimation(walkingAnimation);
                }
            }
        }
        // Se isAlive() for false, o update da Entity já setou a animação de morte (this.deathAnimation).
    }

    @Override
    public void render(SpriteBatch batch) {
        // O método render na Entity.java já foi corrigido para não desenhar após a animação terminar.
        // O offset no Y não é necessário se a posição X, Y e o tamanho da textura estiverem corretos.
        if (currentAnimation != null) {

            // Se estiver morto e a animação tiver acabado, não desenha nada
            if (!isAlive() && this.deathAnimation != null && this.deathAnimation.isAnimationFinished(this.stateTime)) {
                return;
            }

            // A animação de morte NÃO deve dar loop (passamos false para loop)
            boolean looping = isAlive() || (currentAnimation != deathAnimation);
            TextureRegion frame = currentAnimation.getKeyFrame(stateTime, looping);

            // Usa as dimensões da textura (144x72) para desenhar o frame
            // Você pode precisar de um pequeno ajuste manual na coordenada Y se o chão não estiver em Y=0
            batch.draw(frame, x, y, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        }
    }

    @Override
    public float getDefaultSpeed() {
        // Retorna a velocidade que o monstro deve ter quando não estiver em combate.
        return 100.0f;
    }
}
