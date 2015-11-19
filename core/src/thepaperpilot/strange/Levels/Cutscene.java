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
        // keep this so we can return once the cutscene is over
        previous = scene;

        // our parent, used for setting up the inventory, and to pass to effects
        this.level = level;

        // create the various parts of the cutscene based on the prototypes
        for (LinePrototype prototype : cutscenePrototype.scenes) {
            lines.add(new Line(prototype, this));
        }

        // if our cutscene is empty, let's go ahead and not do anything
        if (lines.size() == 0)
            return;

        // create our stages. One for the game stuff, one for the UI
        stage = new Stage(new StretchViewport(256, 144));
        ui = new Stage(new StretchViewport(640, 360));

        // add our background to the stage
        stage.addActor(new Image(scene.background.getDrawable()));

        // add the other entities to the stage
        for (int i = 0; i < scene.entities.size(); i++) {
            if(((Entity) scene.entities.values().toArray()[i]).visible)
                stage.addActor((Actor) scene.entities.values().toArray()[i]);
        }

        // add max to the stage
        Entity.EntityPrototype maxPrototype = new Entity.EntityPrototype("max", "ANIMATION", (int) scene.max.getX(), (int) scene.max.getY(), true, null);
        maxPrototype.attributes.put("texture", "maxIdle");
        maxPrototype.attributes.put("numFrames", "28");
        maxPrototype.attributes.put("speed", ".1");
        stage.addActor(max = new Entity(maxPrototype, previous));

        // add the inventory table to the ui
        inventoryTable = new Table(Main.skin);
        inventoryTable.setSize(ui.getWidth() - 30, 38);
        inventoryTable.setPosition(30, ui.getHeight() - 38);
        inventoryTable.setBackground(Main.skin.getDrawable("default-round"));
        inventoryTable.pad(2);

        updateInventory();

        ui.addActor(inventoryTable);

        // start the cutscene
        next();
    }

    public static Cutscene readCutscene(String fileName, Scene scene, Level level) {
        // load the cutscene from a file using voodoo magic (reflections)
        // yay for libGDX for making this so easy!
        // Also, it uses prototypes so the file can have only the necessary variables
        // without any boilerplating
        return new Cutscene(json.fromJson(CutscenePrototype.class, LinePrototype.class, Gdx.files.internal("cutscenes/" + fileName + ".json")), scene, level);
    }


    public void say(String message) {
        // create a label below the player that slowly fades out/upwards
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
        // check if we've finished the cutscene
        if (lines.size() <= line) {
            // update the previous screen with the updated entities after the cutscene
            previous.stage.getActors().clear();
            previous.stage.addActor(previous.background);
            for (int i = 0; i < previous.entities.size(); i++) {
                if (((Entity) previous.entities.values().toArray()[i]).visible)
                    previous.stage.addActor((Actor) previous.entities.values().toArray()[i]);
            }
            previous.max.setX(max.getX());
            previous.max.setY(max.getY());
            previous.stage.addActor(previous.max);

            // go back to the previous scene
            Main.changeScreen(previous);
            return;
        }

        // go to the next part of the cutscene
        Line nextLine = lines.get(line);
        time = 0;
        for (Effect effect : nextLine.effects) {
            effect.run();
        }

        line++;
    }

    @Override
    public void show() {
        // do this so the player can't mess with the cutscene by clicking on entities and shit
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // check if its time for the next part of the cutscene
        time += delta;
        if (time > lines.get(line - 1).length) next();

        // update and render everything
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
            // length is the amount of time before the next part of the cutscene
            length = prototype.time;
            // effects are the various actions that happen at each part of the animation
            effects = new Effect[prototype.effects.length];
            for (int i = 0; i < prototype.effects.length; i++) {
                effects[i] = new Effect(prototype.effects[i], scene.level) {
                    // we have to override all of these because the cutscene effectively masquerades as the previous scene
                    // see the original method for comments on what the types are doing
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
                            case REMOVE_ITEM:
                                Item item = level.items.get(attributes.get("targetItem"));
                                level.inventory.remove(item);
                                level.selected.remove(item);
                                level.updateInventory();
                                scene.updateInventory();
                                break;
                            case ADD_ITEM:
                                Main.manager.get("audio/pickup.wav", Sound.class).play();
                                item = level.items.get(attributes.get("targetItem"));
                                level.inventory.add(item);
                                level.updateInventory();
                                scene.updateInventory();
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

    public static class CutscenePrototype {
        LinePrototype[] scenes;
    }

    public static class LinePrototype {
        float time;
        Effect.EffectPrototype[] effects;
    }
}
