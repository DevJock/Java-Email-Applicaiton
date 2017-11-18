/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */
package monproject1bangerac8381;

/**
 *
 * @author Chiraag Bangera
 */
public class Email 
{
    public int mailID;
    public String groupID;
    public String from;
    public String to;
    public String subject;
    public String message;
    public String timeStamp;
    public boolean isNew;
    
    
    public Email(boolean isNew,String groupID, int mailID, String from, String to, String subject, String message, String timeStamp)
    {
        this.groupID = groupID;
        this.isNew = isNew;
        this.mailID = mailID;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.timeStamp = timeStamp; 
    }
    
    
    public Email(String from, String to, String subject, String message)
    {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
    
    public Email(int mailID, String groupID,String from, String to, String subject, String message)
    {
        this.groupID = groupID;
        this.mailID = mailID;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
    
    public Email(int mailID, String groupID, String from, String to, String subject, String message,String timeStamp)
    {
        this.groupID = groupID;
        this.mailID = mailID;
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.timeStamp = timeStamp;
    }
    
    public String toString()
    {
        return mailID + ": "+" ( "+groupID+" ) "+from+" -> "+to+" [ "+subject+" ] @ "+timeStamp;
    }
    
    
}
