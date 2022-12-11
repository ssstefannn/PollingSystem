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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DBHelper {

    private static SQLiteDatabase db;
    private static String DomainModels[] = {"User", "Role", "UserRole", "Poll", "Question", "Choice", "UserChoice"};
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private Boolean startFromScratch = true;

    public DBHelper(SQLiteDatabase db) {
        this.db = db;
    }

    public void Initialize() {
        if (startFromScratch) {
            db.execSQL("DROP TABLE IF EXISTS Users");
            db.execSQL("DROP TABLE IF EXISTS Roles");
            db.execSQL("DROP TABLE IF EXISTS UserRoles");
            db.execSQL("DROP TABLE IF EXISTS Polls");
            db.execSQL("DROP TABLE IF EXISTS Questions");
            db.execSQL("DROP TABLE IF EXISTS Choices");
            db.execSQL("DROP TABLE IF EXISTS UserChoices");
        }
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Users(" +
                "UserID TEXT PRIMARY KEY," +
                "Username TEXT," +
                "Password TEXT" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Roles(" +
                "RoleID TEXT PRIMARY KEY," +
                "Name TEXT" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS UserRoles(" +
                "UserRoleID TEXT PRIMARY KEY," +
                "UserID INTEGER NOT NULL," +
                "RoleID INTEGER NOT NULL," +
                "CONSTRAINT FK_UserRoles" +
                "   FOREIGN KEY (UserID) REFERENCES Users (UserID)," +
                "   FOREIGN KEY (RoleID) REFERENCES Roles (RoleID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Polls(" +
                "PollID TEXT PRIMARY KEY," +
                "UserID INTEGER NOT NULL," +
                "Name TEXT," +
                "StartDate TEXT," +
                "DurationInMinutes INTEGER," +
                "CONSTRAINT FK_Polls" +
                "   FOREIGN KEY (UserID) REFERENCES Users (UserID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Questions(" +
                "QuestionID TEXT PRIMARY KEY," +
                "PollID INTEGER NOT NULL," +
                "Name TEXT," +
                "CONSTRAINT [FK_Questions]" +
                "   FOREIGN KEY (PollID) REFERENCES Polls (PollID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS Choices(" +
                "ChoiceID TEXT PRIMARY KEY," +
                "QuestionID INTEGER NOT NULL," +
                "Name TEXT," +
                "CONSTRAINT [FK_Choices]" +
                "   FOREIGN KEY (QuestionID) REFERENCES Questions (QuestionID)" +
                ");");
        db.execSQL("" +
                "CREATE TABLE IF NOT EXISTS UserChoices(" +
                "UserChoiceID TEXT PRIMARY KEY," +
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
        contentValues.put("UserID", user.getId().toString());
        contentValues.put("Username", user.getUsername());
        contentValues.put("Password", user.getPassword());
        db.insert("Users", null, contentValues);
    }

    public void SaveRole(Role role) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("RoleID", role.getId().toString());
        contentValues.put("Name", role.getName());
        db.insert("Roles", null, contentValues);
    }

    public void SaveUserRole(UserRole userRole) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserRoleID", userRole.getId().toString());
        contentValues.put("UserID", userRole.getUserId().toString());
        contentValues.put("RoleID", userRole.getRoleId().toString());
        db.insert("UserRoles", null, contentValues);
    }

    public void SavePoll(Poll poll) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PollID", poll.getId().toString());
        contentValues.put("UserID", poll.getCreatedByUserId().toString());
        contentValues.put("Name", poll.getName());
        contentValues.put("StartDate", format.format(poll.getStartDate()));
        contentValues.put("DurationInMinutes", poll.getDurationInMinutes());
        db.insert("Polls", null, contentValues);
    }

    public void SaveQuestion(Question question) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("QuestionID", question.getId().toString());
        contentValues.put("PollID", question.getPollId().toString());
        contentValues.put("Name", question.getName());
        db.insert("Questions", null, contentValues);
    }

    public void SaveChoice(Choice choice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ChoiceID", choice.getId().toString());
        contentValues.put("QuestionID", choice.getQuestionId().toString());
        contentValues.put("Name", choice.getName());
        db.insert("Choices", null, contentValues);
    }

    public void SaveUserChoice(UserChoice userChoice) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UserChoiceID", userChoice.getId().toString());
        contentValues.put("UserID", userChoice.getUserId().toString());
        contentValues.put("ChoiceID", userChoice.getChoiceId().toString());
        contentValues.put("SubmittedOn", format.format(userChoice.getSubmittedOn()));
        contentValues.put("SubmittedIn", userChoice.getSubmittedIn().toString());
    }

    public User GetUserById(UUID id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE UserID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") User user = new User(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("UserID"))),
                cursor.getString(cursor.getColumnIndex("Username")),
                cursor.getString(cursor.getColumnIndex("Password"))
        );
        cursor = db.rawQuery("SELECT * FROM Roles WHERE RoleID IN (SELECT RoleID FROM UserRoles WHERE UserID = ?)",
                new String[]{id.toString()});
        cursor.moveToFirst();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") UUID roleId = UUID.fromString(cursor.getString(cursor.getColumnIndex("RoleID")));
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
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") User user = new User(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("UserID"))),
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
            @SuppressLint("Range") UUID roleId = UUID.fromString(cursor.getString(cursor.getColumnIndex("RoleID")));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("Name"));
            Role role = new Role(roleId, name);
            roles.add(role);
        } while (cursor.moveToNext());
        cursor.close();

        user.setRoles(roles);

        return user;
    }

    public User GetUserByUsername(String username) {
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Username = ?",
                new String[]{username});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") User user = new User(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("UserID"))),
                cursor.getString(cursor.getColumnIndex("Username")),
                cursor.getString(cursor.getColumnIndex("Password"))
        );

        return user;
    }

    public Role GetRoleById(UUID id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Roles WHERE RoleID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") Role role = new Role(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("RoleID"))),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        return role;
    }

    public Role GetRoleByName(String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM Roles WHERE Name = ?",
                new String[]{name});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") Role role = new Role(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("RoleID"))),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        return role;
    }

    public Choice GetChoiceById(UUID id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Choices WHERE ChoiceID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") Choice choice = new Choice(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("ChoiceID"))),
                UUID.fromString(cursor.getString(cursor.getColumnIndex("QuestionID"))),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        return choice;
    }

    public Question GetQuestionById(UUID id) {
        Cursor cursor = db.rawQuery("SELECT * FROM Questions WHERE QuestionID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") Question question = new Question(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("QuestionID"))),
                UUID.fromString(cursor.getString(cursor.getColumnIndex("PollID"))),
                cursor.getString(cursor.getColumnIndex("Name"))
        );

        cursor = db.rawQuery("SELECT ChoiceID FROM Choices WHERE QuestionID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        List<Choice> choices = new LinkedList<>();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Choice choice =
                    GetChoiceById(UUID.fromString(cursor.getString(cursor.getColumnIndex("ChoiceID"))));
            choices.add(choice);
        } while (cursor.moveToNext());
        question.setChoices(choices);

        return question;
    }

    public Poll GetPollById(UUID id) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM Polls WHERE PollID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") Poll poll = new Poll(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("PollID"))),
                UUID.fromString(cursor.getString(cursor.getColumnIndex("UserID"))),
                cursor.getString(cursor.getColumnIndex("Name")),
                format.parse(cursor.getString(cursor.getColumnIndex("StartDate"))),
                cursor.getInt(cursor.getColumnIndex("DurationInMinutes")));

        cursor = db.rawQuery("SELECT QuestionID FROM Questions WHERE PollID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        List<Question> questions = new LinkedList<>();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Question question =
                    GetQuestionById(UUID.fromString(cursor.getString(cursor.getColumnIndex("QuestionID"))));
            questions.add(question);
        } while (cursor.moveToNext());
        poll.setQuestions(questions);

        return poll;
    }

    public UserChoice GetUserChoiceById(UUID id) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT * FROM UserChoices WHERE UserChoiceID = ?",
                new String[]{id.toString()});
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return null;
        }
        @SuppressLint("Range") UserChoice userChoice = new UserChoice(
                UUID.fromString(cursor.getString(cursor.getColumnIndex("UserChoiceID"))),
                UUID.fromString(cursor.getString(cursor.getColumnIndex("UserID"))),
                UUID.fromString(cursor.getString(cursor.getColumnIndex("ChoiceID"))),
                format.parse(cursor.getString(cursor.getColumnIndex("SubmittedOn"))),
                new Location(cursor.getString(cursor.getColumnIndex("SubmittedIn"))));

        return userChoice;
    }

    public List<UserChoice> GetUserChoicesByPollId(UUID pollId) throws ParseException {
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

        List<UserChoice> userChoices = new LinkedList<>();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") UserChoice userChoice =
                    GetUserChoiceById(UUID.fromString(cursor.getString(cursor.getColumnIndex("UserChoiceID"))));

            userChoices.add(userChoice);
        } while (cursor.moveToNext());

        return userChoices;
    }

    public List<UserChoice> GetUserChoicesByPollIdAndUserId(UUID pollId, UUID userId) throws Exception {
        List<UserChoice> userChoices = new LinkedList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            userChoices = GetUserChoicesByPollId(pollId)
                    .stream().filter(x -> x.getUserId().equals(userId)).collect(Collectors.toList());
        } else {
            throw new Exception("You fucked up");
        }

        return userChoices;
    }

    public List<Poll> GetUnansweredActivePollsByUserId(UUID userId) throws ParseException {
        Cursor cursor = db.rawQuery("SELECT PollID FROM Polls " +
                        "WHERE PollID NOT IN " +
                        "( " +
                        "   SELECT P.PollID FROM Polls P " +
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
        List<Poll> polls = new LinkedList<>();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Poll poll =
                    GetPollById(UUID.fromString(cursor.getString(cursor.getColumnIndex("PollID"))));

            Calendar calendar = Calendar.getInstance();
            long timeInMillis = calendar.getTimeInMillis();
            Date dateInPast = new Date(timeInMillis - poll.getDurationInMinutes() * 60 * 1000);
            if (true || poll.getStartDate().after(dateInPast)) {
                polls.add(poll);
            }
        } while (cursor.moveToNext());

        return polls;
    }

    public List<Poll> GetAnsweredFinishedPollsByUserId(UUID userId) throws ParseException {
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
        List<Poll> polls = new LinkedList<>();
        do {
            if (cursor.getCount() == 0) {
                break;
            }
            @SuppressLint("Range") Poll poll =
                    GetPollById(UUID.fromString(cursor.getString(cursor.getColumnIndex("PollID"))));
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
        if (!startFromScratch) {
            return;
        }
        String username = "stefans";
        String password = "stefans";
        User user = new User(username, password);
        SaveUser(user);
        Role role = new Role("Admin");
        SaveRole(role);
        role = new Role("Default");
        SaveRole(role);

        role = GetRoleByName("Admin");
        UserRole userRole = new UserRole(user.getId(), role.getId());
        SaveUserRole(userRole);
        role = GetRoleByName("Default");
        userRole = new UserRole(user.getId(), role.getId());
        SaveUserRole(userRole);

        Poll poll = new Poll(user.getId(), "First poll", new Date(), 90);
        SavePoll(poll);
        Question question = new Question(poll.getId(), "First question of first poll");
        SaveQuestion(question);
        Choice choice = new Choice(question.getId(), "First choice of first question of first poll");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Second choice of first question of first poll");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Third choice of first question of first poll");
        SaveChoice(choice);
        question = new Question(poll.getId(), "Second question of first poll");
        SaveQuestion(question);
        choice = new Choice(question.getId(), "First choice of second question of first poll");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Second choice of second question of first poll");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Third choice of second question of first poll");
        SaveChoice(choice);

        poll = new Poll(user.getId(), "Second poll", new Date(), 90);
        SavePoll(poll);
        question = new Question(poll.getId(), "First question of second");
        SaveQuestion(question);
        choice = new Choice(question.getId(), "First choice of first question of second");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Second choice of first question of second");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Third choice of first question of second");
        SaveChoice(choice);
        question = new Question(poll.getId(), "Second question of second");
        SaveQuestion(question);
        choice = new Choice(question.getId(), "First choice of second question of second");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Second choice of second question of second");
        SaveChoice(choice);
        choice = new Choice(question.getId(), "Third choice of second question of second");
        SaveChoice(choice);

    }
}
