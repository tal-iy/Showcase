<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a user is logged in
    if (isset($_COOKIE["user"])) {
        
        $username = $_COOKIE["user"];
        
        // Delete all items in the users cart
        $sql = $sqlcon->prepare("DELETE FROM cart WHERE UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('s',$username);
        $sql->execute();
        $result = $sql->get_result();
        
    }
    
    // Go to the cart
    header("Location: ./index.php?menu=cart");
?>