<?php
    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");
  
    // Make sure the Admin is logged in and an ID was given
    if (isset($_COOKIE["user"]) and strcmp($_COOKIE["user"],"Admin") == 0 and isset($_GET["id"])) {
        $id = htmlspecialchars($_GET["id"]);
        
        $result = $sqlcon->query($sql);
        $sql = $sqlcon->prepare("DELETE FROM category WHERE CategoryID = ?");
        $sql->bind_param('i', $id);
        $sql->execute();
        $result = $sql->get_result();
        
    }
    
    // Go back to the index page
    header("Location: ./index.php?menu=categories");
?>