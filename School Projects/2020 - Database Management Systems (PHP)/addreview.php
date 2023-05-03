<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a user is logged in, an item is selected, and a review was given
    if (isset($_COOKIE['item']) and isset($_COOKIE["user"]) and isset($_POST['review']) and isset($_POST['rating'])) {
        
        $item = $_COOKIE['item'];
        $username = $_COOKIE["user"];
        $rating = $_POST['rating'];
        $review = $_POST['review'];
        
        // Check if this user has reviewed this item before
        $sql = $sqlcon->prepare("SELECT * FROM review WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('is', $item, $username);
        $sql->execute();
        $result = $sql->get_result();
        
        if ($result->num_rows == 1) {
            
            // Update the review
            $sql = $sqlcon->prepare("UPDATE review SET ReviewRating = ?, ReviewText = ? WHERE ItemID = ? AND UserID = (SELECT UserID FROM account WHERE UserName = ?)");
            $sql->bind_param('isis', $rating, $review, $item, $username);
            $sql->execute();
            
        } else {
            
            // Add a new review
            $sql = $sqlcon->prepare("INSERT INTO review (UserID,ItemID,ReviewRating,ReviewText) VALUES ((SELECT UserID FROM account WHERE UserName = ?),?,?,?)");
            $sql->bind_param('siis', $username, $item, $rating, $review);
            $sql->execute();
        
        }
        
        // Go to the reviews
        header("Location: ./reviews.php?item=" . $item);
        
    } else {
        
        // Go back to the categories
        header("Location: ./index.php?menu=categories");
    }
?>