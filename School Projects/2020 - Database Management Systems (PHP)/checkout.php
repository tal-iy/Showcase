<?php

    // Connect to database
    $sqlcon = new mysqli("localhost", "id15494343_toolshop", "D[6m63*[fLrBh[K4", "id15494343_tools");

    // Make sure we connected
    if ($sqlcon->connect_error)
      die("Connection failed!");

    // Make sure a user is logged in
    if (isset($_COOKIE["user"])) {
        
        $username = $_COOKIE["user"];
        
        // Get all items in the users cart
        $sql = $sqlcon->prepare("SELECT * FROM cart WHERE UserID = (SELECT UserID FROM account WHERE UserName = ?)");
        $sql->bind_param('s', $username);
        $sql->execute();
        $result = $sql->get_result();
        
        // Make sure there is at least one item in the cart
        if ($result->num_rows != 0) {
            
            // Get the total price of the order
            $sql2 = $sqlcon->prepare("SELECT UserID,SUM(ItemTotal) as TotalPrice FROM (SELECT UserID,ItemPrice*ItemQuantity as ItemTotal FROM (SELECT account.UserID as UserID,ItemPrice,ItemQuantity FROM item,cart,account WHERE item.ItemID = cart.ItemID and cart.UserID = account.UserID and account.UserName = ?) AS PriceQuantity) AS TotalTable") or die($sqlcon->error);
            $sql2->bind_param('s', $username);
            $sql2->execute();
            $result2 = $sql2->get_result();
            
            // Make sure we got a total price
            if ($result2->num_rows == 1 and $row2 = $result2->fetch_assoc()) {
                
                $userID = $row2["UserID"];
                $totalPrice = $row2["TotalPrice"];
                $date=date("y/m/d");
                $address = $_COOKIE["address"];
                
                // Delete all items in the users cart
                $sql3 = $sqlcon->prepare("DELETE FROM cart WHERE UserID = ?");
                $sql3->bind_param('i', $userID);
                $sql3->execute();
                
                // Create a new order for the user
                $sql4 = $sqlcon->prepare("INSERT INTO order_table (UserID,TotalPrice,DeliveryAddress,OrderDate) VALUES (?,?,?,?)");
                $sql4->bind_param('idss', $userID, $totalPrice, $address, $date);
                $sql4->execute();
                $orderID = $sqlcon->insert_id;
            
                // Go through each item in the cart
                while ($row = $result->fetch_assoc()) {
                    
                    // Get the purchase price of the item
                    $sql5 = $sqlcon->prepare("SELECT ItemPrice FROM item WHERE ItemID = ?");
                    $sql5->bind_param('i', $row["ItemID"]);
                    $sql5->execute();
                    $result5 = $sql5->get_result();
                    
                    // Make sure this item exists
                    if ($result5->num_rows == 1 and $row5 = $result5->fetch_assoc())
                    {
                        // Add the item to the order
                        $sql6 = $sqlcon->prepare("INSERT INTO purchase (OrderID,ItemID,ItemQuantity,PurchasePrice) VALUES (?,?,?,?)");
                        $sql6->bind_param('iiid', $orderID, $row["ItemID"], $row["ItemQuantity"], $row5["ItemPrice"]);
                        $sql6->execute();
                    }
                    
                    // Update the stock of the item
                    $sql = $sqlcon->prepare("UPDATE item SET StockQuantity = StockQuantity - ? WHERE ItemID = ?");
                    $sql->bind_param('ii', $row["ItemQuantity"], $row["ItemID"]);
                    $sql->execute();
                }
            }
            
            // Go to orders
            header("Location: ./index.php?menu=orders");
            
        } else {
            
            // Go to the cart
            header("Location: ./index.php?menu=cart");
            
        }
        
    }
?>