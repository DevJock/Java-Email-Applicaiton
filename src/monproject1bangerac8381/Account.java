/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */
package monproject1bangerac8381;

/**
 *
 * @author Chiraag Bangera
 */
public class Account 
{
    private String _name;
    private String _email;
    private String _password;
    private String _userID;
    
    
    public Account()
    {
        _name = "";
        _email = "";
        _password = "";
        _userID = "";
    }
    
    public Account(String userID, String name, String email, String password)
    {
        _name = name;
        _email = email;
        _password = password;
        _userID = userID;
    }
    
    
    public String getName()
    {
        return _name;
    }
    
    
    public String getEmail()
    {
        return _email;
    }
    
    
    public String getUserID()
    {
        return _userID;
    }
    
    public String getPassword()
    {
        return _password;
    }
    
    public String toString()
    {
        return _name + "( " +_userID+" )";
    }
}
