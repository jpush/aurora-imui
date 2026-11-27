package cn.jiguang.imui.messages;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VoicePlaybackStateTest {
    @Test
    public void recycledHolderRendersFromItsBoundMessageId() {
        VoicePlaybackState state = new VoicePlaybackState();

        state.play("A");
        String boundMessageId = "A";
        assertTrue(state.isPlaying(boundMessageId));

        boundMessageId = "B";
        assertFalse(state.isPlaying(boundMessageId));

        boundMessageId = "A";
        assertTrue(state.isPlaying(boundMessageId));

        state.play("B");
        assertFalse(state.isPlaying(boundMessageId));
        boundMessageId = "B";
        assertTrue(state.isPlaying(boundMessageId));
    }

    @Test
    public void staleCompletionCannotStopAnotherMessage() {
        VoicePlaybackState state = new VoicePlaybackState();

        state.play("A");
        state.play("B");
        state.stop("A");

        assertTrue(state.isPlaying("B"));
        state.stop("B");
        assertFalse(state.isPlaying("B"));
    }

    @Test
    public void pauseKeepsOnlyThatMessageResumable() {
        VoicePlaybackState state = new VoicePlaybackState();

        state.play("A");
        state.pause("A");

        assertFalse(state.isPlaying("A"));
        assertTrue(state.isCurrent("A"));
        state.play("B");
        assertFalse(state.isCurrent("A"));
        assertTrue(state.isPlaying("B"));
    }
}
