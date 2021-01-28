<?php

    
    
    // Make sure an item was selected
    if (isset($_GET['item'])) {
        
        // Save which item is being reviewed
        setcookie("item", htmlspecialchars($_GET["item"]), time() + 2592000, "/");
        
        // Go back to the reviewing page
        header("Location: ./index.php?menu=reviewing");
        
    } else {
        
        // Go back to the index page
        header("Location: ./index.php?menu=categories");
    }
    
?>