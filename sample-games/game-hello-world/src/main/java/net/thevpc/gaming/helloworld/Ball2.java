package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.AtomSprite;
import net.thevpc.gaming.atom.annotations.Inject;
import net.thevpc.gaming.atom.annotations.OnInit;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.collisiontasks.BounceSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.collisiontasks.StopSpriteCollisionTask;
import net.thevpc.gaming.atom.engine.maintasks.MotionSpriteMainTask;
import net.thevpc.gaming.atom.engine.maintasks.MoveSpriteMainTask;
import net.thevpc.gaming.atom.model.Sprite;

@AtomSprite(
        name = "Ball2",
        kind = "Ball",
        sceneEngine = "hello",
        x=3,
        y=3,
        direction = - Math.PI/4,
        speed = 0.4,
        mainTask = MoveSpriteMainTask.class,
        width = 2,
        height = 2,
        //   collisionTask = StopSpriteCollisionTask.class
        collisionTask = Ball2DefaultSpriteCollisionManager.class

)
public class Ball2 {
    @Inject
    Sprite sprite;
    @Inject
    SceneEngine sceneEngine;

    @OnInit
    private void init(){
        sprite.setLocation(9,2);
    }
}