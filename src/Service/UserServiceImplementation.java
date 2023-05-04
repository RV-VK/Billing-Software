package Service;

import DAO.*;
import Entity.User;

import java.sql.SQLException;
import java.util.*;

public class UserServiceImplementation implements UserService{
    @Override
    public int createUserService(User user) throws SQLException, ApplicationErrorException {
        UserDAO createUserDAO = new UserDAOImplemetation();
        User userResult = createUserDAO.create(user);
        if (userResult != null)
        {
            return 1;
        }
        else {
            return -1;
        }
    }

    @Override
    public int countUserService() throws ApplicationErrorException {
        UserDAO countUserDAO=new UserDAOImplemetation();
        return countUserDAO.count();
    }

    @Override
    public List listUserService(HashMap<String, String> listattributes) throws ApplicationErrorException, PageCountOutOfBoundsException {
        List<User> userList;
        UserDAO listUserDAO=new UserDAOImplemetation();

         if(Collections.frequency(listattributes.values(),null)==listattributes.size()-2&&listattributes.get("Pagelength")!=null&&listattributes.get("Pagenumber")!=null)
        {
            userList=listUserDAO.list(Integer.parseInt(listattributes.get("Pagelength")),Integer.parseInt(listattributes.get("Pagenumber")));
            return userList;
        }
        else if(Collections.frequency(listattributes.values(),null)==0)
        {
            int pageLength=Integer.parseInt(listattributes.get("Pagelength"));
            int pageNumber=Integer.parseInt(listattributes.get("Pagenumber"));
            int offset=(pageLength*pageNumber)-pageLength;
            userList=listUserDAO.list(listattributes.get("Attribute"),listattributes.get("Searchtext"),pageLength,offset);
            return userList;
        }
        else if(Collections.frequency(listattributes.values(),null)==listattributes.size()-1&&listattributes.get("Searchtext")!=null)
        {
            userList=listUserDAO.list(listattributes.get("Searchtext"));
            return userList;
        }

     return null;
    }

    @Override
    public int editUserService(HashMap<String, String> attributeMap) throws SQLException, ApplicationErrorException, UniqueConstraintException {
        UserDAO editUserDAO=new UserDAOImplemetation();
        String numberRegex="^[0-9]*$";
        String nameRegex="^[a-zA-Z]{3,30}$";
        String passwordRegex="^[a-zA-Z0-9]{8,50}$";
        List<String> userTypeList=new ArrayList<>(Arrays.asList("Admin","Purchase","Sales"));
        int id;
        try {
            id = Integer.parseInt(attributeMap.get("id").trim());
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
        if (attributeMap.get("username") != null && !attributeMap.get("username").matches(nameRegex) || attributeMap.get("password") != null && !attributeMap.get("password").matches(passwordRegex) || attributeMap.get("usertype") != null && !userTypeList.contains(attributeMap.get("usertype")) || attributeMap.get("phonenumber") != null && !attributeMap.get("phonenumber").matches(numberRegex) || attributeMap.get("firstname") != null && !attributeMap.get("firstname").matches(nameRegex) || attributeMap.get("lastname") != null && !attributeMap.get("lastname").matches(nameRegex)) {
            return 0;
        }
        boolean status;
        int updateCount=0;
        if(attributeMap.get("username") != null)
        {
            status=editUserDAO.edit(id, "username",attributeMap.get("username"));
            if(!status)
            {
                return -1;
            }
            else{
                updateCount++;
            }
        }
        if(attributeMap.get("password") != null)
        {
            if(editUserDAO.edit(id,"password",attributeMap.get("password")))
            {
                updateCount++;
            }
        }
        if(attributeMap.get("usertype") != null)
        {
            if(editUserDAO.edit(id,"usertype",attributeMap.get("usertype")))
            {
                updateCount++;
            }
        }
        if(attributeMap.get("firstname") != null)
        {
            if(editUserDAO.edit(id,"firstname",attributeMap.get("firstname")))
            {
                updateCount++;
            }
        }
        if(attributeMap.get("lastname") != null)
        {
            if(editUserDAO.edit(id,"lastname",attributeMap.get("lastname")))
            {
                updateCount++;
            }
        }
        if(attributeMap.get("phonenumber") != null)
        {
            if(editUserDAO.edit(id,"phonenumber",attributeMap.get("phonenumber")))
            {
                updateCount++;
            }
        }
        if(updateCount==(attributeMap.size()-Collections.frequency(attributeMap.values(),null)-1))
        {
            return 1;
        }
        else{
            return -1;
        }
    }

    @Override
    public int deleteUserService(String username) throws ApplicationErrorException {
        UserDAO deleteUserDAO=new UserDAOImplemetation();
        return deleteUserDAO.delete(username);
    }
}
