/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */
package monproject1bangerac8381;

import java.sql.*;
import java.util.ArrayList;
import static monproject1bangerac8381.WebMail.UserNameFromEmail;

/**
 *
 * @author Chiraag Bangera
 */


// Global SQL Command Center.


public class DBHelper 
{
    private String _databaseURL;
    private String _userName;
    private String _password;
    
    private Connection connection = null;
    private ResultSet result = null;
    private Statement statement = null;

    public DBHelper(String databaseURL, String userName, String password)
    {
        _databaseURL = databaseURL;
        _userName = userName;
        _password = password;
    }
    
    public boolean ConnectSQL ()
    {
        
        try
        {
            if(WebMail.developmentBuild)
            {
                WebMail.SOP("Attempting Database Connection...",true);
            }
            connection = DriverManager.getConnection("jdbc:mysql://"+_databaseURL,_userName,_password);
            statement = connection.createStatement();
            // test to see if connection to db is sucessfull.
            if(!isOpen())
            { 
                if(WebMail.developmentBuild)
                {
                    WebMail.SOE("Database Connection... FAILED",true);
                }
                return false;
            }
            if(WebMail.developmentBuild)
            {
                WebMail.SOP("Connection Successful",true);   
            }
            return true;
        }
        catch (Exception e)
        {
            WebMail.SOE("Error While Connecting",true);
            e.printStackTrace();
        }       
        return false;
    }
    
    
    public boolean isOpen()
    {
        try
        {
            if(connection.isValid(0))
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            WebMail.SOE("Error While accessing Connection Info", true);
            e.printStackTrace();
        }
        return false;
    }
    
    
    public Account GetUserAccount(String userName, String password) 
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to get User Account",true);
                }
                result = statement.executeQuery("select * from users where userid = '"+userName+"'");
                if(result.next())
                {
                    if(userName.equals(result.getString("userid")) && password.equals(result.getString("password")))
                    {
                        if(WebMail.developmentBuild)
                        {
                            WebMail.SOP("Fetch Successful",true);
                        }
                        Account account = new Account(result.getString("userid"),
                                result.getString("name"),result.getString("email"),
                                result.getString("password"));  
                        return account;
                    }
                    if(WebMail.developmentBuild)
                    {
                        WebMail.SOP("Invalid Credentials Entered",true);
                    }
                    return null;
                }
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching User Accounts", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Fetch FAILED",true);
        }
        return null;
    }
   
    
    public boolean VerifyUserAccount(String userID)
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Verifying Records",true);
                }
                result = statement.executeQuery("select userid from users where userid = '"+userID+"'"); 
                if(result.next())
                {
                    if(WebMail.developmentBuild)
                    {
                        WebMail.SOP("Record Exists",true);
                    }
                    return true; 
                }
                return false;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Accessing Records", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Accessing Recoreds FAILED",true);
        }
        return false;
    }
    
    
    
    public boolean SaveUserAccount(Account account) 
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Save New User",true);
                }
                statement.executeUpdate("insert into users values ('"+account.getUserID()+"','"
                        +account.getName()+"','"+account.getEmail()+"','"+account.getPassword()+"')"); 
                 if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Save Successfull",true);
                }
                return true;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Saving User Account", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Save FAILED",true);
        }
        return false;
    }
    
    
    public String GenerateRandomID(String name)
    {
        String userID = "";
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Get Counter Data",true);
                }
                result = statement.executeQuery("select number from accountkeys");
                if(result.next())
                {
                    int value = result.getInt("number");
                    if(name.length() > 10)
                    {
                        name = name.substring(0,10);
                    }
                    userID = name+value++;
                    statement.executeUpdate("update accountkeys set number = "+value +" WHERE number="+(value-1));
                     if(WebMail.developmentBuild)
                    {
                        WebMail.SOP("Update  Counter Successfull",true);
                    }
                    return userID.toLowerCase();
                }
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Accessing Online DB", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOE("Query Failed",true);
        }
        return null;
    }
    
    
    public String NameForEmailID(String emailID)
    {
        String userID = WebMail.UserNameFromEmail(emailID);
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetching Name",true);
                }
                result = statement.executeQuery("select name from users where userid = '"+userID+"'"); 
                if(result.next())
                {
                    if(WebMail.developmentBuild)
                    {
                        WebMail.SOP("Name Found",true);
                    }
                    return result.getString("name"); 
                }
                return null;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured while Accessing Records", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Accessing Recoreds FAILED",true);
        }
        return null;
    }
    
    
    public void MarkEmailAsRead(Email email)
    {
       if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Mark Email as Read",true);
                }
                statement.executeUpdate("update emails set isnew=0 where id="+email.mailID);   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Marking Email", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Marking Email FAILED",true);
        } 
    }
    
    
    
    
    public ArrayList<Email> GetInbox(Account account)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Emails",true);
                }
                result = statement.executeQuery("select * from emails where tousername ='"+account.getUserID()+"'"); 
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetching Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getBoolean("isnew"),result.getString(("groupid")),result.getInt("id"),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"));
                    int flag = 0;
                    // i had missed to add this check hence the error was being shown ... 
                    if(email.groupID != null && email.groupID != "")
                    {
                        for(int i=0;i<emails.size();i++)
                        {
                            if(emails.get(i).groupID != null && emails.get(i).groupID != "")
                            {
                                if(emails.get(i).groupID.equalsIgnoreCase(email.groupID))
                                {
                                    flag = 1;
                                    break;
                                }
                            }
                        }
                    }
                    if(flag == 0)
                    {
                        emails.add(email);
                    }
                }
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Email(s)",true);
                }
            return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Emails", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Fetching Email's FAILED",true);
        }
        return null;
    }
    
    
    public ArrayList<Email> GetSent(Account account)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Sent Emails",true);
                }
                result = statement.executeQuery("select * from emails where fromusername ='"+account.getUserID()+"'"); 
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetch Sent Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getBoolean("isnew"),result.getString("groupid"),result.getInt("id"),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"));
                    emails.add(email);
                }
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Email(s)",true);
                } 
             return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Sent Emails", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Fetching Email's FAILED",true);
        }
        return null;
    }
    
    
    public void SendEmail(Email email)
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Send Email",true);
                }
                result = statement.executeQuery("select mailnumber from accountkeys");
                int emailID = 0;
                if(result.next())
                {
                    emailID = result.getInt("mailnumber");
                    emailID++;
                    statement.executeUpdate("update accountkeys set mailnumber = "+emailID +" where mailnumber = "+(emailID-1));
                }
                statement.executeUpdate("insert into emails values(1, NULL, "+emailID+",'"+email.from+
                        "','"+email.to+"','"+email.subject+"','"+email.message+"','"+
                        TimeKeeper.Now()+"')");   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Sending Email", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Sending Email FAILED",true);
        }
    }
    

    public String GenerateConversationID(Email email)
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Generate Convo ID",true);
                }
                String gID = "";
                if(UserNameFromEmail(email.from).compareTo(UserNameFromEmail(email.to)) < 0)
                {
                    gID = UserNameFromEmail(email.from)+UserNameFromEmail(email.to);
                }
                else
                {
                    gID = UserNameFromEmail(email.to)+UserNameFromEmail(email.from);
                }
                result = statement.executeQuery("select groupid from emails where groupid like '"+gID+"_%' order by groupid");
                if(result.next())
                {
                    gID = result.getString("groupid");
                    int newGID = Integer.parseInt(gID.substring(gID.indexOf('_')+1,gID.length())) + 1;
                    gID  = gID.replace(""+(newGID-1), ""+newGID);
                }
                else
                {
                    gID += "_0";
                }
                return gID;
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Creating Convo ID", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Creating Convo ID FAILED",true);
        }
        return null;
    }
    // fix reply module
    public void ReplyEmail(Email email)
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Reply Email",true);
                }
                result = statement.executeQuery("select mailnumber from accountkeys");
                int emailID = 0;
                if(result.next())
                {
                    emailID = result.getInt("mailnumber");
                    emailID++;
                    statement.executeUpdate("update accountkeys set mailnumber = "+emailID +" where mailnumber = "+(emailID-1));
                }
                statement.executeUpdate("update emails set subject = '"+email.subject+"',groupid ='"+email.groupID+"' "
                        + "where id = "+email.mailID);  
                statement.executeUpdate("update emails set subject = '"+email.subject+"' where groupid = '"+email.groupID+"'");
                statement.executeUpdate("insert into emails values(1,'"+email.groupID+"',"+emailID+",'"+email.from+
                        "','"+email.to+"','"+email.subject+"','"+email.message+"','"+TimeKeeper.Now()+"')");
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Sending Email", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Sending Email FAILED",true);
        }
    }
    
    
    public ArrayList<Email> GetConversation(Account account, Email conduitEmail)
    {
        ArrayList<Email> emails = new ArrayList<Email>();
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Fetch Conversation Emails",true);
                }
                result = statement.executeQuery("select * from emails where groupid ='"+conduitEmail.groupID+"'"); 
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetch Conversation Emails Successfull",true);
                }
                while(result.next())
                {
                    Email email = new Email(result.getBoolean("isnew"),result.getString("groupid"),result.getInt("id"),
                            result.getString("fromusername")+"@webmail.com",result.getString("tousername")+"@webmail.com",
                            result.getString("subject"),result.getString("message"),
                            result.getString("timestamp"));
                    emails.add(email);
                }
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Fetched "+emails.size()+" Conversation Email(s)",true);
                } 
             return emails;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured While Fetching Conversation Emails", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Fetching Conversation Email's FAILED",true);
        }
        return null;
    }
    
        
    public String[] getStringsFromQuery(String query)
    {
        if(isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Attempting to Query",true);
                }
                result = statement.executeQuery(query); 
                if(result.next())
                {
                    if(WebMail.developmentBuild)
                    {
                        WebMail.SOP("Query Successfull",true);
                    }
                    int count = 0;
                    ArrayList<String> results = new ArrayList<String>();
                    while(result.next())
                    {
                        results.add(result.getString(count++));
                    }
                    return results.toArray(new String[0]);
                }
             return null;   
            }
            catch(Exception e)
            {
                WebMail.SOE("Error Occured During Query", true);
                e.printStackTrace();
            }
        }
        if(WebMail.developmentBuild)
        {
            WebMail.SOP("Query FAILED",true);
        }
        return null;
    }
    
    
    public boolean Close()
    {
        if(this.isOpen())
        {
            try
            {
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Closing Connection",true);
                }
                connection.close();
                statement.close();
                if(result != null)
                {
                    result.close();
                }
                if(WebMail.developmentBuild)
                {
                    WebMail.SOP("Connection Closed",true);
                }
                return true;
            }
            catch(Exception e)
            {
                WebMail.SOP("Error While Closing Connection", true);
                e.printStackTrace();
            }
        }
        return false;
    }
        
    
}
