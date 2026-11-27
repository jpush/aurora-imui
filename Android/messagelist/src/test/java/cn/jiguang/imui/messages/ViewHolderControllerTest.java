package cn.jiguang.imui.messages;

import org.junit.Test;

import static org.junit.Assert.assertSame;

@SuppressWarnings("deprecation")
public class ViewHolderControllerTest {

    @Test
    public void legacyReplayEntryPointRemainsCallableWithoutPlayback() {
        assertSame(ViewHolderController.getInstance(), ViewHolderController.getInstance());
        ViewHolderController.getInstance().replayVoice();
    }
}
