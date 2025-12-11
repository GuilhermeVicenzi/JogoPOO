package Project.Game;

import Project.Game.Entities.Fly;
import com.badlogic.gdx.Gdx;
import Project.Game.Tile;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import java.util.Random;
import Project.Game.Entities.Cappuccino;
import Project.Game.Entities.Tralalero;
import Project.Game.Entities.Tung;
import Project.Game.Entities.Entity;
import Project.Game.Entities.AttackFrog;
import Project.Game.Entities.ExplosiveFrog;
import Project.Game.Entities.ExplosionEffect;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class GameScreen {

    public enum GameState { RUNNING, GAME_OVER }
    private GameState currentState = GameState.RUNNING;
    private Texture gameOverScreen;

    private SpriteBatch batch;
    private ShapeRenderer debug;
    private Texture bg;

    private Texture ui;
    Rectangle explosivoUI = new Rectangle(240, 561, 96, 96);
    Rectangle lixoUI = new Rectangle(336, 561, 96, 96);
    Rectangle normalUI = new Rectangle(432, 561, 96, 96);
    Rectangle tankUI = new Rectangle(528, 561, 96, 96);
    Rectangle sal = new Rectangle(624, 586, 96, 75);
    private ArrayList<Rectangle> slots = new ArrayList<Rectangle>();
    private Rectangle tryAgainButton;

    private BitmapFont font;
    private float spawnTimer = 0.0f;
    private final float SPAWN_INTERVAL = 5.0f;
    private boolean firstSpawnDone = false;
    private final Random random = new Random();
    private static final int TILE_WIDTH = 96;
    private static final int TILE_HEIGHT = 96;
    private static final float GRID_START_Y = 57.0f;
    private static final float GAME_OVER_X = 327.0f;

   //Temporizador e intervalo para o spawn das moscas
    private float flySpawnTimer = 0.0f;
    private final float FLY_SPAWN_INTERVAL = 10.0f; // 30 segundos

    private final Class<?>[] monsterTypes = {
        Cappuccino.class,
        Tralalero.class,
        Tung.class
    };

    Array<Entity> monsters = new Array<>();
    Array<Fly> flies = new Array<>();
    // Fly teste = new Fly(200, 200); // Não precisamos mais desta instância única

    private void spawnMonster(Class<?> monsterClass, float x, float y) {
        if (monsterClass.equals(Cappuccino.class)) {
            monsters.add(new Cappuccino(x, y));
        } else if (monsterClass.equals(Tralalero.class)) {
            monsters.add(new Tralalero(x, y));
        } else if (monsterClass.equals(Tung.class)) {
            monsters.add(new Tung(x, y));
        }
    }

    private void spawnFlyAt(float x, float y) {
        flies.add(new Fly(x, y));
    }

    private int selectedId;
    private Tile[][] grid;


    // Adiciona as moscas fixas que aparecem no início do jogo.

    private void addInitialFlies() {
        // Moscas que nascem PARADAS
        flies.add(new Fly(200, 200));
        flies.add(new Fly(100, 300));
        flies.add(new Fly(250, 450));
        flies.add(new Fly(100, 380));
        flies.add(new Fly(150, 250));
        System.out.println("Moscas iniciais adicionadas.");
    }


    public GameScreen() {
        batch = new SpriteBatch();
        debug = new ShapeRenderer();

        bg = new Texture("House.png");
        ui = new Texture("UI.png");

        gameOverScreen = new Texture("GameOverScreen.png");
        tryAgainButton = new Rectangle(400, 80, 500, 100);

        GameManager.setFlySpawnCallback(this::spawnFlyAt);

        slots.add(lixoUI);
        slots.add(normalUI);
        slots.add(explosivoUI);
        slots.add(tankUI);
        slots.add(sal);

        createGrid();

        // Adiciona as moscas iniciais
        addInitialFlies();

        font = new BitmapFont();
        font.getData().setScale(2f);
        font.setColor(0,0,0,1);
    }

    private void createGrid() {
        grid = new Tile[5][9];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                float x = 327 + col * 96;
                float y = 57 + row * 96;

                grid[row][col] = new Tile(x, y);
            }
        }
    }


    public void update(float dt) {

        if (currentState == GameState.GAME_OVER) {
            handleGameOverInput();
            return;
        }

        GameManager.handleTimer(dt);
        handleInput();

        // ATUALIZAÇÃO DOS TILES
        for (Tile[] row : grid) {
            for (Tile t : row) {
                t.update(dt);
            }
        }

        // ATUALIZAÇÃO DAS MOSCAS (Queda controlada)
        for (int i = flies.size - 1; i >= 0; i--) {
            Fly fly = flies.get(i);
            fly.update(dt);
        }

        // LIMPEZA DE ESTADO - Reset de Animação de Ataque
        for (Tile[] row : grid) {
            for (Tile t : row) {
                if (t.occupied && t.placed != null) {
                    t.placed.isAttacking = false;
                }
            }
        }


        // --- LÓGICA DE SPAWN DE MONSTROS (Seu código original) ---
        spawnTimer += dt;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnTimer = 0.0f;

            float totalTime = GameManager.getTimer();
            int minMonsters = 1;
            int maxMonsters = 1;

            if (!firstSpawnDone) {
                firstSpawnDone = true;
            } else if (totalTime < 120.0f) {
                maxMonsters = 2;
            } else if (totalTime < 300.0f) {
                maxMonsters = 4;
            } else {
                maxMonsters = 6;
            }


            int monstersToSpawn = random.nextInt(maxMonsters - minMonsters + 1) + minMonsters;

            for (int i = 0; i < monstersToSpawn; i++) {
                Class<?> monsterType = monsterTypes[random.nextInt(monsterTypes.length)];
                int randomRow = random.nextInt(grid.length);
                float spawnX = 1200.0f;
                float spawnY = grid[randomRow][0].y;

                spawnMonster(monsterType, spawnX, spawnY);
            }
        }

        // LÓGICA DE SPAWN DE MOSCAS (CAINDO DO CÉU)
        flySpawnTimer += dt;
        if (flySpawnTimer >= FLY_SPAWN_INTERVAL) {
            flySpawnTimer = 0.0f;

            int minX = (int)GAME_OVER_X;
            int maxX = Gdx.graphics.getWidth() - 100;

            float randomX = minX + random.nextInt(maxX - minX);
            float randomTargetY = 100.0f + random.nextInt(400);
            float spawnY = Gdx.graphics.getHeight() + 50.0f;

            flies.add(new Fly(randomX, spawnY, randomTargetY));
            System.out.println("Mosca caiu do céu em: (" + randomX + ", " + spawnY + ") - Parando em Y: " + randomTargetY);
        }


        // 🌟 LÓGICA DE ATUALIZAÇÃO, COLISÃO E COMBATE FINAL
        for (int i = monsters.size - 1; i >= 0; i--) {
            Entity monster = monsters.get(i);

            if (monster.isAlive()) {

                Entity bodyCollisionFrog = null;
                Entity rangeAttackerFrog = null;

                boolean isMonsterAtBody = false;

                int monsterRow = Math.round((monster.getY() - GRID_START_Y) / TILE_HEIGHT);

                if (monsterRow >= 0 && monsterRow < grid.length) {

                    // --- ETAPA 1: Procurar Colisão de CORPO (Prioridade Máxima) e Atacante de ALCANCE ---
                    for (Tile t : grid[monsterRow]) {
                        if (t.occupied && t.placed != null) {
                            Entity frog = t.placed;

                            // Detecção de Colisão de Corpo
                            if (monster.getBounds().overlaps(frog.getBounds())) {
                                bodyCollisionFrog = frog;
                                isMonsterAtBody = true;
                                break; // Prioriza Body Collision e sai do loop
                            }

                            // Detecção de Ataque de Alcance
                            if (frog instanceof AttackFrog) {
                                AttackFrog attackFrog = (AttackFrog) frog;
                                if (attackFrog.attackRangeBox.overlaps(monster.getBounds())) {
                                    rangeAttackerFrog = frog;
                                }
                            }
                        }
                    }
                }

                boolean canFrogAttack = rangeAttackerFrog != null || bodyCollisionFrog != null;


                // 3. Lógica de Combate e Movimento

                // LÓGICA 1: SAPO ATACANDO O MONSTRO
                if (canFrogAttack) {

                    // Determina o sapo que está CAUSANDO o dano
                    // Se houver sapo de corpo, ele ataca. Senão, é o de alcance.
                    Entity currentAttacker = bodyCollisionFrog != null ? bodyCollisionFrog : rangeAttackerFrog;

                    if (currentAttacker != null) {

                        // GARANTE A ANIMAÇÃO CORRETA:
                        // Se o atacante é de alcance E não está bloqueado por um sapo na frente, ele anima.
                        // Se o atacante é de corpo, ele anima.
                        if (currentAttacker == rangeAttackerFrog || (currentAttacker == bodyCollisionFrog && currentAttacker instanceof AttackFrog)) {
                            currentAttacker.isAttacking = true;
                        }

                        // Se houver um atacante de alcance E houver um sapo de corpo,
                        // a animação do sapo de alcance também deve ser ativada, mesmo que o corpo assuma o dano.
                        if (rangeAttackerFrog != null && bodyCollisionFrog != null) {
                            rangeAttackerFrog.isAttacking = true;
                            // No entanto, o dano continua vindo do bodyCollisionFrog (currentAttacker)
                        }

                        // LÓGICA ESPECIAL PARA SAPO EXPLOSIVO
                        if (currentAttacker instanceof ExplosiveFrog) {
                            ExplosiveFrog explosiveFrog = (ExplosiveFrog) currentAttacker;

                            if (isMonsterAtBody && !explosiveFrog.isCountingDown && explosiveFrog.isAlive()) {
                                explosiveFrog.startCountdown();
                            }

                            if (explosiveFrog.isReadyToExplode) {
                                ExplosionEffect explosion = explosiveFrog.getExplosion();
                                if (explosion != null) {
                                    monsters.add(explosion);
                                    monster.takeDamage(explosion.damage);
                                    explosiveFrog.isAttacking = false;
                                }
                            }
                        }
                        // LÓGICA NORMAL PARA OUTROS AttackFrog/Entidades
                        else {
                            // Aplicação de Dano
                            currentAttacker.attackTimer += dt;
                            if (currentAttacker.attackTimer >= currentAttacker.attackCooldown) {
                                currentAttacker.attackTimer = 0.0f;

                                monster.takeDamage(currentAttacker.damage);

                                if (!monster.isAlive()) {
                                    currentAttacker.isAttacking = false;
                                    // Se o monstro morrer, o Cleanup Loop cuidará do reset da animação do rangeAttackerFrog
                                }
                            }
                        }
                    }
                }

                // LÓGICA 2: MONSTRO ATACANDO E PARANDO
                if (isMonsterAtBody) {
                    monster.isAttacking = true;
                    monster.setMoveSpeed(0.0f);

                    if (bodyCollisionFrog != null) {
                        monster.attackTimer += dt;
                        if (monster.attackTimer >= monster.attackCooldown) {
                            monster.attackTimer = 0.0f;

                            System.out.println("Monstro ataca! Dano: " + monster.damage +
                                " | Alvo: " + bodyCollisionFrog.getClass().getSimpleName() +
                                " | Vida atual: " + bodyCollisionFrog.getHealth());

                            // APLICAÇÃO DE DANO GARANTIDA:
                            bodyCollisionFrog.takeDamage(monster.damage);

                            if (!bodyCollisionFrog.isAlive()) {
                                bodyCollisionFrog.isAttacking = false;
                            }
                        }
                    }

                } else {
                    monster.isAttacking = false;
                    monster.setMoveSpeed(monster.getDefaultSpeed());

                    // ATIVAR GAME OVER se atingir a base
                    if (monster.getX() < GAME_OVER_X) {
                        System.out.println("Monstro atingiu a base! GAME OVER.");
                        currentState = GameState.GAME_OVER;

                        for (Entity m : monsters) {
                            m.setMoveSpeed(0.0f);
                        }
                        return;
                    }
                }
            }

            // 4. Atualiza o monstro
            monster.update(dt);
        }

        // FIM DA LÓGICA DE COMBATE

        for (Tile[] row : grid) {
            for (Tile t : row) {
                if (t.occupied && t.placed != null) {
                    Entity frog = t.placed;

                    if (!frog.isAlive()) {

                        // 1. Caso ExplosiveFrog (usa t.clear() na lógica do GameScreen)
                        if (frog instanceof ExplosiveFrog) {
                            // Se o ExplosiveFrog já atingiu 0 HP e disparou a explosão, ele deve ser limpo.
                            t.clear();
                        }
                        // 2. Caso TankFrog ou AttackFrog (Limpeza imediata)
                        // Se a entidade está morta E não tem animação de morte complexa para esperar
                        else if (frog.deathAnimation == null || frog.animationFinished()) {
                            t.clear();
                        }
                    }
                }
            }
        }


        // 5. Remove monstros mortos (e ExplosionEffects)
        for (int i = monsters.size - 1; i >= 0; i--) {
            Entity monster = monsters.get(i);

            if (!monster.isAlive()) {

                if (monster.deathAnimation == null || monster.animationFinished()) {
                    monsters.removeIndex(i);
                    if (!(monster instanceof ExplosionEffect)) {
                        GameManager.addScore(10);
                    }
                }
            }
        }
    }

    public void render(float dt) {

        batch.begin();
        batch.draw(bg, 0, 0);
        batch.draw(ui, 0, 0);

        for (Tile[] row : grid) {
            for (Tile t : row) {
                t.render(batch);
            }
        }

        for (Fly fly : flies) {
            fly.render(batch);
        }

        for (Entity monster : monsters) {
            monster.render(batch);
        }

        font.draw(batch, "" + GameManager.getMoney(), 165, 590);

        String timeDisplay = String.format("Tempo: %02d:%02d", GameManager.getMinutes(), GameManager.getSeconds());
        font.draw(batch, timeDisplay, 885, 650);

        font.draw(batch, "Pontuação: " + GameManager.getScore(), 1075,650);

        if (currentState == GameState.GAME_OVER) {
            batch.draw(gameOverScreen, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            font.draw(batch, "", tryAgainButton.x + 50, tryAgainButton.y + 70);
        }

        batch.end();

        //  BLOCO DE DEBUG (HITBOXES) COMENTADO:
        /*
        if (currentState == GameState.RUNNING) {
            debug.begin(ShapeRenderer.ShapeType.Line);

            for (Tile[] row : grid) {
                for (Tile t : row) {
                    debug.rect(t.x, t.y, t.width, t.height);
                }
            }
            for (Rectangle r : slots) {
                debug.rect(r.x, r.y, r.width, r.height);
            }

            debug.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
            for (Entity monster : monsters) {
                debug.rect(monster.getBounds().x, monster.getBounds().y, monster.getBounds().width, monster.getBounds().height);
            }

            for (Tile[] row : grid) {
                for (Tile t : row) {
                    if (t.occupied && t.placed != null) {

                        debug.setColor(com.badlogic.gdx.graphics.Color.RED);
                        debug.rect(t.placed.getBounds().x, t.placed.getBounds().y, t.placed.getBounds().width, t.placed.getBounds().height);

                        if (t.placed instanceof AttackFrog) {
                            AttackFrog attackFrog = (AttackFrog) t.placed;
                            debug.setColor(com.badlogic.gdx.graphics.Color.BLUE);
                            debug.rect(attackFrog.attackRangeBox.x, attackFrog.attackRangeBox.y, attackFrog.attackRangeBox.width, attackFrog.attackRangeBox.height);
                        }
                    }
                }
            }
            debug.setColor(com.badlogic.gdx.graphics.Color.WHITE);
            debug.end();
        } else {
            // Este bloco desenha a hitbox do botão Try Again
            debug.begin(ShapeRenderer.ShapeType.Line);
            debug.setColor(com.badlogic.gdx.graphics.Color.GREEN);
            debug.rect(tryAgainButton.x, tryAgainButton.y, tryAgainButton.width, tryAgainButton.height);
            debug.end();
        }
        */
    }

    public void dispose() {
        batch.dispose();
        bg.dispose();
        debug.dispose();
        gameOverScreen.dispose();
    }

    public void handleGameOverInput() {
        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (tryAgainButton.contains(screenX, screenY)) {
                restartGame();
            }
        }
    }

    private void restartGame() {
        GameManager.reset();

        monsters.clear();
        flies.clear();

        //Recria as moscas que aparecem no mapa inicial
        addInitialFlies();

        for (Tile[] row : grid) {
            for (Tile t : row) {
                t.clear();
            }
        }

        spawnTimer = 0.0f;
        flySpawnTimer = 0.0f; // Zera o temporizador de moscas caindo
        firstSpawnDone = false;

        currentState = GameState.RUNNING;
        System.out.println("Jogo reiniciado.");
    }


    public void handleInput() {
        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (int i = flies.size - 1; i >= 0; i--) {
                Fly fly = flies.get(i);

                if (fly.isClicked(screenX, screenY)) {
                    GameManager.addMoney(50);
                    flies.removeIndex(i);
                    System.out.println("Coletou uma mosca, dinheiro agora é " + GameManager.getMoney());
                }
            }

            for (int r = 0; r < 5; r++) {
                for (int c = 0; c < 9; c++) {
                    Tile tile = grid[r][c];
                    if (tile.contains(screenX, screenY)) {
                        tile.onClick(selectedId);
                    }
                }
            }

            for (int i = 0; i < slots.size(); i++) {
                if (slots.get(i).contains(screenX, screenY)) {
                    selectedId = i + 1;
                    System.out.println("Clicou no slot: " + (i+1));
                    return;
                }
                selectedId = 0;
            }
        }
    }
}
