/*
 * Confidential Source Code. No unauthorized copying or redistribution in parts or whole allowed. 
 * Copyright 2017. Chiraag Bangera
 */
package monproject1bangerac8381;

/**
 *
 * @author Chiraag Bangera
 */
import java.io.*;


public class Input 
{
    BufferedReader br;
    public Input()
    {
        br = new BufferedReader(new InputStreamReader(System.in));
    }
    
    
    public String next()
    {
        try
        {
            return br.readLine();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public int nextInt()
    {
        try
        {
            return Integer.parseInt(br.readLine());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    
    public double nextDouble()
    {
        try
        {
            return Double.parseDouble(br.readLine());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0.0;
    }
    
    public char nextChar()
    {
        try
        {
            return (char)br.read();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return '\0';
    }
    
}
