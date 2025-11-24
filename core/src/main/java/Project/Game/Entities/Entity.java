package Project.Game.Entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {

    protected float x, y;
    protected float width, height;

    protected int health = 100;
    protected boolean alive = true;

    protected Rectangle bounds;

    protected float stateTime = 0f;
    protected Animation<TextureRegion> currentAnimation;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        bounds = new Rectangle(x, y, width, height);
    }

    public void setAnimation(Animation<TextureRegion> anim) {
        this.currentAnimation = anim;
    }

    public void update(float dt) {
        if (!alive) return;

        stateTime += dt;
        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        if (currentAnimation != null) {
            TextureRegion frame = currentAnimation.getKeyFrame(stateTime, true);
            batch.draw(frame, x, y, width, height);
        }
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            alive = false;
            onDeath();
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    protected void onDeath() {}

}
