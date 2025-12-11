package Project.Game.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;

public class ExplosiveFrog extends Entity {
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> attackingAnimation;

    private Animation<TextureRegion> countdownAnimation;

    public boolean isCountingDown = false;
    public final float COUNTDOWN_DURATION = 2.0f;

    // 🛑 NOVO: Sinaliza ao GameScreen que a contagem terminou e a explosão deve ser criada.
    public boolean isReadyToExplode = false;

    // --- CONSTRUTOR ---

    public ExplosiveFrog(float x, float y) {
        super(x, y, 72.0F, 72.0F);

        Texture idleSheet = new Texture("FrogExplosive/Frog-Explosive-Idle.png");
        Texture attackSheet = new Texture("FrogExplosive/Frog-Explosive-Attack.png");
        Texture countdownSheet = attackSheet;

        // 🛑 REMOÇÃO: Removemos o carregamento da explosionSheet e a definição do this.deathAnimation.
        // O ExplosiveFrog não lida mais com a animação de morte/explosão final.

        // --- SPLIT DE FRAMES (72x72) ---
        TextureRegion[][] framesIdle = TextureRegion.split(idleSheet, 72, 72);
        TextureRegion[][] framesAttacking = TextureRegion.split(attackSheet, 72, 72);

        Array<TextureRegion> idleFrames = new Array();
        Array<TextureRegion> attackFrames = new Array();
        // Array<TextureRegion> explosionFrames = new Array(); // REMOVIDO

        this.moveSpeed = 0.0f;
        this.health = 200;
        this.damage = 250;
        this.attackCooldown = 0.50f;
        this.attackTimer = 0.0f;

        // Carrega frames Idle e Attack
        for(int i = 0; i < framesIdle[0].length; ++i) { idleFrames.add(framesIdle[0][i]); }
        for(int i = 0; i < framesAttacking[0].length; ++i) { attackFrames.add(framesAttacking[0][i]); }

        // Configuração das animações locais
        this.idleAnimation = new Animation(0.15F, idleFrames, PlayMode.LOOP);
        this.attackingAnimation = new Animation(0.15F, attackFrames, PlayMode.LOOP);

        // Configura a Animação de Contagem (Looping rápido por 2s)
        this.countdownAnimation = new Animation(0.1F, attackFrames, PlayMode.LOOP);

        // 🛑 REMOÇÃO: A lógica abaixo não é mais necessária:
        // if (explosionSheet != null) { ... this.deathAnimation = ... }

        this.setAnimation(this.idleAnimation);

        // CORREÇÃO CRÍTICA: Ajusta o tamanho da ÁREA DE DESENHO para 96x96
        this.width = 72.0F;
        this.height = 72.0F;
    }


    @Override
    public float getDefaultSpeed() {
        return 0.0f;
    }

    @Override
    public void update(float dt) {

        super.update(dt);

        this.bounds.x = this.x;
        this.bounds.y = this.y;

        // 🛑 LÓGICA DE CONTROLE DA CONTAGEM REGRESSIVA
        if (isCountingDown) {

            // 1. Garante que a animação de contagem esteja ativa
            if (currentAnimation != countdownAnimation) {
                setAnimation(countdownAnimation);
            }

            // 2. O attackTimer agora é o timer da contagem
            attackTimer += dt;

            // 3. Se a contagem terminou:
            if (attackTimer >= COUNTDOWN_DURATION) {

                isCountingDown = false;
                attackTimer = 0.0f;

                // 🛑 CRÍTICO: Sinaliza ao GameScreen para criar a ExplosionEffect
                this.isReadyToExplode = true;

                // 🛑 CRÍTICO: Mata o sapo para que o Tile seja limpo no GameScreen.
                this.takeDamage(9999);

                System.out.println("Sapo: Fim da contagem regressiva. Sinalizando a criação da explosão!");
            }
            return;
        }

        // LÓGICA EXCLUSIVA PARA ENTIDADES VIVAS ABAIXO:
        if (isAlive()) {
            if (isAttacking) {
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

    /**
     * Inicia a contagem regressiva para a explosão.
     */
    public void startCountdown() {
        if (!isCountingDown) {
            isCountingDown = true;
            isAttacking = true;
            attackTimer = 0.0f;
            System.out.println("Sapo: Contagem regressiva de explosão iniciada.");
        }
    }

    /**
     * 🛑 NOVO: Cria e retorna a entidade de explosão, sinalizando que a explosão foi tratada.
     */
    public ExplosionEffect getExplosion() {
        if (isReadyToExplode) {
            // Reseta o sinalizador
            isReadyToExplode = false;
            // 🛑 CORREÇÃO APLICADA: Inclui this.width e this.height para corresponder ao novo construtor de ExplosionEffect.
            return new ExplosionEffect(this.x, this.y, this.width, this.height, this.damage);
        }
        return null;
    }
}
