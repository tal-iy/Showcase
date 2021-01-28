<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a category name and description was given and the Admin is logged in
    if (isset($_POST['name']) and isset($_POST['description']) and isset($_COOKIE["user"]) and strcmp($_COOKIE["user"],"Admin") == 0) {
        
        $name = $_POST['name'];
        $description = $_POST['description'];
        
        // Add category to the database
        $sql = $sqlcon->prepare("INSERT INTO category (CategoryName,CategoryDescription) VALUES (?,?)");
        $sql->bind_param('ss', $name, $description);
        $sql->execute();
        $result = $sql->get_result();
        
    }
    
    // Go back to the index page
    header("Location: ./index.php?menu=categories");
?>