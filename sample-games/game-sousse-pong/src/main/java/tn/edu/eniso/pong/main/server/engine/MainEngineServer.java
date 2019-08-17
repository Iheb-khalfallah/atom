/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tn.edu.eniso.pong.main.server.engine;

import net.vpc.gaming.atom.engine.SpriteTask;
import net.vpc.gaming.atom.engine.tasks.MoveSpriteTask;
import net.vpc.gaming.atom.model.MovementStyles;
import net.vpc.gaming.atom.model.Orientation;
import tn.edu.eniso.pong.hello.business.WelcomeEngine;
import tn.edu.eniso.pong.hello.model.WelcomeModel;
import tn.edu.eniso.pong.main.server.dal.DALServer;
import tn.edu.eniso.pong.main.server.dal.DALServerListener;
import tn.edu.eniso.pong.main.server.engine.collision.BallCollisionManager;
import tn.edu.eniso.pong.main.server.engine.tasks.BallFollowPaddleSpriteTask;
import tn.edu.eniso.pong.main.shared.dal.DALFactory;
import tn.edu.eniso.pong.main.shared.engine.AbstractMainEngine;
import tn.edu.eniso.pong.main.shared.model.AppPhase;
import tn.edu.eniso.pong.main.shared.model.Ball;
import tn.edu.eniso.pong.main.shared.model.MainModel;
import tn.edu.eniso.pong.main.shared.model.Paddle;

/**
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class MainEngineServer extends AbstractMainEngine implements DALServerListener {

    private DALServer dal;

    public MainEngineServer() {
        super();
    }

    @Override
    public void clientConnected() {
        MainModel m = getModel();
        m.setPhase(AppPhase.GAMING);
    }

    private WelcomeModel getHelloModel() {
        return getGameEngine().getSceneEngine(WelcomeEngine.class).getModel();
    }

    //    @Override
//    public PlayScreenModel getModel() {
//        return (PlayScreenModel) super.getModel();
//    }
    @Override
    public void sceneActivating() {
        MainModel m = getModel();
        m.setRole(getHelloModel().getRole());
        m.setPhase(AppPhase.WAITING);
        dal = DALFactory.createServer(getHelloModel().getTransport());

        Ball ball = findSprite(Ball.class);
        ball.setDirection(Orientation.NORTH_EAST);
        setSpriteCollisionManager(ball, new BallCollisionManager());
        setSpriteTask(ball, new BallFollowPaddleSpriteTask(1));
        ball.setMovementStyle(MovementStyles.STOPPED);

        for (Paddle paddle : findSprites(Paddle.class)) {
            setSpriteTask(paddle, null);
            paddle.setMovementStyle(MovementStyles.STOPPED);
        }
        dal.start("localhost", 1050, this);
    }

    @Override
    public void updateModel() {
        MainModel m = getModel();
        if (findSprites(Paddle.class).size() < 2) {
            m.setPhase(AppPhase.GAMEOVER);
        }
    }

    @Override
    protected void modelUpdated() {
        MainModel m = getModel();
        switch (m.getPhase()) {
            case WAITING: {
                //do nothing
                break;
            }
            case GAMING: {
                //do nothing
                dal.sendModelChanged(this);
                break;
            }
            case GAMEOVER: {
                //do nothing
                dal.sendModelChanged(this);
                break;
            }
        }
    }


    public void move(int playerId, Orientation direction) {
        MainModel m = getModel();
        if (m.getPhase() == AppPhase.GAMING) {
            Paddle paddle = findSpriteByPlayer(Paddle.class, playerId);
            paddle.setDirection(direction);
            SpriteTask task = getSpriteTask(paddle);
            if (task == null || !(task instanceof MoveSpriteTask)) {
                setSpriteTask(paddle, new MoveSpriteTask());
                paddle.setMovementStyle(MovementStyles.MOVING_SLOW);
            }
        }
    }

    public void moveLeft(int playerId) {
        move(playerId, Orientation.WEST);
    }

    public void moveRight(int playerId) {
        move(playerId, Orientation.EAST);
    }

    public void relaseBall(int playerId) {
        MainModel m = getModel();
        if (m.getPhase() == AppPhase.GAMING) {
            Ball ball = findSprite(Ball.class);
            //ball is null if game over
            if (ball != null) {
                SpriteTask task = getSpriteTask(ball);
                if (task instanceof BallFollowPaddleSpriteTask) {
                    BallFollowPaddleSpriteTask followBarTask = (BallFollowPaddleSpriteTask) task;
                    if (followBarTask.getPlayer() == playerId) {
                        setSpriteTask(ball, new MoveSpriteTask());
                        ball.setMovementStyle(MovementStyles.MOVING_SLOW);
                    }
                }
            }
        }
    }

    @Override
    public void recieveKeyLeft() {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                //left pour le joueur 2 == right pour le joueur 1
                moveRight(2);
            }
        });
    }

    @Override
    public void recieveKeyRight() {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                moveLeft(2);
            }
        });
    }

    @Override
    public void recieveKeySpace() {
        invokeLater(new Runnable() {

            @Override
            public void run() {
                relaseBall(2);
            }
        });
    }
}
