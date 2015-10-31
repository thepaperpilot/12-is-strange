package thepaperpilot.strange.Levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import thepaperpilot.strange.Entities.Max;
import thepaperpilot.strange.Main;

import java.util.ArrayList;

public class Cutscene implements Screen {
    private final static Json json = new Json();
    private final ArrayList<Line> lines = new ArrayList<Line>();
    private final Level level;
    private Table inventoryTable;
    private int line = 0;
    private float time = 0;
    private Stage stage;
    private Stage ui;
    private Scene previous;
    private Max max;

    public Cutscene(CutscenePrototype cutscenePrototype, Scene scene, Level level) {
        previous = scene;
        this.level = level;
        for (LinePrototype prototype : cutscenePrototype.scenes) {
            lines.add(new Line(prototype, this));
        }

        if (lines.size() == 0)
            return;

        stage = new Stage(new StretchViewport(256, 144));
        ui = new Stage(new StretchViewport(640, 360));

        stage.addActor(new Image(scene.background.getDrawable()));

        for (int i = 0; i < scene.entities.size(); i++) {
            stage.addActor((Actor) scene.entities.values().toArray()[i]);
        }
        stage.addActor(max = new Max((int) scene.max.getX(), (int) scene.max.getY()));

        inventoryTable = new Table(Main.skin);
        inventoryTable.setSize(ui.getWidth() - 30, 38);
        inventoryTable.setPosition(30, ui.getHeight() - 38);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(2);

        updateInventory();

        ui.addActor(inventoryTable);

        next();
    }

    public static Cutscene readCutscene(String fileName, Scene scene, Level level) {
        return new Cutscene(json.fromJson(CutscenePrototype.class, LinePrototype.class, Gdx.files.internal("cutscenes/" + fileName + ".json")), scene, level);
    }


    public void say(String message) {
        Main.manager.get("audio/error.wav", Sound.class).play();
        final Label label = new Label(message, Main.skin);
        label.setPosition((int) ((max.getX() + max.getWidth() / 2f) * ui.getWidth() / stage.getWidth() - label.getWidth() / 2f), 5);
        label.addAction(Actions.sequence(Actions.moveBy(0, 0, 1f), Actions.parallel(Actions.moveBy(0, 50, 1), Actions.fadeOut(1)), Actions.run(new Runnable() {
            @Override
            public void run() {
                label.remove();
            }
        })));
        ui.addActor(label);
    }

    public void updateInventory() {
        inventoryTable.clearChildren();
        for (int i = 0; i < level.inventory.size(); i++) {
            inventoryTable.left().add(new Item(level.inventory.get(i))).pad(2).height(32);
        }
    }

    private void next() {
        if (lines.size() <= line) {
            previous.stage.getActors().clear();
            previous.stage.addActor(previous.background);
            for (int i = 0; i < previous.entities.size(); i++) {
                if (((Entity) previous.entities.values().toArray()[i]).visible)
                    previous.stage.addActor((Actor) previous.entities.values().toArray()[i]);
            }
            previous.max = max;
            previous.stage.addActor(previous.max);
            Main.changeScreen(previous);
            return;
        }

        Line nextLine = lines.get(line);
        time = 0;
        for (Effect effect : nextLine.effects) {
            effect.run();
        }

        line++;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        time += delta;
        if (time > lines.get(line).length) next();

        stage.act();
        stage.draw();
        ui.act();
        ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    static class Line {
        float length;
        Effect[] effects;

        Line(LinePrototype prototype, final Cutscene scene) {
            length = prototype.time;
            effects = new Effect[prototype.effects.length];
            for (int i = 0; i < prototype.effects.length; i++) {
                effects[i] = new Effect(prototype.effects[i], scene.level) {
                    public void run() {
                        switch (type) {
                            case REMOVE_ENTITY:
                                Entity entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                entity.visible = false;
                                entity.remove();
                                break;
                            case ADD_ENTITY:
                                entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                entity.visible = true;
                                scene.stage.addActor(entity);
                                break;
                            case MOVE_ENTITY:
                                entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                entity.addAction(Actions.moveBy(Float.valueOf(attributes.get("moveX")), Float.valueOf(attributes.get("moveY")), Float.valueOf(attributes.get("time"))));
                            case REMOVE_ITEM:
                                Item item = level.items.get(attributes.get("targetItem"));
                                level.inventory.remove(item);
                                level.selected.remove(item);
                                level.updateInventory();
                                break;
                            case ADD_ITEM:
                                Main.manager.get("audio/pickup.wav", Sound.class).play();
                                item = level.items.get(attributes.get("targetItem"));
                                level.inventory.add(item);
                                level.updateInventory();
                                break;
                            case REMOVE_BARRIER:
                                scene.previous.obstacles.remove(attributes.get("targetObstacle"));
                            case CHANGE_APPEARANCE:
                                entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                if (attributes.get("type") != null)
                                    entity.type = Entity.Type.valueOf(attributes.get("type"));
                                if (attributes.get("texture") != null)
                                    entity.attributes.put("texture", attributes.get("texture"));
                                if (attributes.get("numFrames") != null)
                                    entity.attributes.put("numFrames", attributes.get("numFrames"));
                                if (attributes.get("speed") != null)
                                    entity.attributes.put("speed", attributes.get("speed"));
                                if (attributes.get("time") != null)
                                    entity.attributes.put("time", attributes.get("time"));
                                entity.updateAppearance();
                                break;
                            case SAY:
                                scene.say(attributes.get("message"));
                                break;
                            case DIALOGUE:
                                scene.ui.addActor(Dialogue.readDialogue(attributes.get("dialogue"), level));
                            case CHANGE_SCREEN:
                                Main.manager.get("audio/error.wav", Sound.class).play();
                                Main.changeScreen(scene);
                                break;
                            case CHANGE_LEVEL:
                                Level level = Level.readLevel(attributes.get("targetLevel"));
                                Main.changeScreen(level.firstScene);
                                break;
                        }
                    }
                };
            }
        }
    }

    static class CutscenePrototype {
        LinePrototype[] scenes;
    }

    static class LinePrototype {
        float time;
        Effect.EffectPrototype[] effects;
    }
}
