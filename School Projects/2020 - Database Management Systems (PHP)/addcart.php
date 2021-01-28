<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a user is logged in and an item is selected
    if (isset($_GET['item']) and isset($_COOKIE["user"])) {
        
        $item = htmlspecialchars($_GET['item']);
        $username = $_COOKIE["user"];
        
        // Check if the item already exists in the cart
        $sql = $sqlcon->prepare("SELECT * FROM cart WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('is', $item, $username);
        $sql->execute();
        $result = $sql->get_result();
        
        if ($result->num_rows == 1) {
            
            // Increment the ItemQuantity in the cart
            $sql = $sqlcon->prepare("UPDATE cart SET ItemQuantity = ItemQuantity + 1 WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
            $sql->bind_param('is', $item, $username);
            $sql->execute();
            $result = $sql->get_result();
            
        } else {
            
            // Add the item to the cart
            $sql = $sqlcon->prepare("INSERT INTO cart (ItemID,UserID,ItemQuantity) VALUES (?,(SELECT UserID FROM account WHERE UserName = ?),1)");
            $sql->bind_param('is', $item, $username);
            $sql->execute();
            $result = $sql->get_result();
        
        }
        
    }
    
    // Go to the cart
    header("Location: ./index.php?menu=cart");
?>