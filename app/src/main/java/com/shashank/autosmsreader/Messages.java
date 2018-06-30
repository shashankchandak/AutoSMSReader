package com.shashank.autosmsreader;

public class Messages {

    public String senderName;
    public String messageBody;
    public String date;

    public Messages(){

    }

    public Messages(String senderName, String messageBody, String date) {
        this.senderName = senderName;
        this.messageBody = messageBody;
        this.date=date;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
