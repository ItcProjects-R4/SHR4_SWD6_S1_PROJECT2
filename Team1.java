import java.util.*;

public class Team1
{
    static ArrayList<ArrayList<String>> Users = new ArrayList<ArrayList<String>>();
    public static void main(String[] args)throws Exception
    {
        Scanner input = new Scanner(System.in);

        System.out.println("_________Bug Detective_________");

        System.out.print("Enter UserName: ");
        String userName = input.nextLine();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        try
        {
            if(isValid(userName, password))
            {
                System.out.println("Login Successful!");
                ArrayList<String> userInfo = new ArrayList<String>(Arrays.asList(userName, password));
                Users.add(userInfo);
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static boolean isValid(String username, String password)throws Exception
    {
        if(!username.trim().isEmpty() && password.length() >= 8)
        {
            return true;
        }
        else
        {
            if(username.trim().isEmpty())
            {
                throw new Exception("Username cannot be empty.");
            }
            else
            {
                throw new Exception("Password must be at least 8 characters.");
            }
        }
    }
}