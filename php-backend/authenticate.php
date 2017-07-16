<?php

     $response = array();

     $pin = $_POST["pin"];
     $userID = $_POST["userID"];

     $SESSID = getSESSID($userID,$pin);

     if($SESSID == null){
          $response["success"] = false;
          $response["debug"] = "getSESSID()";
          $response["balance"] = "NULL";
     }else{
          $response["success"] = true;
          $response["debug"] = "success";
          $response["balance"] = "NULL";
          logout($SESSID);
     }

     echo json_encode($response);


     function logout($SESSID){
          $ch = curl_init();

          curl_setopt($ch, CURLOPT_URL, "https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_Logout");
          curl_setopt($ch, CURLOPT_HTTPHEADER, array("Cookie: SESSID=" . $SESSID));
          curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

          $result = curl_exec($ch);
          if (curl_errno($ch)) {
               curl_close ($ch);
               return false;
          }else{
               curl_close ($ch);
               return true;
          }
     }

     function getSESSID($SID, $PIN){
          $ch = curl_init();

          curl_setopt($ch, CURLOPT_URL, "https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_ValLogin");
          curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
          curl_setopt($ch, CURLOPT_POSTFIELDS, "sid=". $SID . "&PIN=" . $PIN);
          curl_setopt($ch, CURLOPT_POST, 1);
          curl_setopt($ch, CURLOPT_ENCODING, 'gzip, deflate');
          // get headers too with this line
          curl_setopt($ch, CURLOPT_HEADER, 1);


          $headers = array();
          $headers[] = "Pragma: no-cache";
          $headers[] = "Origin: https://zagweb.gonzaga.edu";
          $headers[] = "Accept-Encoding: gzip, deflate, br";
          $headers[] = "Accept-Language: en-US,en;q=0.8";
          $headers[] = "Upgrade-Insecure-Requests: 1";
          $headers[] = "User-Agent: Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.86 Safari/537.36";
          $headers[] = "Content-Type: application/x-www-form-urlencoded";
          $headers[] = "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8";
          $headers[] = "Cache-Control: no-cache";
          $headers[] = "Referer: https://zagweb.gonzaga.edu/pls/gonz/twbkwbis.P_WWWLogin";
          $headers[] = "Cookie: TESTID=set; accessibility=false; _ga=GA1.2.125809831.1497317199; _gid=GA1.2.926823714.1497978173; __utmt=1; __utma=247906316.125809831.1497317199.1497977790.1498030046.2; __utmb=247906316.9.10.1498030046; __utmc=247906316; __utmz=247906316.1498030046.2.2.utmcsr=gonzaga.edu|utmccn=(referral)|utmcmd=referral|utmcct=/Campus-Resources/Offices-and-Services-A-Z/Student-Financial-Services/Student-Accounts/Zag-Card/bulldog-bucks-form.asp";
          $headers[] = "Connection: keep-alive";
          curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
          $result = curl_exec($ch);

          if (curl_errno($ch)) {
               return NULL;
          }

          preg_match('/^Set-Cookie:\s*([^;]*)/mi', $result, $m);

          parse_str($m[1], $cookies);
          $SESSID = explode("Connection", $cookies["SESSID"])[0];
          if(strlen($SESSID) > 1){
               return $SESSID;
          }else{
               return NULL;
          }
     }

?>
