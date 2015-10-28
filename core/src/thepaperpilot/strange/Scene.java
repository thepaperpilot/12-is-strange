package thepaperpilot.strange;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import thepaperpilot.strange.Entities.*;
import thepaperpilot.strange.Screens.ChoicesScreen;
import thepaperpilot.strange.Screens.GameScreen;
import thepaperpilot.strange.Screens.JunkyardCutscene;

public enum Scene {
    FIRST(1, "bathroom") {
        public void init() {
            new Entity(new Image(Main.entities.findRegion("schoolDoor")).getDrawable(), screen, 10, 32) {
                public void onTouch() {
                    Main.changeScreen(new ChoicesScreen(-1, "Where do you want to go?", new String[]{"Hang outside", "Study inside"}, new GameScreen[]{SECOND.screen, THIRD.screen}, screen));
                }
            };
            new Puzzle(new Image(Main.entities.findRegion("fireAlarmPuzzle")).getDrawable(), screen, 210, 60, Item.HAMMER) {
                public void onFail() {
                    screen.say("I need to find something to break this with");
                }

                public void onSuccess() {
                    setDrawable(new Image(Main.entities.findRegion("fireAlarmPuzzleBroken")).getDrawable());
                    screen.say("whoah! what was a gun doing in there?");
                    Main.inventory.add(Item.GUN);
                }
            };
            Item.SOAP.place(screen, 230, 16);
        }

        public void previous() {
            // lol
        }
    },
    SECOND(2, "outside") {
        public void init() {
            new AnimatedPuzzle(screen, 81, 10, Main.animations.findRegion("catIdle"), 14, 1 / 6f, Item.CAT_FOOD) {
                public void onFail() {
                    screen.say("This cat looks bored. I hear cats hunt birds when they're bored.");
                }

                public void onSuccess() {
                    setAnimation(Main.animations.findRegion("catEating"), 2, 1 / 6f);
                    screen.say("aww. it looks so happy now!");
                    new Entity(new Image(Main.entities.findRegion("sugarWorld")).getDrawable(), FOURTH.screen, (int) screen.stage.getWidth() / 3, 10) {
                        public void onTouch() {
                            Main.manager.get("audio/pickup.wav", Sound.class).play();
                            Main.inventory.add(Item.SUGAR);
                            Scene.updateInventory();
                            remove();
                        }
                    };
                    GameScreen.bird.alive = true;
                }
            };
            screen.clock.setPosition(148, 55);
            Item.DOG_BONE.place(screen, 200, 14);
        }

        public void previous() {
            Main.changeScreen(FIRST.screen);
        }
    },
    THIRD(3, "school") {
        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("schoolDoor")).getDrawable(), screen, 48, 16, Item.KEYS) {
                public void onFail() {
                    screen.say("it's locked");
                }

                public void open() {
                    Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, screen));
                }
            };
            new Puzzle(new Image(Main.entities.findRegion("AlyssaSoapPuzzle")).getDrawable(), screen, 192, 10, Item.SOAP) {
                public void onFail() {
                    screen.ui.addActor(Dialogue.readDialogue("dialogue/Alyssa1.json"));
                }

                public void onSuccess() {
                    screen.say("thank you Alyssa!");
                    Main.inventory.add(Item.KEYS);
                }

                public void open() {
                    screen.say("I already gave her the soap. What more do you want?");

                }
            };
            Item.SODA.place(screen, 80, 16);
        }

        public void previous() {
            Main.changeScreen(FIRST.screen);
        }
    },
    FOURTH(4, "outside") {
        public void init() {
            new Entity(new Image(Main.entities.findRegion("tirestack")).getDrawable(), screen, 230, 13) {
                public void onTouch() {
                    Main.changeScreen(FIFTH.screen);
                }
            };
            new Entity(new Image(Main.entities.findRegion("stairs")).getDrawable(), screen, 0, 7) {
                public void onTouch() {
                    Main.changeScreen(SIXTH.screen);
                }
            };
            new AnimatedPuzzle(screen, 160, 13, Main.animations.findRegion("dogIdle"), 14, 1 / 6f, Item.DOG_BONE) {
                public void onFail() {
                    screen.say("This dog won't let me through. It looks hungry");
                }

                public void onSuccess() {
                    screen.say("look how cute it is!");
                    setAnimation(Main.animations.findRegion("dogHappy"), 2, 1 / 6f);
                    screen.obstacles.remove(0);
                }

                public void open() {
                    screen.say("The dog is much happier now that it has a bone");
                }
            };
            screen.clock.setPosition(148, 55);
            screen.obstacles.add(new Rectangle(160, 0, 16, screen.stage.getHeight()));
            GameScreen.bird = new Bird(screen, 73, 10);
            Item.CAT_FOOD.place(screen, 170, 13);
            Item.WEED_KILLER.place(screen, 21, 7);
        }

        public void previous() {
            Main.changeScreen(THIRD.screen);
        }
    },
    FIFTH(5, "junkyard") {
        boolean carBlock = true;
        boolean ductTape = true;

        public void init() {
            final Entity usb = new Entity(new Image(Main.entities.findRegion("usbWorld")).getDrawable(), screen, 80, 16) {
                public void onTouch() {
                    if (!carBlock) {
                        Main.manager.get("audio/pickup.wav", Sound.class).play();
                        Main.inventory.add(Item.USB);
                        Scene.updateInventory();
                        remove();
                    } else screen.say("it's blocked by this car");
                }
            };
            final Entity ductTapeEntity = new Entity(new Image(Main.entities.findRegion("ductTapeWorld")).getDrawable(), screen, 40, 16) {
                public void onTouch() {
                    Main.manager.get("audio/pickup.wav", Sound.class).play();
                    Main.inventory.add(Item.DUCT_TAPE);
                    Scene.updateInventory();
                    remove();
                    ductTape = false;
                }
            };
            new Entity(new Image(Main.entities.findRegion("carPuzzle")).getDrawable(), screen, 80, 16) {
                public void onTouch() {
                    if (Main.selected.contains(Item.GUN) && Main.selected.contains(Item.BOTTLES)) {
                        carBlock = false;
                        Actor[] actors = new Actor[ductTape ? 3 : 2];
                        actors[0] = screen.clock;
                        actors[1] = usb;
                        if (ductTape) actors[2] = ductTapeEntity;
                        Main.changeScreen(new JunkyardCutscene(actors, this, screen.max));
                    } else if (carBlock) screen.say("I should use this car to break some bottles");
                }
            };
            usb.setZIndex(1);
        }

        public void previous() {
            Main.changeScreen(FOURTH.screen);
        }
    },
    SIXTH(6, "school") {
        boolean doorBlocked = true;

        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("schoolDoor")).getDrawable(), screen, 48, 16, Item.MAKE_SHIFT_BOMB) {
                public void onFail() {
                    screen.say("it's locked. I think I can blow it open\nusing some sugar, weed killer, duct tape, and a soda can");
                }

                public void onSuccess() {
                    Main.manager.get("audio/explosion.wav", Sound.class).play();
                    setDrawable(new Image(Main.entities.findRegion("breakableDoorPuzzle")).getDrawable());
                    screen.stage.addActor(new Explosion(((int) getX()), ((int) getY()) + 8));
                    screen.say("I sure hope no one heard that");
                }

                public void open() {
                    Main.changeScreen(OFFICE.screen);
                }
            };
            new Puzzle(new Image(Main.entities.findRegion("crowdCameraPuzzle")).getDrawable(), screen, 180, 14, Item.CAMERA) {
                public void onFail() {
                    screen.say("This crowd of people would make a great shot");
                }

                public void onSuccess() {
                    doorBlocked = false;
                    Main.inventory.add(Item.PHOTO);
                    addAction(Actions.sequence(Actions.moveBy(100, 0, 4), Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            remove();
                        }
                    })));
                    screen.say("they're going away. must be camera shy");
                }
            };
            new Entity(new Image(Main.entities.findRegion("schoolDoor")).getDrawable(), screen, 192, 16) {
                public void onTouch() {
                    if (!doorBlocked)
                        Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, screen));
                }
            };
        }

        public void previous() {
            Main.changeScreen(FOURTH.screen);
        }
    },
    OFFICE(6, "office") { // sub-level of 6

        public void init() {
            new Entity(new Image(Main.entities.findRegion("breakableDoorPuzzle")).getDrawable(), screen, 4, 16) {
                public void onTouch() {
                    Main.changeScreen(SIXTH.screen);
                }
            };
            screen.clock.setPosition(100, 57);
            screen.stage.getViewport().setWorldSize(120, 80);
            screen.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            Item.CAMERA.place(screen, 46, 33);
            Item.FILES.place(screen, 71, 30);
        }

        public void previous() {
            Main.changeScreen(FOURTH.screen);
        }
    },
    SEVENTH(7, "junkyard") {
        public void init() {
            new AnimatedPuzzle(screen, 230, 13, Main.animations.findRegion("balloon"), 14, .1f, new Item[0]) {
                public void onTouch() {
                    Main.changeScreen(new ChoicesScreen(-1, "Are you a nerd?", new String[]{"Go party!", "Go study?"}, new GameScreen[]{EIGTH.screen, NINTH.screen}, screen));
                }
            };
            Item.ALCOHOL.place(screen, 15, 32);
            Item.HAMMER.place(screen, 170, 16);
        }

        public void previous() {
            Main.changeScreen(SIXTH.screen);
        }
    },
    EIGTH(8, "vortex") {
        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("bouncer")).getDrawable(), screen, 192, 10, Item.ALCOHOL) {
                public void onFail() {
                    screen.say("I'm not allowed in the VIP area. Maybe if I brought him some alcohol");
                }

                public void onSuccess() {
                    screen.obstacles.remove(0);
                    screen.say("here you go");
                }

                public void open() {
                    screen.say("I bribed the bouncer with alcohol, and now he'll let me in the VIP area");
                }
            };
            screen.obstacles.add(new Rectangle(192, 0, 16, screen.stage.getHeight()));
            Item.BOTTLES.place(screen, 225, 13);
            Item.FIRE_EXTINGUISHER.place(screen, 10, 16);
        }

        public void previous() {
            Main.changeScreen(SEVENTH.screen);
        }
    },
    NINTH(9, "school") {
        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("warrenPuzzle")).getDrawable(), screen, 192, 10, Item.USB) {
                public void onFail() {
                    screen.ui.addActor(Dialogue.readDialogue("dialogue/Warren1.json"));
                }

                public void open() {
                    Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, screen));
                }
            };
        }

        public void previous() {
            Main.changeScreen(SEVENTH.screen);
        }
    },
    TENTH(10, "dorm") {
        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("schoolDoor")).getDrawable(), screen, 4, 16, Item.FIRE_EXTINGUISHER) {
                public void onFail() {
                    screen.say("it's locked");
                }

                public void onSuccess() {
                    Main.manager.get("audio/explosion.wav", Sound.class).play();
                    setDrawable(new Image(Main.entities.findRegion("breakableDoorPuzzle")).getDrawable());
                    screen.stage.addActor(new Explosion(((int) getX()), ((int) getY()) + 8));
                }

                public void open() {
                    Main.changeScreen(new ChoicesScreen(-1, "Do you need to go?", new String[]{"Go to the bathroom", "Stay in class"}, new GameScreen[]{ELEVENTH.screen, TWELTH.screen}, screen));
                }
            };
            screen.clock.setPosition(100, 57);
            screen.stage.getViewport().setWorldSize(120, 80);
            screen.stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            Item.PHONE.place(screen, 56, 13);
        }

        public void previous() {
            Main.changeScreen(NINTH.screen);
        }
    },
    ELEVENTH(11, "bathroom") {
        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("detective")).getDrawable(), screen, 48, 16, new Item[]{Item.NOTEBOOK, Item.PHONE, Item.PHOTO, Item.FILES}) {
                public void onFail() {
                    screen.ui.addActor(Dialogue.readDialogue("dialogue/Detective1.json"));
                }

                public void onSuccess() {
                    Main.inventory.add(Item.FORTUNE_COOKIE);
                    screen.say("pleasure doing business");
                }

                public void open() {
                    screen.say("He may have been mysterious, but at least he was helpful");
                }
            };
        }

        public void previous() {
            Main.changeScreen(TENTH.screen);
        }
    },
    TWELTH(12, "school") {
        public void init() {
            new Puzzle(new Image(Main.entities.findRegion("schoolDoorKeypad")).getDrawable(), screen, 48, 16, Item.FORTUNE_COOKIE_CODE) {
                public void onFail() {
                    screen.say("it's locked with a keypad.\nI'll need some sort of code to get in");
                }

                public void open() {
                    Main.changeScreen(FINAL.screen);
                }
            };
        }

        public void previous() {
            Main.changeScreen(TENTH.screen);
        }
    },
    FINAL(12, "school") { //placeholder, obviously

        public void init() {
            screen.stage.clear();
            screen.ui.clear();
            Table goodbye = new Table();
            goodbye.setFillParent(true);
            goodbye.bottom().add(new Label("Thanks for Playing!", Main.skin, "large")).padBottom(20);
            screen.ui.addActor(goodbye);
            screen.ui.addActor(Dialogue.readDialogue("dialogue/final1.json"));
        }

        public void previous() {
            // You won. You can't go back. muahaha
        }
    };

    public GameScreen screen;
    public int scene;

    Scene(int scene, String bg) {
        this.scene = scene - 1;
        screen = new GameScreen(this, new Image(Main.backgrounds.findRegion(bg + "Background")));
        init();
    }

    public static void updateInventory() {
        for (Scene scene : Scene.values()) {
            scene.screen.updateInventory();
        }
    }

    public abstract void init();

    public abstract void previous();
}
