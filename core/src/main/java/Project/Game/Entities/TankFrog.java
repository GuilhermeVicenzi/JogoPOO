package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class TankFrog extends Entity {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> damagedAnimation;

    // 🛑 NOVO: Variável para armazenar o limite de HP para a animação de Dano.
    private final float DAMAGED_LIMIT;

    // 🛑 NOVO: Constante para definir a porcentagem de vida para o estado "Damaged" (50%)
    private static final float DAMAGED_THRESHOLD = 0.50f;

    public TankFrog(float x, float y) {
        // Bounds 96x96 (Correto, não requer offset de render)
        super(x, y, 96, 96);

        // 🛑 CORREÇÃO/AJUSTE: Usa float explicitamente e armazena a vida máxima
        this.health = 500.0f;

        // 🛑 NOVO: Calcula o limite de HP (350 * 0.5 = 175.0f)
        this.DAMAGED_LIMIT = this.health * DAMAGED_THRESHOLD;

        this.isAttacking = false; // Garante o estado inicial
        this.moveSpeed = 0.0f; // O sapo NUNCA se move
        this.damage = 0.0f; // Usa float

        // --- Carregamento de Texturas (Sem alterações) ---
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
        super.update(dt); // Chama a lógica da Entity (timers)

        // LÓGICA DE TRANSIÇÃO DE ANIMAÇÃO
        if (!isAlive()) {
            // A lógica de limpeza e animação de morte é tratada pela Entity/GameScreen
        }
        else {
            // 🛑 LÓGICA CRÍTICA: Checa se o HP caiu abaixo do limite de dano
            if (this.health <= this.DAMAGED_LIMIT) {

                // Se a vida está baixa, force a animação de DANO
                if (currentAnimation != damagedAnimation) {
                    setAnimation(damagedAnimation);
                }

            } else {

                // Se a vida está acima do limite, use a animação IDLE (Vida Cheia)
                if (currentAnimation != idleAnimation) {
                    setAnimation(idleAnimation);
                }
            }
        }
    }
}
