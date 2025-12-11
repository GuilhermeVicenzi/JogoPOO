package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;

public class Tung extends Entity {

    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> attackAnimation;
    // Removendo dyingAnimation localmente para usar this.deathAnimation da classe pai

    public Tung(float x, float y) {
        // Hitbox: 96x96 (Tamanho do Tile)
        super(x, y, 96.0F, 96.0F);

        this.damage = 30;
        this.health = 300;
        this.attackCooldown = 1.0f; // Cooldown de ataque

        // 🎯 Dimensões do frame da textura
        final int TEXTURE_WIDTH = 108;
        final int TEXTURE_HEIGHT = 135;

        Texture attackSheet = new Texture("Tung/TunTunTunSahur-Attack.png");
        Texture walkingSheet = new Texture("Tung/TunTunTunSahur-Walking.png");
        Texture dyingSheet = new Texture("Tung/TunTunTunSahur-Dying.png");

        // Splitting
        TextureRegion[][] framesAttack = TextureRegion.split(attackSheet, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        TextureRegion[][] framesDying = TextureRegion.split(dyingSheet, TEXTURE_WIDTH, TEXTURE_HEIGHT);
        TextureRegion[][] framesWalk = TextureRegion.split(walkingSheet, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // Inicialização de Frames
        Array<TextureRegion> attackFrames = new Array<>();
        Array<TextureRegion> dyingFrames = new Array<>();
        Array<TextureRegion> walkFrames = new Array<>();

        for (int i = 0; i < framesAttack[0].length; ++i) {
            attackFrames.add(framesAttack[0][i]);
        }
        for (int i = 0; i < framesDying[0].length; ++i) {
            dyingFrames.add(framesDying[0][i]);
        }
        for (int i = 0; i < framesWalk[0].length; ++i) {
            walkFrames.add(framesWalk[0][i]);
        }

        this.attackAnimation = new Animation(0.15F, attackFrames, PlayMode.LOOP);
        this.walkAnimation = new Animation(0.15F, walkFrames, PlayMode.LOOP);

        // 🛑 CORREÇÃO PRINCIPAL: Atribui a animação de morte à variável da classe Entity
        this.deathAnimation = new Animation(0.15F, dyingFrames, PlayMode.NORMAL); // PlayMode.NORMAL é crucial!

        this.setAnimation(this.walkAnimation);
    }

    @Override
    public void update(float dt) {
        super.update(dt); // Chama a lógica da Entity (que agora inicia a animação de morte)

        // LÓGICA DE TRANSIÇÃO DE ANIMAÇÃO (Apenas se estiver VIVO)
        if (isAlive()) {
            if (isAttacking) {
                // 1. Ataque
                if (currentAnimation != attackAnimation) {
                    setAnimation(attackAnimation);
                }
            } else {
                // 2. Andar (Padrão para quem está se movendo)
                if (currentAnimation != walkAnimation) {
                    setAnimation(walkAnimation);
                }
            }
        }
        // Se morreu, a Entity.update() já definiu a animação de morte (this.deathAnimation).
    }

    @Override
    public void render(SpriteBatch batch) {
        // O render na Entity.java já lida com a checagem se a animação terminou.
        if (currentAnimation != null) {

            // Checa se a animação deve rodar em loop. (false se for a animação de morte)
            boolean looping = isAlive() || (currentAnimation != deathAnimation);
            TextureRegion frame = currentAnimation.getKeyFrame(stateTime, looping);

            // 🎯 Usando as constantes de desenho que você definiu:
            final float DRAW_OFFSET_Y = 16.0f;
            final float DRAW_SIZE = 96.0f;
            final float DRAW_OFFSET_X = 0.0f;

            // Desenha o frame completo de 96x96, levantado 16px.
            batch.draw(frame, x + DRAW_OFFSET_X, y + DRAW_OFFSET_Y, DRAW_SIZE, DRAW_SIZE);
        }
    }

    @Override
    public float getDefaultSpeed() {
        return 50.0f;
    }
}
