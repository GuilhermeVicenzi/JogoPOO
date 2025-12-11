package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;

public class AttackFrog extends Entity {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> attackingAnimation;

    // 🛑 Constantes para controle
    private static final float RENDER_WIDTH = 288.0F; // Largura total do sprite (inclui a língua estendida)
    private static final float RENDER_HEIGHT = 72.0F;
    private static final float BODY_WIDTH = 96.0F; // Largura real do corpo (hitbox de dano)

    // 🛑 CAIXA DE ALCANCE PÚBLICA (Língua - 288px)
    // Usada no GameScreen para checar se o SAPO ataca o monstro.
    public final Rectangle attackRangeBox;


    public AttackFrog(float x, float y) {
        // Inicializa a Entity com a posição X, Y e largura do SPRITE (288px)
        super(x, y, RENDER_WIDTH, RENDER_HEIGHT);

        // Inicializa a Attack Range Box com o tamanho total do sprite (288px)
        this.attackRangeBox = new Rectangle(x, y, RENDER_WIDTH, RENDER_HEIGHT);

        // --- ANIMAÇÕES ---
        Texture idleSheet = new Texture("FrogNormal/Frog-Normal-Idle.png");
        Texture attackSheet = new Texture("FrogNormal/Frog-Normal-Attack.png");
        TextureRegion[][] framesIdle = TextureRegion.split(idleSheet, 288, 72);
        TextureRegion[][] framesAttacking = TextureRegion.split(attackSheet, 288, 72);

        Array<TextureRegion> idleFrames = new Array();
        Array<TextureRegion> attackFrames = new Array();

        this.moveSpeed = 0.0f; // Sapo não se move
        this.health = 300;
        this.damage = 30; // Dano que o sapo causa ao monstro

        for(int i = 0; i < framesIdle[0].length; ++i) {
            idleFrames.add(framesIdle[0][i]);
        }
        for(int i = 0; i < framesAttacking[0].length; ++i) {
            attackFrames.add(framesAttacking[0][i]);
        }

        this.idleAnimation = new Animation(0.15F, idleFrames, PlayMode.LOOP);
        this.attackingAnimation = new Animation(0.15F, attackFrames, PlayMode.LOOP);
        this.setAnimation(this.idleAnimation);

        // 🛑 HITBOX DE DANO (Corpo - Caixa Vermelha)
        // Define a largura da hitbox de dano (this.bounds) como o corpo real (96px)
        this.bounds.setWidth(BODY_WIDTH);
        this.bounds.setHeight(RENDER_HEIGHT);
    }

    // Sobrescreve o método da Entity, já que o sapo não tem velocidade de movimento
    @Override
    public float getDefaultSpeed() {
        return 0.0f;
    }


    @Override
    public void update(float dt) {
        super.update(dt);

        // 🛑 1. ATUALIZA A POSIÇÃO DA CAIXA DE ALCANCE (Caixa Azul - 288px)
        // Cobre todo o sprite, garantindo que o sapo ataque de longe.
        this.attackRangeBox.setPosition(this.x, this.y);

        // 🛑 2. POSICIONA A HITBOX DE DANO (Caixa Vermelha - 96px)
        // A Hitbox Vermelha deve ficar no lado esquerdo (corpo) do sprite.
        // Já que a largura do sprite é 288px e a hitbox do corpo é 96px,
        // garantimos que this.bounds.x seja igual a this.x (canto esquerdo).
        // A largura (96px) já foi definida no construtor.
        this.bounds.x = this.x; // <--- AGORA A HITBOX VERMELHA COMEÇA NO CANTO ESQUERDO DO SPRITE
        this.bounds.y = this.y;


        // LÓGICA DE TRANSIÇÃO DE ANIMAÇÃO
        if (!isAlive()) {
            // Lógica de Morte (se necessário)
        }
        else if (isAttacking) {
            if (currentAnimation != attackingAnimation) {
                setAnimation(attackingAnimation);
            }
        }
        else {
            if (currentAnimation != idleAnimation) {
                setAnimation(idleAnimation);
            }
        }
    }
}
