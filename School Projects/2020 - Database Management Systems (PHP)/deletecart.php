<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a user is logged in, an item is selected, and a quantity is selected
    if (isset($_GET['item']) and isset($_GET['num']) and isset($_COOKIE["user"])) {
        
        $item = htmlspecialchars($_GET['item']);
        $num = htmlspecialchars($_GET['num']);
        $username = $_COOKIE["user"];
        
        // Check if the item exists in the cart
        $sql = $sqlcon->prepare("SELECT * FROM cart WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('is', $item, $username);
        $sql->execute();
        $result = $sql->get_result();
        
        // Make sure that there is at least one item in the cart
        if ($result->num_rows == 1 and $row = $result->fetch_assoc()) {
            
            // Check if there is only one in the cart or all were selected
            if ($num == 0 or $row["ItemQuantity"] == 1 ) {
                
                // Delete the item from the cart
                $sql = $sqlcon->prepare("DELETE FROM cart WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
                $sql->bind_param('is', $item, $username);
                $sql->execute();
                $result = $sql->get_result();
                
            } else {
                
                // Remove one item from the quantity
                $sql = $sqlcon->prepare("UPDATE cart SET ItemQuantity = ItemQuantity - 1 WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
                $sql->bind_param('is', $item, $username);
                $sql->execute();
                $result = $sql->get_result();
                
            } 
        }
        
    }
    
    // Go to the cart
    header("Location: ./index.php?menu=cart");
?>