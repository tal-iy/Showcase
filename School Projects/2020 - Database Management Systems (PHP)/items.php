<?php

    // Check if a category ID was given
    if (isset($_GET["category"])) {
        
        // Show the selected category
        setcookie("categoryid", htmlspecialchars($_GET["category"]), time() + 2592000, "/");
        
        // Go to the items screen
        header("Location: ./index.php?menu=items");
        
    } else {
        
        // Go back to the categories screen
        header("Location: ./index.php?menu=categories");
        
    }

    
    
?>