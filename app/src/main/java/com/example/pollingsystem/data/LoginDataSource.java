package com.example.pollingsystem.data;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;

import com.example.pollingsystem.data.model.LoggedInUser;
import com.example.pollingsystem.data.model.User;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password, SQLiteDatabase db) {

        try {
            // TODO: handle loggedInUser authentication
            DBHelper dbHelper = new DBHelper(db);
            User user = dbHelper.GetUserByUsernameAndPassword(username,password);
            if(user != null){
                LoggedInUser loggedInUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                user.getUsername(),
                                user);
                return new Result.Success<>(loggedInUser);
            }else {
                throw new Exception();
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}