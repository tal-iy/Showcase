<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure the Admin is logged in and the item and user was given
    if (isset($_COOKIE["user"]) and strcmp($_COOKIE["user"],"Admin") == 0 and isset($_GET["item"]) and isset($_GET["user"])) {
        
        $item = htmlspecialchars($_GET["item"]);
        $user = htmlspecialchars($_GET["user"]);
        
        $sql = $sqlcon->prepare("DELETE FROM review WHERE ItemID = ? AND UserID = ?");
        $sql->bind_param('ii', $item, $user);
        $sql->execute();
        $result = $sql->get_result();
        
    }
    
    // Go back to the index page
    header("Location: ./index.php?menu=reviews");
?>