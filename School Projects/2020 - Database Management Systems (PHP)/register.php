<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a username, password, email, card info, and address was given
    if (isset($_POST['username']) and isset($_POST['password']) and isset($_POST['email']) and isset($_POST['payment']) and isset($_POST['address'])) {
        
        $username = $_POST['username'];
        $password = $_POST['password'];
        $email = $_POST['email'];
        $payment = $_POST['payment'];
        $address = $_POST['address'];
        
        if ($username === '' or $password === '' or $email === '' or $payment === '' or $address === '') {
            
            // Set the menu to the register screen and set the error to invalid input
            setcookie("error", "register", time() + 2592000, "/");
            
            // Go back to the index page
            header("Location: ./index.php?menu=register");
            
        } else {

            // Find an account with the given username
            $sql = $sqlcon->prepare("SELECT * FROM account WHERE Username = ?");
            $sql->bind_param('s', $username);
            $sql->execute();
            $result = $sql->get_result();

            // Make sure an account with that username doesn't already exist
            if ($result == false or $result->num_rows != 1) {

                // Add account to the database
                $sql = $sqlcon->prepare("INSERT INTO account (UserName,Password,Email,CardInfo,BillingAddress) VALUES (?,?,?,?,?)");
                $sql->bind_param('sssss', $username, $password, $email, $payment, $address);
                $sql->execute();
                $result = $sql->get_result();
            
                // Go back to the login page
                header("Location: ./index.php?menu=login");
                
            } else {
                // Set the menu to the register screen and set the error to invalid input
                setcookie("error", "register", time() + 2592000, "/");
                
                // Go back to the register page
                header("Location: ./index.php?menu=register");
            }
        }
    } else {
        
        // Go back to the index page
        header("Location: ./index.php?menu=register");  
    }
?>