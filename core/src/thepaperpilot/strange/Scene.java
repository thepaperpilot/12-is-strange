package thepaperpilot.strange;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import thepaperpilot.strange.Entities.AnimatedEntity;
import thepaperpilot.strange.Entities.Bird;
import thepaperpilot.strange.Entities.Entity;
import thepaperpilot.strange.Entities.Explosion;
import thepaperpilot.strange.Screens.ChoicesScreen;
import thepaperpilot.strange.Screens.FinalScreen;
import thepaperpilot.strange.Screens.GameScreen;
import thepaperpilot.strange.Screens.JunkyardCutscene;

public enum Scene {
    FIRST(1, "bathroom") {
        public void init() {
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 10, 32) {
                public void onTouch() {
                    Main.changeScreen(new ChoicesScreen(-1, "Where do you want to go?", new String[]{"Hang outside", "Study inside"}, new GameScreen[]{SECOND.screen, THIRD.screen}, screen));
                }
            };
            new Entity(new Image(Main.manager.get("fireAlarmPuzzle.png", Texture.class)).getDrawable(), screen, 210, 60) {
                public void onTouch() {
                    if (Main.selected.contains(Item.HAMMER)) {
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.inventory.remove(Item.HAMMER);
                        Main.selected.remove(Item.HAMMER);
                        Main.inventory.add(Item.GUN);
                        setDrawable(new Image(Main.manager.get("fireAlarmPuzzleBroken.png", Texture.class)).getDrawable());
                        updateInventory();
                    } else screen.say("I need to find something to break this with");
                }
            };
        }

        public void previous() {
            // lol
        }
    },
    SECOND(2, "outside") {
        public void init() {
            new AnimatedEntity(screen, 81, 10, Main.manager.get("catIdle.png", Texture.class), 14, 1 / 6f) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.CAT_FOOD)) {
                        TextureRegion[][] tmp = TextureRegion.split(Main.manager.get("catEating.png", Texture.class), (int) getWidth(), (int) getHeight());
                        TextureRegion[] frames = tmp[0];
                        animation = new Animation(animation.getFrameDuration(), frames);
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.CAT_FOOD);
                        Main.inventory.remove(Item.CAT_FOOD);
                        updateInventory();
                        new Entity(new Image(Main.manager.get("sugarWorld.png", Texture.class)).getDrawable(), FOURTH.screen, (int) screen.stage.getWidth() / 3, 10) {
                            public void onTouch() {
                                Main.manager.get("pickup.wav", Sound.class).play();
                                Main.inventory.add(Item.SUGAR);
                                Scene.updateInventory();
                                remove();
                            }
                        };
                        GameScreen.bird.alive = true;
                    }
                }
            };
            screen.clock.setPosition(148, 55);
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FIRST.screen);
        }
    },
    THIRD(3, "school") {
        public void init() {
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 48, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (!locked || Main.selected.contains(Item.KEYS)) {
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.KEYS);
                        Main.inventory.remove(Item.KEYS);
                        updateInventory();
                        Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, screen));
                    } else screen.say("it's locked");
                }
            };
            new Entity(new Image(Main.manager.get("AlyssaSoapPuzzle.png", Texture.class)).getDrawable(), screen, 192, 10) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.SOAP)) {
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.SOAP);
                        Main.inventory.remove(Item.SOAP);
                        Main.inventory.add(Item.KEYS);
                        updateInventory();
                    } else if (locked)
                        screen.say("She'll give me the keys to the door in exchange for some soap\nI know its weird, just consider it a tutorial");
                    else screen.say("I already gave her the soap. What more do you want?");
                }
            };
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FIRST.screen);
        }
    },
    FOURTH(4, "outside") {
        public void init() {
            new Entity(new Image(Main.manager.get("tirestack.png", Texture.class)).getDrawable(), screen, 230, 13) {
                public void onTouch() {
                    Main.changeScreen(FIFTH.screen);
                }
            };
            new Entity(new Image(Main.manager.get("stairs.png", Texture.class)).getDrawable(), screen, 0, 7) {
                public void onTouch() {
                    Main.changeScreen(SIXTH.screen);
                }
            };
            new AnimatedEntity(screen, 160, 13, Main.manager.get("dogIdle.png", Texture.class), 14, 1 / 6f) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.DOG_BONE)) {
                        TextureRegion[][] tmp = TextureRegion.split(Main.manager.get("dogHappy.png", Texture.class), (int) getWidth(), (int) getHeight());
                        TextureRegion[] frames = tmp[0];
                        animation = new Animation(animation.getFrameDuration(), frames);
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.DOG_BONE);
                        Main.inventory.remove(Item.DOG_BONE);
                        updateInventory();
                        screen.obstacles.remove(0); // there's only one, so this works
                    } else if (locked) screen.say("This dog won't let me through. It looks hungry");
                    else screen.say("The dog is much happier now that it has a bone");
                }
            };
            screen.clock.setPosition(148, 55);
            screen.obstacles.add(new Rectangle(160, 0, 16, screen.stage.getHeight()));
            GameScreen.bird = new Bird(screen, 73, 10);
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, THIRD.screen));
        }
    },
    FIFTH(5, "junkyard") {
        boolean carBlock = true;
        boolean ductTape = true;
        public void init() {
            final Entity usb = new Entity(new Image(Main.manager.get("usbWorld.png", Texture.class)).getDrawable(), screen, 80, 16) {
                public void onTouch() {
                    if (!carBlock) {
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.inventory.add(Item.USB);
                        Scene.updateInventory();
                        remove();
                    } else screen.say("it's blocked by this car");
                }
            };
            final Entity ductTapeEntity = new Entity(new Image(Main.manager.get("ductTapeWorld.png", Texture.class)).getDrawable(), screen, 40, 16) {
                public void onTouch() {
                    Main.manager.get("pickup.wav", Sound.class).play();
                    Main.inventory.add(Item.DUCT_TAPE);
                    Scene.updateInventory();
                    remove();
                    ductTape = false;
                }
            };
            new Entity(new Image(Main.manager.get("carPuzzle.png", Texture.class)).getDrawable(), screen, 80, 16) {
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
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FOURTH.screen);
        }
    },
    SIXTH(6, "school") {
        boolean doorBlocked = true;

        public void init() {
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 48, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.MAKE_SHIFT_BOMB)) {
                        Main.manager.get("explosion.wav", Sound.class).play();
                        setDrawable(new Image(Main.manager.get("breakableDoorPuzzle.png", Texture.class)).getDrawable());
                        screen.stage.addActor(new Explosion(((int) getX()), ((int) getY()) + 8));
                        locked = false;
                        Main.selected.remove(Item.MAKE_SHIFT_BOMB);
                        Main.inventory.remove(Item.MAKE_SHIFT_BOMB);
                        updateInventory();
                    } else if (!locked) {
                        Main.changeScreen(OFFICE.screen);
                    } else screen.say("it's locked");
                }
            };
            new Entity(new Image(Main.manager.get("crowdCameraPuzzle.png", Texture.class)).getDrawable(), screen, 180, 14) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.CAMERA)) {
                        doorBlocked = false;
                        locked = false;
                        Main.selected.remove(Item.CAMERA);
                        Main.inventory.remove(Item.CAMERA);
                        Main.inventory.add(Item.PHOTO);
                        updateInventory();
                        addAction(Actions.sequence(Actions.moveBy(100, 0, 4), Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                remove();
                            }
                        })));
                    } else if (locked) screen.say("This crowd of people would make a great shot");
                }
            };
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 192, 16) {
                public void onTouch() {
                    if (!doorBlocked)
                        Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, screen));
                }
            };
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FOURTH.screen);
        }
    },
    OFFICE(6, "office") { // sub-level of 6
        public void init() {
            new Entity(new Image(Main.manager.get("breakableDoorPuzzle.png", Texture.class)).getDrawable(), screen, 68, 16) {
                public void onTouch() {
                    Main.changeScreen(SIXTH.screen);
                }
            };
            screen.clock.setPosition(168, 60);
            screen.obstacles.add(new Rectangle(0, 0, 67, screen.stage.getHeight()));
            screen.obstacles.add(new Rectangle(screen.stage.getWidth() - 68, 0, 67, screen.stage.getHeight()));
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FOURTH.screen);
        }
    },
    SEVENTH(7, "junkyard") {
        public void init() {
            new AnimatedEntity(screen, 230, 13, Main.manager.get("balloon.png", Texture.class), 14, .1f) {
                public void onTouch() {
                    Main.changeScreen(new ChoicesScreen(-1, "Are you a nerd?", new String[]{"Go party!", "Go study?"}, new GameScreen[]{EIGTH.screen, NINTH.screen}, screen));
                }
            };
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, SIXTH.screen));
        }
    },
    EIGTH(8, "vortex") {
        public void init() {
            new Entity(new Image(Main.manager.get("bouncer.png", Texture.class)).getDrawable(), screen, 192, 10) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.ALCOHOL)) {
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.ALCOHOL);
                        Main.inventory.remove(Item.ALCOHOL);
                        updateInventory();
                        screen.obstacles.remove(0);
                    } else if (locked)
                        screen.say("I'm not allowed in the VIP area. Maybe if I brought him some alcohol");
                    else screen.say("I bribed the bouncer with alcohol, and now he'll let me in the VIP area");
                }
            };
            screen.obstacles.add(new Rectangle(192, 0, 16, screen.stage.getHeight()));
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(SEVENTH.screen);
        }
    },
    NINTH(9, "school") {
        public void init() {
            new Entity(new Image(Main.manager.get("warrenPuzzle.png", Texture.class)).getDrawable(), screen, 192, 10) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.USB)) {
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.USB);
                        Main.inventory.remove(Item.USB);
                        updateInventory();
                        Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, screen));
                    } else if (locked) screen.say("Warren needs his flash drive. I think I left it in the junkyard");
                    else
                        Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, screen));
                }
            };
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(SEVENTH.screen);
        }
    },
    TENTH(10, "dorm") {
        public void init() {
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 68, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.FIRE_EXTINGUISHER)) {
                        Main.manager.get("explosion.wav", Sound.class).play();
                        setDrawable(new Image(Main.manager.get("breakableDoorPuzzle.png", Texture.class)).getDrawable());
                        screen.stage.addActor(new Explosion(((int) getX()), ((int) getY()) + 8));
                        locked = false;
                        Main.inventory.remove(Item.FIRE_EXTINGUISHER);
                        Main.selected.remove(Item.FIRE_EXTINGUISHER);
                        updateInventory();
                    } else if (!locked) {
                        Main.changeScreen(new ChoicesScreen(-1, "Do you need to go?", new String[]{"Go to the bathroom", "Stay in class"}, new GameScreen[]{ELEVENTH.screen, TWELTH.screen}, screen));
                    } else screen.say("it's locked");
                }
            };
            screen.clock.setPosition(168, 60);
            screen.obstacles.add(new Rectangle(0, 0, 67, screen.stage.getHeight()));
            screen.obstacles.add(new Rectangle(screen.stage.getWidth() - 68, 0, 67, screen.stage.getHeight()));
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, NINTH.screen));
        }
    },
    ELEVENTH(11, "bathroom") {
        public void init() {
            new Entity(new Image(Main.manager.get("detective.png", Texture.class)).getDrawable(), screen, 48, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.NOTEBOOK) && Main.selected.contains(Item.PHONE) && Main.selected.contains(Item.PHOTO) && Main.selected.contains(Item.FILES)) {
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.NOTEBOOK);
                        Main.inventory.remove(Item.NOTEBOOK);
                        Main.selected.remove(Item.PHONE);
                        Main.inventory.remove(Item.PHONE);
                        Main.selected.remove(Item.PHOTO);
                        Main.inventory.remove(Item.PHOTO);
                        Main.selected.remove(Item.FILES);
                        Main.inventory.remove(Item.FIRE_EXTINGUISHER);
                        updateInventory();
                        Main.inventory.add(Item.FORTUNE_COOKIE);
                    } else if (locked)
                        screen.say("This mysterious guy says he can give me the keypad code\nin exchange for a notebook, phone, a photo, and some files");
                    else screen.say("He may have been mysterious, but at least he was helpful");
                }
            };
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(TENTH.screen);
        }
    },
    TWELTH(12, "school") {
        public void init() {
            new Entity(new Image(Main.manager.get("schoolDoorKeypad.png", Texture.class)).getDrawable(), screen, 48, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (!locked || Main.selected.contains(Item.FORTUNE_COOKIE_CODE)) {
                        locked = false;
                        Main.manager.get("pickup.wav", Sound.class).play();
                        Main.selected.remove(Item.FORTUNE_COOKIE_CODE);
                        Main.inventory.remove(Item.FORTUNE_COOKIE_CODE);
                        updateInventory();
                        Main.changeScreen(FINAL.screen);
                    } else screen.say("it's locked with a keypad.\nI'll need some sort of code to get in");
                }
            };
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(TENTH.screen);
        }
    },
    FINAL(12, "school") { //placeholder, obviously
        public void init() {
            // TODO Only time to copy paste one ending. Oh the humanity!
            String[] dialogs = new String[]{
                    "Maxine and Chloe decide to warn only the people they care about. ",
                    "Chloe:\n\"Max, did you warn your family and close friends?\"",
                    "Maxine:\n\"Yes, and did you?\"",
                    "Chloe:\n\"Just my mom and even step douche. Now let's watch this hellhole go to the ground.\"",
                    "The tornado rips through Arcadia Bay. Max and Chloe hold hands and the tornado destroys the town.",
                    "Chloe:\n\"Later a-holes. Won't be missing you.\""
            };
            Main.changeScreen(new FinalScreen(new Image(Main.manager.get("butterfly.png", Texture.class)), dialogs));
        }

        public void previous() {
            // You won. You can't go back. muahaha
        }
    };

    public GameScreen screen;
    public int scene;

    Scene(int scene, String bg) {
        this.scene = scene - 1;
        screen = new GameScreen(this, new Image(Main.manager.get(bg + "Background.png", Texture.class)));
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
