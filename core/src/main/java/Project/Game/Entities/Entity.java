package Project.Game.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

public abstract class Entity {

    protected float x, y;
    protected float width, height;

    // 🛑 CORREÇÃO: Usar float para health (melhor para cálculos de dano)
    protected float health = 100.0f;
    protected boolean alive = true;

    // Campos de Combate e Estado
    public boolean isAttacking = false;
    public float attackTimer = 0.0f;
    public float attackCooldown = 1.0f;
    // 🛑 CORREÇÃO: Usar float para dano (consistente com o GameScreen)
    public float damage = 10.0f;

    protected float moveSpeed = 50.0f;

    protected Rectangle bounds;

    protected float stateTime = 0f;
    protected Animation<TextureRegion> currentAnimation;

    // 🛑 VARIÁVEIS DE MORTE
    public Animation<TextureRegion> deathAnimation;
    private boolean isDeadPlaying = false;

    // --- CONSTRUTOR ---

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        bounds = new Rectangle(x, y, width, height);
    }

    // --- GETTERS & SETTERS (Omitidos para brevidade) ---
    public float getHealth() {
        return health;
    }
    public float getX() {
        return x;
    }
    public float getY() { return y; }
    public boolean isAlive() { return alive; }
    public void setMoveSpeed(float speed) { this.moveSpeed = speed; }
    public float getDefaultSpeed() { return 50.0f; }
    public Rectangle getBounds() { return bounds; }
    public void setAnimation(Animation<TextureRegion> anim) { this.currentAnimation = anim; }

    // --- UPDATE (Lógica de Movimento e Morte) ---

    public void update(float dt) {
        stateTime += dt;

        // 🛑 LÓGICA DE TRANSIÇÃO PARA MORTE (Executado apenas 1 vez):
        if (!alive && deathAnimation != null && !isDeadPlaying) {
            isDeadPlaying = true;
            stateTime = 0.0f; // 🛑 Zera para ver o primeiro frame da explosão
            currentAnimation = deathAnimation;
            isAttacking = false;
            setMoveSpeed(0.0f);
            return; // CRÍTICO: Não execute nenhuma lógica de entidade viva.
        }

        if (!alive) {
            // Se a morte já começou (isDeadPlaying=true), apenas avança stateTime.
            return;
        }

        // LÓGICA EXCLUSIVA PARA ENTIDADES VIVAS ABAIXO:
        bounds.setPosition(x, y);

        if (moveSpeed > 0) {
            x -= moveSpeed * dt;
            bounds.x = x;
        }

        if (isAttacking) {
            attackTimer += dt;
        }
    }

    // --- RENDER ---

    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {

            // 1. Não renderiza se a animação de morte terminou
            if (isDeadPlaying && animationFinished()) {
                return;
            }

            // 🛑 LÓGICA DE LOOPING:
            boolean shouldLoop = !isDeadPlaying;

            // Certifique-se de que a animação não está no PlayMode.NORMAL se for uma animação infinita de morte/dano
            if (deathAnimation != null && isDeadPlaying) {
                shouldLoop = (deathAnimation.getPlayMode() != PlayMode.NORMAL);
            }

            TextureRegion frame = currentAnimation.getKeyFrame(stateTime, shouldLoop);

            if (frame != null) {
                batch.draw(frame, x, y, width, height);
            }
        }
    }

    // --- COMBATE E VIDA ---

    // 🛑 CORREÇÃO CRÍTICA: Mudar o parâmetro de int para float para aceitar dano do monstro
    public void takeDamage(float amount) {
        if (!alive) return;

        this.health -= amount;

        // Log para debug (opcional, remova depois)
        // System.out.println(this.getClass().getSimpleName() + " tomou " + amount + " dano. HP restante: " + this.health);

        if (this.health <= 0) {
            this.health = 0.0f;
            this.alive = false; // Define como morto
            onDeath();
        }
    }

    protected void onDeath() {}

    // 🛑 MÉTODO CRÍTICO para GameScreen checar se a remoção é segura
    public boolean animationFinished() {
        // Se a animação de morte nunca começou (isDeadPlaying=false), a entidade não deve ser removida por esta lógica.
        if (!isDeadPlaying) {
            return false;
        }

        // Se a morte começou, mas não há animação (segurança), remova.
        if (deathAnimation == null) {
            return true;
        }

        // Verifica se a animação terminou de rodar.
        return deathAnimation.isAnimationFinished(stateTime);
    }
}
