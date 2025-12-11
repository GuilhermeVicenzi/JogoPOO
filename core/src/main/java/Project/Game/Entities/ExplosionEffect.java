package Project.Game.Entities;

import Project.Game.GameManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.utils.Array;

// NOTA: Esta entidade é puramente visual e de dano. Ela não se move.
public class ExplosionEffect extends Entity {

    // Construtor para criar a explosão no local, tamanho e dano corretos
    // 🛑 AGORA ACEITA renderWidth E renderHeight
    public ExplosionEffect(float x, float y, float renderWidth, float renderHeight, float damage) {

        // CRÍTICO: Chama o construtor Entity com o tamanho de renderização desejado.
        super(x, y, renderWidth, renderHeight);

        // 🛑 NOVO: Define explicitamente o tamanho de RENDERIZAÇÃO da ENTIDADE.
        // Isso garante que o this.width e this.height da ExplosionEffect reflitam o que você quer.
        this.width = renderWidth;
        this.height = renderHeight;

        // Configura o dano
        this.damage = damage;
        this.moveSpeed = 0.0f;
        this.health = 0; // Já nasce morta

        // Carrega e configura a animação de explosão
        loadExplosionAnimation();

        // Garante que o bounds (hitbox) seja minúsculo ou zero, pois é um efeito.
        this.bounds.setWidth(1f);
        this.bounds.setHeight(1f);

        // Inicia o processo de morte (para rodar a deathAnimation imediatamente)
        this.alive = false;
    }

    // Construtor auxiliar caso o GameScreen ainda use 3 argumentos (compatibilidade temporária)
    // 🛑 REMOVER ISTO DEPOIS que o ExplosiveFrog estiver 100% corrigido
    public ExplosionEffect(float x, float y, float damage) {
        this(x, y, 96.0F, 96.0F, damage); // Chama o construtor de 5 argumentos com tamanho padrão 96x96
    }


    private void loadExplosionAnimation() {
        Texture explosionSheet = null;
        String explosionPath = "FrogExplosive/Frog_explosion-Sheet.png";

        try {
            explosionSheet = new Texture(explosionPath);
        } catch (Exception e) {
            System.err.println("❌ ERRO: Falha ao carregar a folha de explosão: " + explosionPath);
            return;
        }

        // Os frames SEMPRE serão splitados no tamanho original da arte (72x72)
        TextureRegion[][] framesExplosion = TextureRegion.split(explosionSheet, 288, 288);
        Array<TextureRegion> explosionFrames = new Array();

        for(int i = 0; i < framesExplosion[0].length; ++i) {
            explosionFrames.add(framesExplosion[0][i]);
        }

        // Configura a deathAnimation (que será usada como a animação principal)
        this.deathAnimation = new Animation(0.1F, explosionFrames, PlayMode.NORMAL);
    }

    @Override
    public float getDefaultSpeed() {
        return 0.0f;
    }

    @Override
    public void update(float dt) {
        // A lógica de morte da Entity irá garantir que a animação rode e depois seja removida
        super.update(dt);

        // Atualiza a posição da hitbox (opcional, mas boa prática)
        this.bounds.x = this.x;
        this.bounds.y = this.y;
    }
}
