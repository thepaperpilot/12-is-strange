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
    private Entity max;

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
            if(((Entity) scene.entities.values().toArray()[i]).visible)
                stage.addActor((Actor) scene.entities.values().toArray()[i]);
        }
        Entity.EntityPrototype maxPrototype = new Entity.EntityPrototype("max", "ANIMATION", (int) scene.max.getX(), (int) scene.max.getY(), true, null);
        maxPrototype.attributes.put("texture", "maxIdle");
        maxPrototype.attributes.put("numFrames", "28");
        maxPrototype.attributes.put("speed", ".1");
        stage.addActor(max = new Entity(maxPrototype, previous));

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
            previous.max.setX(max.getX());
            previous.max.setY(max.getY());
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
        if (time > lines.get(line - 1).length) next();

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
                                if (attributes.get("targetEntity").equals("max"))
                                    entity = scene.max;
                                entity.visible = false;
                                entity.remove();
                                break;
                            case ADD_ENTITY:
                                entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                if (attributes.get("targetEntity").equals("max"))
                                    entity = scene.max;
                                entity.visible = true;
                                scene.stage.addActor(entity);
                                break;
                            case MOVE_ENTITY:
                                entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                if (attributes.get("targetEntity").equals("max"))
                                    entity = scene.max;
                                entity.addAction(Actions.moveTo(Float.valueOf(attributes.get("newX")), Float.valueOf(attributes.get("newY")), Float.valueOf(attributes.get("time"))));
                                break;
                            case REMOVE_BARRIER:
                                scene.previous.obstacles.remove(attributes.get("targetObstacle"));
                                break;
                            case CHANGE_APPEARANCE:
                                entity = scene.previous.entities.get(attributes.get("targetEntity"));
                                if (attributes.get("targetEntity").equals("max"))
                                    entity = scene.max;
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
                                if (attributes.get("loop") != null)
                                    entity.attributes.put("loop", attributes.get("loop"));
                                if (attributes.get("flip") != null)
                                    entity.attributes.put("flip", attributes.get("flip"));
                                entity.updateAppearance();
                                break;
                            case SAY:
                                scene.say(attributes.get("message"));
                                break;
                            case DIALOGUE:
                                scene.ui.addActor(Dialogue.readDialogue(attributes.get("dialogue"), level));
                                break;
                            default:
                                super.run();
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
