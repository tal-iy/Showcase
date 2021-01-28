<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a item name and description was given and the Admin is logged in
    if (isset($_POST['name']) and isset($_POST['description']) and isset($_POST['price']) and isset($_POST['quantity']) and isset($_POST['category']) and isset($_COOKIE["user"]) and strcmp($_COOKIE["user"],"Admin") == 0) {
        
        $name = $_POST['name'];
        $description = $_POST['description'];
        $price = $_POST['price'];
        $quantity = $_POST['quantity'];
        $category = $_POST['category'];
        
        // Add item to the database
        $sql = $sqlcon->prepare("INSERT INTO item (ItemName,ItemDescription,ItemPrice,StockQuantity) VALUES (?,?,?,?)");
        $sql->bind_param('ssdi', $name, $description, $price, $quantity);
        $sql->execute();
        $result = $sql->get_result();
        
        // Associate the new item with a category
        $sql = $sqlcon->prepare("INSERT INTO group_table (CategoryID,ItemID,GroupRank) VALUES (?,( SELECT LAST_INSERT_ID() ),1)");
        $sql->bind_param('i', $_POST['category']);
        $sql->execute();
        $result = $sql->get_result();
        
    }
    
    // Go back to the index page
    header("Location: ./index.php?menu=items");
?>