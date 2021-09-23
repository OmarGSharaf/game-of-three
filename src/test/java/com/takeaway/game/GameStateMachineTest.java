//package com.takeaway.game;
//
//import com.takeaway.game.configuration.StateMachineConfig;
//import com.takeaway.game.type.GameEvents;
//import com.takeaway.game.type.GameStates;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.assertEquals;
//
//@SpringBootTest(classes = GameEngine.class)
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = StateMachineConfig.class)
//public class GameStateMachineTest {
//
//    @Autowired
//    private StateMachine<GameStates, GameEvents> stateMachine;
//
//    @Before
//    public void setUp() {
//        stateMachine.start();
//    }
//
//    @Test
//    public void givenStateMachine_whenStart_thenInitState() {
//        assertEquals(GameStates.INIT, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenInitState_whenConfigure_thenConfigureState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        assertEquals(GameStates.CONFIGURING, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenConfigureState_whenConnect_thenConnectingState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        stateMachine.sendEvent(GameEvents.CONNECT);
//        assertEquals(GameStates.CONNECTING, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenConnectingState_whenPlay_thenPlayingState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        stateMachine.sendEvent(GameEvents.CONNECT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        assertEquals(GameStates.PLAYING, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenPlayingState_whenWait_thenWaitingState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        stateMachine.sendEvent(GameEvents.CONNECT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        stateMachine.sendEvent(GameEvents.WAIT);
//        assertEquals(GameStates.WAITING, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenWaitingState_whenPlay_thenPlayingState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        stateMachine.sendEvent(GameEvents.CONNECT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        stateMachine.sendEvent(GameEvents.WAIT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        assertEquals(GameStates.PLAYING, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenPlayingState_whenAnnounceWinner_thenOverState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        stateMachine.sendEvent(GameEvents.CONNECT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        stateMachine.sendEvent(GameEvents.WAIT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        stateMachine.sendEvent(GameEvents.ANNOUNCE_WINNER);
//        assertEquals(GameStates.OVER, stateMachine.getState().getId());
//    }
//
//    @Test
//    public void givenOverState_whenPlayAgain_thenConfiguringState() {
//        stateMachine.sendEvent(GameEvents.CONFIGURE);
//        stateMachine.sendEvent(GameEvents.CONNECT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        stateMachine.sendEvent(GameEvents.WAIT);
//        stateMachine.sendEvent(GameEvents.PLAY);
//        stateMachine.sendEvent(GameEvents.ANNOUNCE_WINNER);
//        stateMachine.sendEvent(GameEvents.PLAY_AGAIN);
//        assertEquals(GameStates.CONFIGURING, stateMachine.getState().getId());
//    }
//
//    @After
//    public void tearDown() {
//        stateMachine.stop();
//    }
//}