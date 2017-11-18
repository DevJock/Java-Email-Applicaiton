/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */
package monproject1bangerac8381;


import java.util.ArrayList;

/**
 *
 * @author Chiraag Bangera
 */
public class WebMail 
{
    public static boolean developmentBuild = false;
    public static Input input;
    public static DBHelper db;
    public static void main(String[] args) 
    {
        //Application Entry Point
        SOP("Starting Application... ",true);
        input = new Input();
        db = new DBHelper("mis-sql.uhcl.edu/bangerac8361","bangerac8361","1603798");
        db.ConnectSQL();
        MainMenu();
        db.Close();
    }

    public static void MainMenu()
    {
        String option = "";
        do
        {
            SOP("Welcome To WebMail", true);
            SOP("Make A Selcetion", true);
            SOP("1: Register", true);
            SOP("2: Login", true);
            SOP("x: Exit", true);
            option = input.next();
            if(option.equals("1"))
            {
                if(WebMail.developmentBuild)
                {
                    SOP("Registration...", true);
                }
                register();
            }
            else if(option.equals("2"))
            {
                if(WebMail.developmentBuild)
                {
                   SOP("Login...", true);
                }
                login();
            }
        }while(!option.equalsIgnoreCase("x"));
        SOP("Exiting Application", true);
    }
    
    
    private static void login()
    {
        SOP("Enter your Username", true);
        String userID = input.next();
        SOP("Enter your Password", true);
        String password = input.next();
        if(WebMail.developmentBuild)
        {
            SOP("Attempting to Fetch Records...", true);
        }
        if(db.VerifyUserAccount(userID))
        {
            if(WebMail.developmentBuild)
            {
                SOP("Record Found...", true);
            }
             Account account = db.GetUserAccount(userID, password);
             if(account != null)
             {
                accountView(account);
             }
             else
             {
                 SOP("Invalid Password Entered. Try Again",true);
             }
        }
        else
        {
           SOP("Record Not Found... Consider Registering", true);
        }
    } 
    
    
    
    
    
    private static void register()
    {
        SOP("Registration Wizard", true);
        SOP("Enter your Name", true);
        String name = null;
        int count = 0;
        do
        {
            if(count++ > 0)
            {
                SOP("Enter a valid Name that is atleast 3 characters long.",true);
            }
            name = input.next();
        }while(name.equalsIgnoreCase("") || name.equalsIgnoreCase(" ") || name.length() < 3);
        SOP("Enter your Password", true);
        String password = input.next();
        String id = db.GenerateRandomID(name);
        String email = id+"@webmail.com";
        email=email.toLowerCase();
        if(db.VerifyUserAccount(id))
        {
             SOE("User Already Exists... Exiting Wizard",true);
             return;
        }
        Account account = new Account(id,name,email,password);
        db.SaveUserAccount(account);
        SOP("Registered.... Your UserName is "+id, true);
        accountView(account);
    } 
    
    
    private static void accountView(Account account)
    {
        String option = null;
        int pageOffset = 0;
        do
        {
            //because we want to refresh the inbox everytime we load view
            ArrayList<Email> inbox = TimeKeeper.SortEmailsByTime(db.GetInbox(account));
            SOP("Hello "+account.getName()+", Welcome to your WebMail.",true);
            for(int i=pageOffset;i<(2 + pageOffset) && i<inbox.size();i++)
            {
                if(inbox.get(i).isNew)
                {
                    SOP((i+1 - pageOffset)+". "+db.NameForEmailID(inbox.get(i).from)+
                            ", "+inbox.get(i).subject+" (NEW), "+inbox.get(i).timeStamp, true);
                }
                else
                {
                    SOP((i+1 - pageOffset)+". "+db.NameForEmailID(inbox.get(i).from)+
                            ", "+inbox.get(i).subject+", "+inbox.get(i).timeStamp, true);
                }
            }
            if(inbox.size() > 2)
            {
                SOP("n: Next Page",true);
                SOP("p: Previous page",true);
            }
            SOP("w: Compose new Mail", true);
            SOP("s: Sent Folder",true);
            SOP("x: Log Out",true);
            option = input.next();
            if(option.equalsIgnoreCase("1"))
            {
                Email email = inbox.get(0+pageOffset);
                if(email.groupID == null || email.groupID == "" || email.groupID == "null")
                {
                    mailView(account,email);
                }
                else
                {
                    conversationView(account,email);
                }               
            }
            else if(option.equalsIgnoreCase("2"))
            {
                Email email = inbox.get(1+pageOffset);
                if(email.groupID == null || email.groupID == "" || email.groupID == "null")
                {
                    mailView(account,email);
                }
                else
                {
                    conversationView(account,email);
                }
            }
            else if(option.equalsIgnoreCase("n"))
            {
                if(inbox.size() > 2)
                {
                    if(pageOffset + 2 < inbox.size())
                    {
                        pageOffset += 2;
                    }
                    else if(pageOffset + 1 <inbox.size())
                    {
                        pageOffset += 1;
                    }
                }
            }
            else if(option.equalsIgnoreCase("p"))
            {
                if(inbox.size() > 2)
                {
                    if(pageOffset - 2 > -1)
                    {
                        pageOffset -= 2;
                    }
                    else if(pageOffset - 1 > -1)
                    {
                        pageOffset -= 1;
                    }
                }
            }
            else if(option.equalsIgnoreCase("w"))
            {
                composeView(account);
            }
            else if(option.equalsIgnoreCase("s"))
            {
                sentView(account);
            }   
        }while(!option.equalsIgnoreCase("x"));
        SOP("Logging Out",true);
    }
    
    
    private static void conversationView(Account account, Email email)
    {
        ArrayList<Email> conversation = TimeKeeper.SortEmailsByTime(db.GetConversation(account, email));    
        if(conversation.size() > 1)
        {
            SOP("Conversation Thread between "+ db.NameForEmailID(email.from) +" and "+ db.NameForEmailID(email.to),true);
            SOP("SUBJECT: "+email.subject,true);
            for(int i=0;i<conversation.size();i++)
            {
                SOP((i+1)+". "+conversation.get(i).message+", "+conversation.get(i).timeStamp,true);
            }
            SOP("x: Go Back",true);
            String opt = input.next();
            if(opt.equalsIgnoreCase("x"))
            {
                return;
            }
            else 
            {
                int option = 0;
                try
                {
                    option = Integer.parseInt(opt) - 1;
                    if(option >= 0 && option < conversation.size())
                    {
                         mailView(account,conversation.get(option));
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    SOE("Error while Parsing",true);
                }
                return;
            }
        }
    }
    
    
    private static void mailView(Account account, Email email)
    {
        String opt = null;  
        db.MarkEmailAsRead(email);
        do
        {
            SOP("FROM: "+email.from,true);
            SOP("TO: "+email.to,true);
            SOP("SUBJECT: "+email.subject,true);
            SOP("MESSGAE: "+email.message,true); 
            SOP("f: Forward Email",true);
            SOP("r: Reply to Email",true);
            SOP("x: Go Back to Account",true);
            opt = input.next();
            if(opt.equalsIgnoreCase("f"))
            {
                forwardView(account,email);
            }
            else if(opt.equalsIgnoreCase("r"))
            {
                replyView(account,email);
            }
        }while(!opt.equalsIgnoreCase("x"));
    }
    
    
    private static void sentView(Account account)
    {
        String option  = null;
        do
        {
            //we are refreshing the sentemail list every time we load the view
            ArrayList<Email> sent = TimeKeeper.SortEmailsByTime(db.GetSent(account));
            if(sent.size() == 0)
            {
                SOP("You Have not sent any Emails Yet\nPress return to go back to your Account",true);
                String x = input.next();
                return;
            }
            SOP("Listing Sent Emails...",true);
            int count = sent.size();
            for(int i=0;i<sent.size();i++)
            {
                SOP((i+1)+". "+db.NameForEmailID(sent.get(i).to)+", "+sent.get(i).subject+", "+sent.get(i).timeStamp,true);
            }
            SOP("m: Main Menu",true);
            option = input.next();
            if(!option.equalsIgnoreCase("m"))
            {
                try
                {
                    int choice = Integer.parseInt(option) - 1;
                    if(choice >= 0 && choice <count)
                    {
                        mailView(account,sent.get(choice));
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    SOE("Error during Conversion",true);
                }
            }
        }
        while(!(option.equalsIgnoreCase("m")));
    }
    
    
    private static void composeView(Account account)
    {
        String to = null;
        int count = 0;
        do
        {
            if(count++ != 0)
            {
                SOP("Enter Valid Email Address int the format (someone@webmail.com)",true);
            }
           SOP("Enter Recipient Email Address",true); 
           to = input.next();
        }while(!(to.contains("@webmail.com")));
        to = UserNameFromEmail(to);
        SOP("Enter Subject Field",true);
        String subject = input.next();
        SOP("Enter The Message to Send",true);
        String message = input.next();
        Email email = new Email(account.getUserID(),to,subject,message);
        db.SendEmail(email);
        SOP("Email Sent",true);
        return;
    }
    
    private static void forwardView(Account account, Email email)
    {
        String subject = null;
        String to = null;
        subject = "FW: "+email.subject;
        int count = 0;
        do
        {
            if(count++ != 0)
            {
                SOP("Enter Valid Email Address int the format (someone@webmail.com)",true);
            }
           SOP("Enter Recipient Email Address to Forward to",true); 
           to = input.next();
        }while(!(to.contains("@webmail.com")));
        to = to.substring(0,to.indexOf("@"));
        SOP("Enter The Message to Append with Forward Mail",true);
        String message = input.next();
        message += "\nBegin forwarded Email\n";
        message += email.message;
        Email fwEmail = new Email(account.getUserID(),to,subject,message);
        db.SendEmail(fwEmail);
        SOP("Email Forwarded",true);
        return; 
    }
    
    private static void replyView(Account account, Email email)
    {
        String subject = "RE: "+email.subject;
        SOP("Enter The Message to Reply",true);
        String message = input.next();
        String gID = "";
        if(email.groupID == null || email.groupID == "")
        {
           gID = db.GenerateConversationID(email);
        }
        else
        {
            gID = email.groupID;
        }
        
        Email reEmail = new Email(email.mailID,gID,account.getUserID(),UserNameFromEmail(email.from),
                subject,message);
        db.ReplyEmail(reEmail);
        SOP("Email Replied",true);
        return; 
    }
    
        
    // Global Printer
    public static void SOP(String str, boolean newLine)
    {
        if(newLine)
        {
            System.out.println(str);
            return;
        }
        System.out.print(str);
    }
    
    
    public static void SOE(String str, boolean newLine)
    {
        if(newLine)
        {
            System.err.println(str);
            return;
        }
        System.err.print(str);
        System.exit(-1);
    }
    
    
    public static String UserNameFromEmail(String email)
    {
        if(email.length() < 1)
        {
            return null;
        }
        return email.substring(0, email.indexOf("@"));
    }
}
