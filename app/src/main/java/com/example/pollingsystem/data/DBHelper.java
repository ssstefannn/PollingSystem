package com.example.pollingsystem.data;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.example.pollingsystem.data.model.Choice;
import com.example.pollingsystem.data.model.Poll;
import com.example.pollingsystem.data.model.Question;
import com.example.pollingsystem.data.model.Role;
import com.example.pollingsystem.data.model.User;
import com.example.pollingsystem.data.model.UserChoice;
import com.example.pollingsystem.data.model.UserRole;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;

public class DBHelper {

    private static SQLiteDatabase db;
    private static String DomainModels[] = {"User","Role","UserRole","Poll","Question","Choice","UserChoice"};

    public DBHelper(SQLiteDatabase db) {
        this.db = db;
    }

    public void Initialize(){
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Roles");
        db.execSQL("DROP TABLE IF EXISTS UserRoles");
        db.execSQL("DROP TABLE IF EXISTS Polls");
        db.execSQL("DROP TABLE IF EXISTS Questions");
        db.execSQL("DROP TABLE IF EXISTS Choices");
        db.execSQL("DROP TABLE IF EXISTS UserChoices");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Users(" +
                "UserID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Username TEXT," +
                "Password TEXT" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Roles(" +
                "RoleID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS UserRoles(" +
                "UserRoleID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "RoleID INTEGER NOT NULL," +
                "CONSTRAINT FK_UserRoles" +
                "   FOREIGN KEY (UserID) REFERENCES Users (UserID)," +
                "   FOREIGN KEY (RoleID) REFERENCES Roles (RoleID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Polls(" +
                "PollID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "Name TEXT," +
                "StartDate TEXT," +
                "DurationInMinutes INTEGER," +
                "CONSTRAINT FK_Polls" +
                "   FOREIGN KEY (UserID) REFERENCES Users (UserID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Questions(" +
                "QuestionID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "PollID INTEGER NOT NULL," +
                "Name TEXT," +
                "CONSTRAINT [FK_Questions]" +
                "   FOREIGN KEY (PollID) REFERENCES Polls (PollID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Choices(" +
                "ChoiceID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "QuestionID INTEGER NOT NULL," +
                "Name TEXT," +
                "CONSTRAINT [FK_Choices]" +
                "   FOREIGN KEY (QuestionID) REFERENCES Questions (QuestionID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS UserChoices(" +
                "UserChoiceID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "UserID INTEGER NOT NULL," +
                "ChoiceID INTEGER NOT NULL," +
                "SubmittedOn TEXT," +
                "SubmittedIn TEXT," +
                "CONSTRAINT [FK_UserChoices]" +
                "   FOREIGN KEY (UserID) REFERENCES Users (UserID)," +
                "   FOREIGN KEY (ChoiceID) REFERENCES Choices (ChoiceID)" +
                ");");


    }

    public <T> void Save(T data) throws Exception {
        String tableName = data.getClass().getSimpleName() + "s";
        ContentValues contentValues = new ContentValues();
        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getName().equals("id")){
                continue;
            }
            String className = field.getType().getSimpleName();
            if(Arrays.asList(DomainModels).contains(className)){
                Object classValue = field.get(data);
                int value = classValue.getClass().getDeclaredField("id").getInt(classValue);
                contentValues.put(className+"ID",value);
            }
            else{
                Object classValue = field.get(data);
                switch (className){
                    case "Location":
                        Location location = (Location)classValue;
                        contentValues.put(className,location.getLatitude()+","+location.getLongitude());
                        break;
                    case "Date":
                        Date date = (Date)classValue;
                        contentValues.put(className,date.getYear()+"-"+date.getMonth()+"-"+date.getDay());
                        break;
                    case "int":
                        int integer = (int)classValue;
                        contentValues.put(className,integer);
                    case "String":
                        String string = (String)classValue;
                        contentValues.put(className,string);
                        break;
                    default:
                        throw new Exception("Can't resolve data type");
                }
            }
        }
        db.insert(tableName, null, contentValues);
    }

    public void SaveUser(User user){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username",user.getUsername());
        contentValues.put("Password",user.getPassword());
        db.insert("Users",null,contentValues);
    }

    public void SaveRole(Role role){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",role.getName());
        db.insert("Roles",null,contentValues);
    }

    public void SaveUserRole(UserRole userRole){
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID",userRole.getUser().getId());
        contentValues.put("RoleID",userRole.getRole().getId());
        db.insert("UserRoles",null,contentValues);
    }

    public void SavePoll(Poll poll){
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID",poll.getCreatedBy().getId());
        contentValues.put("Name",poll.getName());
        contentValues.put("StartDate",poll.getStartDate().toString());
        contentValues.put("DurationInMinutes",poll.getDurationInMinutes());
        db.insert("Polls",null,contentValues);
    }

    public void SaveQuestion(Question question){
        ContentValues contentValues = new ContentValues();
        contentValues.put("PollID",question.getPoll().getId());
        contentValues.put("Name",question.getName());
        db.insert("Questions",null,contentValues);
    }

    public void SaveChoice(Choice choice){
        ContentValues contentValues = new ContentValues();
        contentValues.put("QuestionID",choice.getQuestion().getId());
        contentValues.put("Name",choice.getName());
        db.insert("Choices",null,contentValues);
    }

    public void SaveUserChoice(UserChoice userChoice){
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID",userChoice.getUser().getId());
        contentValues.put("ChoiceID",userChoice.getChoice().getId());
        contentValues.put("SubmittedOn",userChoice.getSubmittedOn().toString());
        contentValues.put("SubmittedIn",userChoice.getSubmittedIn().toString());
    }
}
