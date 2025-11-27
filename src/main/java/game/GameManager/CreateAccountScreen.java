package game.gamemanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class CreateAccountScreen extends BackgroundSetUp{

    private VBox createAccountVBox;

    private TextField usernameTF;
    private TextField pwdTF;

    private Button createAccountButton;

    private String usernameString;
    private String pwdString;

    private Label userDoesSomethingWrongLabel;

    private Path userAccountsFilePath = Paths.get("user_accounts.txt");

    String controllerImgPathString = "/game/gamemanager/ChatGPTGeneratedControllerBackground.png";

    public CreateAccountScreen(){
        rootStackPane = new StackPane();
        
        constructBackgroundPane(controllerImgPathString);
        constructDimAndBlurBackgroundImage();

        constructVBox();
        rootStackPane.getChildren().addAll(backgroundPane, dimOverlay, this.createAccountVBox);
    }
   
    @Override 
    public void constructVBox(){
        createAccountVBox = new VBox(15);
        createAccountVBox.setPrefSize(300, 250);
        createAccountVBox.setMaxSize(300, 250);
        createAccountVBox.setAlignment(Pos.TOP_CENTER);
        createAccountVBox.setPadding(new Insets(20));
        createAccountVBox.setStyle(
            "-fx-background-color: #c9f3ff;" +
            "-fx-background-radius: 5;" //round corners
        );

        Label pleaseSignUp = new Label("Please Register");
        pleaseSignUp.setStyle(
            "-fx-font-size: 20px"
        );

        this.usernameTF = new TextField();
        this.usernameTF.setPromptText("Username");
        usernameTF.setStyle(
            "-fx-background-color: #62ddff;" + 
            "-fx-prompt-text-fill: black;"
        );

        this.pwdTF = new TextField();
        this.pwdTF.setPromptText("Password");
        this.pwdTF.setStyle(
            "-fx-background-color: #62ddff;" +
            "-fx-prompt-text-fill: black;" 
        );

        this.createAccountButton = new Button("Sign Up");
        this.createAccountButton.setMaxWidth(Double.MAX_VALUE); // fill the vBox width
        this.createAccountButton.setStyle(
            "-fx-background-color: #ff0066;" +
            "-fx-text-fill: white;"
        );

        userDoesSomethingWrongLabel = new Label();
        userDoesSomethingWrongLabel.setVisible(false);
        userDoesSomethingWrongLabel.setManaged(false); //don't take up layout space
        userDoesSomethingWrongLabel.setWrapText(true); //enable text wrapping
        userDoesSomethingWrongLabel.setMaxWidth(Double.MAX_VALUE);
        userDoesSomethingWrongLabel.setStyle(
            "-fx-text-fill: red;"
        );
        
        createAccountVBox.getChildren().addAll(pleaseSignUp, usernameTF, pwdTF, userDoesSomethingWrongLabel, createAccountButton);

        createAccountButton.setOnAction(event -> {
            this.usernameString = usernameTF.getText().trim();
            this.pwdString = pwdTF.getText().trim();

            if(!isValidUsername(this.usernameString)){
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }

            if(!isValidPwd(pwdString, usernameString)){
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }

            userDoesSomethingWrongLabel.setVisible(false);
            userDoesSomethingWrongLabel.setManaged(false);

            try{
                Files.writeString(userAccountsFilePath, usernameString + ":" + pwdString + "\n", StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            }catch (IOException e){
                userDoesSomethingWrongLabel.setText("Error: " + e.getMessage());
                userDoesSomethingWrongLabel.setVisible(true);
                userDoesSomethingWrongLabel.setManaged(true);
                return;
            }
           
            //if there were no errors along the way, this is the step to call loginScreen 
        });

    }

    public boolean isValidUsername(String username){
        if(username == null || username.isBlank()) {
            userDoesSomethingWrongLabel.setText("Username cannot be blank or null, please try again!");
            return false;
        }
        if(username.length() < 3){
            userDoesSomethingWrongLabel.setText("Username must have at least 3 characters, please try again!");
            return false;
        }
        if(username.length() > 15){
            userDoesSomethingWrongLabel.setText("Username must only have 3 to 15 characters, please try again!");
            return false;
        }
        if(!Character.isLetter(username.charAt(0))) {
            userDoesSomethingWrongLabel.setText("Username must start with a letter, please try again!");
            return false;
        }
        if(!username.matches("[A-Za-z][A-Za-z0-9_]*")){
            userDoesSomethingWrongLabel.setText("Username can only have letters, digits and _, please try again");
            return false;
        }
        if(Files.exists(userAccountsFilePath) && Files.isReadable(userAccountsFilePath)){
            try{
                List<String> lines = Files.readAllLines(userAccountsFilePath);
                for(String line : lines){
                    String[] parts = line.split(":", 2); //to prevent bugs, split at the first : into 2 Strings
                    String name = parts[0];
                    if(username.equalsIgnoreCase(name)){
                        userDoesSomethingWrongLabel.setText("Username already exists, please try another one!");
                        return false;
                    }
                }
            }catch(IOException e){
                userDoesSomethingWrongLabel.setText("Error: " + e.getMessage());
                return false;
            }
       }
        
        return true;
    }

    public boolean isValidPwd(String pwd, String username){
        if(pwd == null || pwd.isBlank()){
            userDoesSomethingWrongLabel.setText("Password cannot be blank or null, please try again!");
            return false;
        }
        if(pwd.length() < 6 || pwd.length() > 20){
            userDoesSomethingWrongLabel.setText("Password must only have 6 to 20 characters, please try again!");
            return false;
        }
        if(!pwd.matches(".*[A-Za-z].*")){
            userDoesSomethingWrongLabel.setText("Password must contain at least 1 letter, please try again!");
            return false;
        }
        if(pwd.contains(" ")){
            userDoesSomethingWrongLabel.setText("Password cannot contain a space");
            return false;
        }
        if(pwd.equalsIgnoreCase(username)){ 
            userDoesSomethingWrongLabel.setText("Password cannot be the same as the username!");
            return false;
        }

        return true;
    }

    @Override
    public StackPane getRootNode(){
        return this.rootStackPane;
    }
    
}
