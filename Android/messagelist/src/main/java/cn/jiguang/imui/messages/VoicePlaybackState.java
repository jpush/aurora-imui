package cn.jiguang.imui.messages;

final class VoicePlaybackState {
    private String messageId;
    private boolean playing;

    boolean isPlaying(String candidateMessageId) {
        return playing && messageId != null && messageId.equals(candidateMessageId);
    }

    boolean isCurrent(String candidateMessageId) {
        return messageId != null && messageId.equals(candidateMessageId);
    }

    String getMessageId() {
        return messageId;
    }

    void play(String nextMessageId) {
        messageId = nextMessageId;
        playing = true;
    }

    void pause(String candidateMessageId) {
        if (isCurrent(candidateMessageId)) {
            playing = false;
        }
    }

    void stop(String candidateMessageId) {
        if (isCurrent(candidateMessageId)) {
            messageId = null;
            playing = false;
        }
    }
}
