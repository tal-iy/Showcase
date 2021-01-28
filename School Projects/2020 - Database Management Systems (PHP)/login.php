<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a username and password was given
    if (isset($_POST['username']) and isset($_POST['password'])) {

        // Find an account with the given username and password
        $sql = $sqlcon->prepare("SELECT * FROM account WHERE Username = ? and Password = ?");
        $sql->bind_param('ss', $_POST['username'], $_POST['password']);
        $sql->execute();
        $result = $sql->get_result();
        

        // Make sure an account with the username and password exists
        if ($result != false && $result->num_rows == 1) {
            
            $row = $result->fetch_assoc();

            setcookie("user", $row["UserName"], time() + 2592000, "/");
            setcookie("email", $row["Email"], time() + 2592000, "/");
            setcookie("address", $row["BillingAddress"], time() + 2592000, "/");
            
            
            // Go back to the index page
            header("Location: ./index.php?menu=categories");

        } else {
            
            // Set the menu to the login screen and set the error to invalid login
            setcookie("error", "login", time() + 2592000, "/");
            
            // Go back to the login page
            header("Location: ./index.php?menu=login");
        }
    } else {
        
        // Set the menu to the login screen and set the error to invalid login
        setcookie("error", "login", time() + 2592000, "/");
        
        // Go back to the login page
        header("Location: ./index.php?menu=login");
    }
    
?>