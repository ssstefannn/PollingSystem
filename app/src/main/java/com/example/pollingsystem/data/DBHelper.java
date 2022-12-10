package com.example.pollingsystem.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DBHelper {

    private static SQLiteDatabase db;
    private static String DomainModels[] = {"User", "Role", "UserRole", "Poll", "Question", "Choice", "UserChoice"};
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public DBHelper(SQLiteDatabase db) {
        this.db = db;
    }

    public void Initialize() {
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
            if (field.getName().equals("id")) {
                continue;
            }
            String className = field.getType().getSimpleName();
            if (Arrays.asList(DomainModels).contains(className)) {
                Object classValue = field.get(data);
                int value = classValue.getClass().getDeclaredField("id").getInt(classValue);
                contentValues.put(className + "ID", value);
            } else {
                Object classValue = field.get(data);
                switch (className) {
                    case "Location":
                        Location location = (Location) classValue;
                        contentValues.put(className, location.getLatitude() + "," + location.getLongitude());
                        break;
                    case "Date":
                        Date date = (Date) classValue;
                        contentValues.put(className, date.getYear() + "-" + date.getMonth() + "-" + date.getDay());
                        break;
                    case "int":
                        int integer = (int) classValue;
                        contentValues.put(className, integer);
                    case "String":
                        String string = (String) classValue;
                        contentValues.put(className, string);
                        break;
                    default:
                        throw new Exception("Can't resolve data type");
                }
            }
        }
        db.insert(tableName, null, contentValues);
    }

    public void SaveUser(User user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username", user.getUsername());
        contentValues.put("Password", user.getPassword());
        db.insert("Users", null, contentValues);
    }

    public void SaveRole(Role role) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name", role.getName());
        db.insert("Roles", null, contentValues);
    }

    public void SaveUserRole(UserRole userRole) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", userRole.getUserId());
        contentValues.put("RoleID", userRole.getRoleId());
        db.insert("UserRoles", null, contentValues);
    }

    public void SavePoll(Poll poll) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", poll.getCreatedByUserId());
        contentValues.put("Name", poll.getName());
        contentValues.put("StartDate", format.format(poll.getStartDate()));
        contentValues.put("DurationInMinutes", poll.getDurationInMinutes());
        db.insert("Polls", null, contentValues);
    }

    public void SaveQuestion(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PollID", question.getPollId());
        contentValues.put("Name", question.getName());
        db.insert("Questions", null, contentValues);
    }

    public void SaveChoice(Choice choice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("QuestionID", choice.getQuestionId());
        contentValues.put("Name", choice.getName());
        db.insert("Choices", null, contentValues);
    }

    public void SaveUserChoice(UserChoice userChoice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserID", userChoice.getUserId());
        contentValues.put("ChoiceID", userChoice.getChoiceId());
        contentValues.put("SubmittedOn", format.format(userChoice.getSubmittedOn()));
        contentValues.put("SubmittedIn", userChoice.getSubmittedIn().toString());
    }

    public User GetUserById(Integer id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE UserID = ?",
                new String[]{((Integer) id).toString()});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") User user = new User(
                cursor.getInt(cursor.getColumnIndex("UserID")),
                cursor.getString(cursor.getColumnIndex("Username")),
                cursor.getString(cursor.getColumnIndex("Password"))
        );
        cursor = db.rawQuery("SELECT * FROM Roles WHERE RoleID IN (SELECT RoleID FROM UserRoles WHERE UserID = ?)",
                new String[]{((Integer) id).toString()});
        cursor.moveToFirst();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") int roleId = cursor.getInt(cursor.getColumnIndex("RoleID"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
            Role role = new Role(roleId, name);
            user.addRole(role);
        } while (cursor.moveToNext());
        cursor.close();

        return user;
    }

    public User GetUserByUsernameAndPassword(String username, String password) {
        //todo use password hash
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Username = ? AND Password = ?",
                new String[]{username, password});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") User user = new User(
                cursor.getInt(cursor.getColumnIndex("UserID")),
                cursor.getString(cursor.getColumnIndex("Username")),
                cursor.getString(cursor.getColumnIndex("Password"))
        );
        cursor = db.rawQuery("SELECT * FROM Roles " +
                        "WHERE RoleID IN " +
                        "(" +
                        "   SELECT RoleID FROM UserRoles UR " +
                        "   JOIN Users U " +
                        "   ON UR.UserID = U.UserID " +
                        "   WHERE U.Username = ? AND U.Password = ? " +
                        ")",
                new String[]{username, password});
        cursor.moveToFirst();
        List<Role> roles = new LinkedList<>();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") int roleId = cursor.getInt(cursor.getColumnIndex("RoleID"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
            Role role = new Role(roleId, name);
            roles.add(role);
        } while (cursor.moveToNext());
        cursor.close();

        user.setRoles(roles);

        return user;
    }

    public Role GetRoleById(Integer id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Roles WHERE RoleID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") Role role = new Role(
                cursor.getInt(cursor.getColumnIndex("RoleID")),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        return role;
    }

    public Role GetRoleByName(String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM Roles WHERE Name = ?",
                new String[]{name});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") Role role = new Role(
                cursor.getInt(cursor.getColumnIndex("RoleID")),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        return role;
    }

    public Choice GetChoiceById(Integer id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Choices WHERE ChoiceID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") Choice choice = new Choice(
                cursor.getInt(cursor.getColumnIndex("ChoiceID")),
                cursor.getInt(cursor.getColumnIndex("QuestionID")),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        return choice;
    }

    public Question GetQuestionById(Integer id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Questions WHERE QuestionID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") Question question = new Question(
                cursor.getInt(cursor.getColumnIndex("RoleID")),
                cursor.getInt(cursor.getColumnIndex("PollID")),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        cursor = db.rawQuery("SELECT ChoiceID FROM Choices WHERE QuestionID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        List<Choice> choices = null;
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Choice choice =
                    GetChoiceById(cursor.getInt(cursor.getColumnIndex("ChoiceID")));
            choices.add(choice);
        } while (cursor.moveToNext());
        question.setChoices(choices);

        return question;
    }

    public Poll GetPollById(Integer id) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM Polls WHERE PollID = ?",
                new String[]{((Integer) id).toString()});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") Poll poll = new Poll(
                cursor.getInt(cursor.getColumnIndex("PollID")),
                cursor.getInt(cursor.getColumnIndex("UserID")),
                cursor.getString(cursor.getColumnIndex("Name")),
                format.parse(cursor.getString(cursor.getColumnIndex("StartDate"))),
                cursor.getInt(cursor.getColumnIndex("DurationInMinutes")));

        cursor = db.rawQuery("SELECT QuestionID FROM Questions WHERE PollID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        List<Question> questions = null;
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Question question =
                    GetQuestionById(cursor.getInt(cursor.getColumnIndex("QuestionID")));
            questions.add(question);
        } while (cursor.moveToNext());
        poll.setQuestions(questions);

        return poll;
    }

    public UserChoice GetUserChoiceById(Integer id) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM UserChoices WHERE UserChoiceID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if(cursor.getCount() == 0){
            return null;
        }
        @SuppressLint("Range") UserChoice userChoice = new UserChoice(
                cursor.getInt(cursor.getColumnIndex("UserChoiceID")),
                cursor.getInt(cursor.getColumnIndex("UserID")),
                cursor.getInt(cursor.getColumnIndex("ChoiceID")),
                format.parse(cursor.getString(cursor.getColumnIndex("SubmittedOn"))),
                new Location(cursor.getString(cursor.getColumnIndex("SubmittedIn"))));

        return userChoice;
    }

    public List<UserChoice> GetUserChoicesByPollId(Integer pollId) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT UC.UserChoiceID FROM UserChoices UC  " +
                        "JOIN Choices C " +
                        "ON UC.ChoiceID = C.ChoiceID " +
                        "JOIN Questions Q " +
                        "ON C.QuestionID = Q.QuestionID " +
                        "JOIN Polls P " +
                        "ON Q.PollID = P.PollID " +
                        "WHERE P.PollID = ?",
                new String[]{pollId.toString()});
        cursor.moveToFirst();

        List<UserChoice> userChoices = null;
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") UserChoice userChoice =
                    GetUserChoiceById(cursor.getInt(cursor.getColumnIndex("UserChoiceID")));

            userChoices.add(userChoice);
        } while (cursor.moveToNext());

        return userChoices;
    }

    public List<UserChoice> GetUserChoicesByPollIdAndUserId(Integer pollId, Integer userId) throws Exception {
        List<UserChoice> userChoices = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            userChoices = GetUserChoicesByPollId(pollId)
                    .stream().filter(x -> x.getUserId() == userId).collect(Collectors.toList());
        } else {
            throw new Exception("You fucked up");
        }

        return userChoices;
    }

    public List<Poll> GetUnansweredActivePollsByUserId(Integer userId) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT PollID FROM Polls " +
                        "WHERE PollID NOT IN " +
                        "( " +
                        "   SELECT PollID FROM Polls P " +
                        "   JOIN Questions Q " +
                        "   ON P.PollID = Q.PollID " +
                        "   JOIN Choices C " +
                        "   ON Q.QuestionID = C.QuestionID " +
                        "   JOIN UserChoices UC " +
                        "   ON C.ChoiceID = UC.ChoiceID " +
                        "   WHERE UC.UserID = ?" +
                        ")",
                new String[]{userId.toString()});
        cursor.moveToFirst();
        List<Poll> polls = null;
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Poll poll =
                    GetPollById(cursor.getInt(cursor.getColumnIndex("PollID")));

            Calendar calendar = Calendar.getInstance();
            long timeInMillis = calendar.getTimeInMillis();
            Date dateInPast = new Date(timeInMillis - poll.getDurationInMinutes() * 60 * 1000);
            if (poll.getStartDate().after(dateInPast)) {
                polls.add(poll);
            }
        } while (cursor.moveToNext());

        return polls;
    }

    public List<Poll> GetAnsweredFinishedPollsByUserId(Integer userId) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT PollID FROM Polls " +
                        "WHERE PollID IN " +
                        "( " +
                        "   SELECT PollID FROM Polls P " +
                        "   JOIN Questions Q " +
                        "   ON P.PollID = Q.PollID " +
                        "   JOIN Choices C " +
                        "   ON Q.QuestionID = C.QuestionID " +
                        "   JOIN UserChoices UC " +
                        "   ON C.ChoiceID = UC.ChoiceID " +
                        "   WHERE UC.UserID = ?" +
                        ")",
                new String[]{userId.toString()});
        cursor.moveToFirst();
        List<Poll> polls = null;
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Poll poll =
                    GetPollById(cursor.getInt(cursor.getColumnIndex("PollID")));
            Calendar calendar = Calendar.getInstance();
            long timeInMillis = calendar.getTimeInMillis();
            Date dateInPast = new Date(timeInMillis - poll.getDurationInMinutes() * 60 * 1000);
            if (poll.getStartDate().before(dateInPast)) {
                polls.add(poll);
            }
        } while (cursor.moveToNext());

        return polls;
    }

    public void SetDefaultDbData() {
        String username = "stefans";
        String password = "stefans";
        User user = new User(null, username, password);
        SaveUser(user);
        Role role = new Role(null, "Admin");
        SaveRole(role);
        role.setName("Default");
        SaveRole(role);

        user = GetUserByUsernameAndPassword(username, password);
        role = GetRoleByName("Admin");
        UserRole userRole = new UserRole(null, user.getId(), role.getId());
        SaveUserRole(userRole);
        role = GetRoleByName("Default");
        userRole = new UserRole(null, user.getId(), role.getId());
        SaveUserRole(userRole);

        Poll poll = new Poll(null,user.getId(),"First poll",new Date(),90);
        SavePoll(poll);

    }
}
