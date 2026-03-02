package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.kkotlyarenko.domain.model.*;
import org.kkotlyarenko.domain.state.ArthurState;
import org.kkotlyarenko.domain.state.CrowdState;
import org.kkotlyarenko.domain.state.SpeakerState;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {

    @Nested
    @DisplayName("Arthur")
    class ArthurTests {

        private Arthur arthur;
        private Window window;

        @BeforeEach
        void setUp() {
            arthur = new Arthur();
            window = new Window(2);
        }

        @Test
        @DisplayName("Initial state is STANDING")
        void initialState() {
            assertEquals(ArthurState.STANDING, arthur.getState());
            assertNull(arthur.getTargetWindow());
        }

        @Test
        @DisplayName("Transition STANDING → SLIDING")
        void startSliding() {
            arthur.startSliding(window);
            assertEquals(ArthurState.SLIDING, arthur.getState());
            assertEquals(window, arthur.getTargetWindow());
        }

        @Test
        @DisplayName("Transition SLIDING → ARRIVED")
        void arrive() {
            arthur.startSliding(window);
            arthur.arrive();
            assertEquals(ArthurState.ARRIVED, arthur.getState());
        }

        @Test
        @DisplayName("Transition ARRIVED → STANDING (land)")
        void land() {
            arthur.startSliding(window);
            arthur.arrive();
            arthur.land();
            assertEquals(ArthurState.STANDING, arthur.getState());
            assertNull(arthur.getTargetWindow());
        }

        @Test
        @DisplayName("Cannot start sliding outside STANDING")
        void cannotSlideFromSliding() {
            arthur.startSliding(window);
            assertThrows(IllegalStateException.class, () -> arthur.startSliding(window));
        }

        @Test
        @DisplayName("Cannot arrive outside SLIDING")
        void cannotArriveFromStanding() {
            assertThrows(IllegalStateException.class, () -> arthur.arrive());
        }

        @Test
        @DisplayName("Cannot slide to null window")
        void cannotSlideToNullWindow() {
            assertThrows(IllegalArgumentException.class, () -> arthur.startSliding(null));
        }

        @Test
        @DisplayName("Full cycle: STANDING → SLIDING → ARRIVED → STANDING")
        void fullCycle() {
            assertEquals(ArthurState.STANDING, arthur.getState());
            arthur.startSliding(window);
            assertEquals(ArthurState.SLIDING, arthur.getState());
            arthur.arrive();
            assertEquals(ArthurState.ARRIVED, arthur.getState());
            arthur.land();
            assertEquals(ArthurState.STANDING, arthur.getState());
        }
    }


    @Nested
    @DisplayName("Crowd")
    class CrowdTests {

        private Crowd crowd;

        @BeforeEach
        void setUp() {
            crowd = new Crowd();
        }

        @Test
        @DisplayName("Initial state is QUIET")
        void initialState() {
            assertEquals(CrowdState.QUIET, crowd.getState());
        }

        @Test
        @DisplayName("Transition QUIET → CHEERING")
        void cheer() {
            crowd.cheer();
            assertEquals(CrowdState.CHEERING, crowd.getState());
        }

        @Test
        @DisplayName("Transition CHEERING → QUIET")
        void quietDown() {
            crowd.cheer();
            crowd.quiet();
            assertEquals(CrowdState.QUIET, crowd.getState());
        }

        @Test
        @DisplayName("Repeated cheer remains CHEERING")
        void cheerTwice() {
            crowd.cheer();
            crowd.cheer();
            assertEquals(CrowdState.CHEERING, crowd.getState());
        }
    }


    @Nested
    @DisplayName("Speaker")
    class SpeakerTests {

        private Speaker speaker;
        private Platform platform;

        @BeforeEach
        void setUp() {
            Building building = new Building();
            platform = new Platform(building);
            speaker = new Speaker("Оратор");
        }

        @Test
        @DisplayName("Initial state is IDLE")
        void initialState() {
            assertEquals(SpeakerState.IDLE, speaker.getState());
            assertEquals("Оратор", speaker.getName());
            assertNull(speaker.getPlatform());
        }

        @Test
        @DisplayName("Start speaking: IDLE → SPEAKING")
        void startSpeaking() {
            speaker.startSpeaking(platform);
            assertEquals(SpeakerState.SPEAKING, speaker.getState());
            assertEquals(platform, speaker.getPlatform());
            assertEquals(speaker, platform.getSpeaker());
        }

        @Test
        @DisplayName("Stop speaking: SPEAKING → IDLE")
        void stopSpeaking() {
            speaker.startSpeaking(platform);
            speaker.stopSpeaking();
            assertEquals(SpeakerState.IDLE, speaker.getState());
        }

        @Test
        @DisplayName("Cannot speak without platform (null)")
        void cannotSpeakWithoutPlatform() {
            assertThrows(IllegalArgumentException.class, () -> speaker.startSpeaking(null));
        }
    }


    @Nested
    @DisplayName("Window")
    class WindowTests {

        @Test
        @DisplayName("Window creation with valid floor")
        void validWindow() {
            Window w = new Window(2);
            assertEquals(2, w.getFloor());
        }

        @Test
        @DisplayName("Cannot create window with floor <= 0")
        void invalidFloor() {
            assertThrows(IllegalArgumentException.class, () -> new Window(0));
            assertThrows(IllegalArgumentException.class, () -> new Window(-1));
        }
    }


    @Nested
    @DisplayName("Building")
    class BuildingTests {

        @Test
        @DisplayName("Add and retrieve windows")
        void addAndGetWindows() {
            Building b = new Building();
            Window w1 = new Window(1);
            Window w2 = new Window(2);
            Window w3 = new Window(2);
            b.addWindow(w1);
            b.addWindow(w2);
            b.addWindow(w3);
            assertEquals(3, b.getWindows().size());
        }

        @Test
        @DisplayName("Filter windows by floor")
        void getWindowsOnFloor() {
            Building b = new Building();
            b.addWindow(new Window(1));
            b.addWindow(new Window(2));
            b.addWindow(new Window(2));
            b.addWindow(new Window(3));

            assertEquals(2, b.getWindowsOnFloor(2).size());
            assertEquals(1, b.getWindowsOnFloor(1).size());
            assertEquals(0, b.getWindowsOnFloor(5).size());
        }

        @Test
        @DisplayName("Attach platform to building")
        void platformAttachment() {
            Building b = new Building();
            assertNull(b.getPlatform());

            Platform p = new Platform(b);
            assertEquals(p, b.getPlatform());
        }
    }


    @Nested
    @DisplayName("Platform")
    class PlatformTests {

        @Test
        @DisplayName("Create platform for building")
        void creation() {
            Building b = new Building();
            Platform p = new Platform(b);
            assertEquals(b, p.getBuilding());
            assertFalse(p.hasSpeaker());
        }

        @Test
        @DisplayName("Cannot create platform with null building")
        void nullBuilding() {
            assertThrows(IllegalArgumentException.class, () -> new Platform(null));
        }

        @Test
        @DisplayName("Speaker on platform")
        void speakerOnPlatform() {
            Building b = new Building();
            Platform p = new Platform(b);
            Speaker s = new Speaker("Оратор");

            assertFalse(p.hasSpeaker());
            s.startSpeaking(p);
            assertTrue(p.hasSpeaker());
            assertEquals(s, p.getSpeaker());
        }
    }


    @Nested
    @DisplayName("Scene — full scenario")
    class SceneTests {

        private Arthur arthur;
        private Crowd crowd;
        private Building building;
        private Platform platform;
        private Speaker speaker;
        private Scene scene;
        private Window window;

        @BeforeEach
        void setUp() {
            arthur = new Arthur();
            crowd = new Crowd();
            building = new Building();
            building.addWindow(new Window(1));
            building.addWindow(new Window(2));
            building.addWindow(new Window(2));
            platform = new Platform(building);
            speaker = new Speaker("Зафод");
            scene = new Scene(arthur, crowd, building, platform, speaker);
        }

        @Test
        @DisplayName("Initial scene state")
        void initialState() {
            assertEquals(ArthurState.STANDING, scene.getArthur().getState());
            assertEquals(CrowdState.QUIET, scene.getCrowd().getState());
            assertEquals(SpeakerState.IDLE, scene.getSpeaker().getState());
            assertFalse(scene.isScenePlayed());
        }

        @Test
        @DisplayName("isScenePlayed(): Arthur is SLIDING but crowd is QUIET")
        void isScenePlayedArthurSlidingCrowdQuiet() {
            Window target = building.getWindowsOnFloor(2).get(0);
            arthur.startSliding(target);

            assertEquals(ArthurState.SLIDING, arthur.getState());
            assertEquals(CrowdState.QUIET, crowd.getState());
            assertFalse(scene.isScenePlayed());
        }

        @Test
        @DisplayName("isScenePlayed(): Arthur is SLIDING and crowd is CHEERING but speaker is IDLE")
        void isScenePlayedSpeakerIdle() {
            Window target = building.getWindowsOnFloor(2).get(0);
            arthur.startSliding(target);
            crowd.cheer();

            assertEquals(ArthurState.SLIDING, arthur.getState());
            assertEquals(CrowdState.CHEERING, crowd.getState());
            assertEquals(SpeakerState.IDLE, speaker.getState());
            assertFalse(scene.isScenePlayed());
        }

        @Test
        @DisplayName("Scene getters")
        void getters() {
            assertSame(arthur, scene.getArthur());
            assertSame(crowd, scene.getCrowd());
            assertSame(building, scene.getBuilding());
            assertSame(platform, scene.getPlatform());
            assertSame(speaker, scene.getSpeaker());
        }

        @Test
        @DisplayName("play() executes full text scenario")
        void playFullScenario() {
            scene.play();
            assertEquals(SpeakerState.SPEAKING, speaker.getState());
            assertEquals(platform, speaker.getPlatform());
            assertEquals(CrowdState.CHEERING, crowd.getState());
            assertEquals(ArthurState.SLIDING, arthur.getState());
            assertNotNull(arthur.getTargetWindow());
            assertEquals(2, arthur.getTargetWindow().getFloor());
            assertTrue(scene.isScenePlayed());
        }

        @Test
        @DisplayName("Arthur can arrive after play()")
        void arthurArrives() {
            scene.play();
            arthur.arrive();
            assertEquals(ArthurState.ARRIVED, arthur.getState());
        }

        @Test
        @DisplayName("Relation: platform belongs to building")
        void platformBuilding() {
            assertEquals(building, platform.getBuilding());
            assertEquals(platform, building.getPlatform());
        }

        @Test
        @DisplayName("Relation: speaker is on platform after play()")
        void speakerOnPlatformAfterPlay() {
            scene.play();
            assertTrue(platform.hasSpeaker());
            assertEquals(speaker, platform.getSpeaker());
        }
    }
}
