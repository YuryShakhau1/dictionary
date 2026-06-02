package by.shakhau.dictionary.service.bean;

public class WordInfo {

    private long wordId;
    private int prevStatus = -1;
    private int status;

    public long getWordId() {
        return wordId;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public int getPrevStatus() {
        return prevStatus;
    }

    public void setPrevStatus(int prevStatus) {
        this.prevStatus = prevStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
