package thepaperpilot.strange;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import thepaperpilot.strange.Entities.Cat;
import thepaperpilot.strange.Entities.Entity;
import thepaperpilot.strange.Entities.Explosion;
import thepaperpilot.strange.Screens.ChoicesScreen;
import thepaperpilot.strange.Screens.GameScreen;
import thepaperpilot.strange.Screens.MenuScreen;

public enum Scene {
    FIRST(1, "bathroom") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 10, 32) {
                public void onTouch() {
                    Main.changeScreen(new ChoicesScreen(1, "Where do you want to go?", new String[]{"Hang outside", "Study inside"}, new GameScreen[]{SECOND.screen, THIRD.screen}, screen));
                }
            };
        }

        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(SECOND.screen);
            else Main.changeScreen(THIRD.screen);
        }

        public void previous() {
            // lol
        }
    },
    SECOND(2, "outside") {
        public void init() {
            screen.stage.addActor(new Cat((int) screen.stage.getWidth() / 3, 10));
            screen.clock.setPosition(148, 55);
        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FIRST.screen);
        }
    },
    THIRD(3, "school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 48, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (!locked || Main.selected.contains(Item.KEYS)) {
                        next(-1);
                        locked = false;
                    } else screen.say("it's locked", 56, 60);
                }
            };
        }

        public void next(int direction) {
            Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, screen));
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FIRST.screen);
        }
    },
    FOURTH(4, "outside") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
            screen.clock.setPosition(148, 55);
        }

        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(FIFTH.screen);
            else Main.changeScreen(SIXTH.screen);
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(new ChoicesScreen(1, "Why were you in the bathroom?", new String[]{"Tell the Truth", "Hide the Truth"}, FOURTH.screen, THIRD.screen));
        }
    },
    FIFTH(5, "junkyard") {
        public void init() {

        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FIRST.screen);
        }
    },
    SIXTH(6, "school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 48, 16) {
                public boolean locked = true;

                public void onTouch() {
                    if (locked && Main.selected.contains(Item.MAKE_SHIFT_BOMB)) {
                        Main.manager.get("explosion.wav", Sound.class).play();
                        setDrawable(new Image(Main.manager.get("breakableDoorPuzzle.png", Texture.class)).getDrawable());
                        screen.stage.addActor(new Explosion(((int) getX()), ((int) getY()) + 8));
                        locked = false;
                    } else if (!locked) {
                        Main.changeScreen(OFFICE.screen);
                    } else screen.say("it's locked", 56, 60);
                }
            };
            new Entity(new Image(Main.manager.get("schoolDoor.png", Texture.class)).getDrawable(), screen, 192, 16) {
                public void onTouch() {
                    next(-1);
                }
            };
        }

        public void next(int direction) {
            Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, screen));
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(FOURTH.screen);
        }
    },
    OFFICE(6) { // sub-level of 6
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(SIXTH.screen);
        }
    },
    SEVENTH(7, "junkyard") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }
        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(EIGTH.screen);
            else Main.changeScreen(NINTH.screen);
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(new ChoicesScreen(2, "Who is at fault?", new String[]{"Blame David", "Blame Nathan", "Blame Jefferson"}, SEVENTH.screen, SIXTH.screen));
        }
    },
    EIGTH(8) {
        public void init() {

        }

        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(SEVENTH.screen);
        }
    },
    NINTH(9, "school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, screen));
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(SEVENTH.screen);
        }
    },
    TENTH(10) {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }

        public void next(int direction) {
            if(direction == 0)
                Main.changeScreen(ELEVENTH.screen);
            else Main.changeScreen(TWELTH.screen);
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(new ChoicesScreen(3, "Warn Victoria her life is in danger?", new String[]{"Warn", "Don't Warn"}, TENTH.screen, NINTH.screen));
        }
    },
    ELEVENTH(11, "bathroom") {
        public void init() {

        }
        public void next(int direction) {
            // dead end :(
        }

        public void previous() {
            Main.changeScreen(TENTH.screen);
        }
    },
    TWELTH(12, "school") {
        public void init() {
            screen.clock.addListener(new ClickListener(Input.Buttons.LEFT) {
                public void clicked(InputEvent event, float x, float y) {
                    next(1);
                }
            });
        }
        public void next(int direction) {
            Main.changeScreen(FINAL.screen);
        }

        public void previous() {
            Main.manager.get("rewind.wav", Sound.class).play();
            Main.changeScreen(TENTH.screen);
        }
    },
    FINAL(12) {
        public void init() {
            // show them one of 12 end cutscenes based on their choices
        }
        public void next(int direction) {
            Main.changeScreen(new MenuScreen());
        }

        public void previous() {
            // You won. You can't go back. muahaha
        }
    };

    public GameScreen screen;
    public int scene;

    Scene(int scene) {
        this.scene = scene - 1; //just to make it index properly (start at 0)
        screen = new GameScreen(this, new Image(Main.manager.get("schoolBackground.png", Texture.class)));
        init();
    }

    Scene(int scene, String bg) {
        this.scene = scene - 1;
        screen = new GameScreen(this, new Image(Main.manager.get("" + bg + "Background.png", Texture.class)));
        init();
    }

    public static void updateInventory() {
        for (Scene scene : Scene.values()) {
            scene.screen.updateInventory();
        }
    }

    public abstract void init();

    public abstract void next(int direction);

    public abstract void previous();
}
