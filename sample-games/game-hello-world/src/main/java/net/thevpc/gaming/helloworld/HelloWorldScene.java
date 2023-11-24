package net.thevpc.gaming.helloworld;

import net.thevpc.gaming.atom.annotations.*;
import net.thevpc.gaming.atom.debug.AdjustViewController;
import net.thevpc.gaming.atom.engine.SceneEngine;
import net.thevpc.gaming.atom.engine.SpriteFilter;
import net.thevpc.gaming.atom.model.Orientation;
import net.thevpc.gaming.atom.model.Point;
import net.thevpc.gaming.atom.model.Sprite;
import net.thevpc.gaming.atom.presentation.*;
import net.thevpc.gaming.atom.presentation.components.SLabel;
import net.thevpc.gaming.atom.presentation.layers.ImageBoardLayer;
import net.thevpc.gaming.atom.presentation.layers.Layers;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vpc on 9/23/16.
 */
@AtomScene(
        id = "hello",
        title = "Hello World",
        tileWidth = 100,
        tileHeight = 100

)
@AtomSceneEngine(
        id="hello",
        columns = 20,
        rows = 10,
        fps = 25
)

public class HelloWorldScene{

    @Inject
    private Scene scene;
    @Inject
    private SceneEngine sceneEngine;
    
    private Sprite ballSprite;
    
    
    
    @OnSceneStarted
    public void init() {
        scene.addLayer(Layers.fillBoardGradient(Color.yellow,Color.CYAN, Orientation.NORTH));
        scene.addLayer(new ImageBoardLayer("/image.jpg"));
        scene.addLayer(Layers.debug());
        //scene.addLayer(Layers.fillScreen(Color.red));
        scene.addController(new SpriteController(SpriteFilter.byName("Ball1")).setArrowKeysLayout());
        scene.addController(new SpriteController(SpriteFilter.byName("Ball2")).setESDFLayout());
        scene.addController(new AdjustViewController());
        scene.addComponent(
                new SLabel("Click CTRL-D to switch debug mode, use Arrows to move the ball")
                .setLocation(Point.ratio(0.2f,0.5f))
        );

        scene.addComponent(
                new SLabel("vies","vies : 3")
                        .setLocation(Point.ratio(0.2f,0.55f))
        );
        scene.setSpriteView(SpriteFilter.byName("Ball1"), new ImageSpriteView("/ball.png", 8, 4));
        scene.setSpriteView(SpriteFilter.byName("Ball2"), new ImageSpriteView("/ball.png", 8, 4));
//        scene.setSpriteView(SpriteFilter.byName("Ball2"), new DefaultSpriteView() {
//
//            @Override
//            public void draw(SpriteDrawingContext context) {
//                Graphics2D graphics = context.getGraphics();
//                context.getSpriteBounds().toRectangle();
//
//                    graphics.draw(context.getSpriteShape());
//            }
//        });
    }


    /*@OnNextFrame
    public void aChaqueTic() {
        Sprite ballSprite = sceneEngine.findSpriteByName("Ball1"); 
        ballSprite.setName("Vies restantes : " + ballSprite.getLife());
        System.out.println("Vies restantes : " + ballSprite.getLife());

    }*/
}