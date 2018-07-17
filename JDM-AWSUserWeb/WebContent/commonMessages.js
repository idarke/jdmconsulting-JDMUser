
function showInlineErrorMessage(error)
{
   showErrorMessage(error, "inline");
   scrollToTop();
}

function showBlockErrorMessage(error)
{
   showErrorMessage(error, "block");
   scrollToTop();
}

function showInlineInfoMessage(info)
{
   showInfoMessage(info, "inline");
   scrollToTop();
}

function showBlockInfoMessage(info)
{
   showInfoMessage(info, "block");
   scrollToTop();
}

function showErrorMessage(error, showHow)
{
   hideMessages();
   var msgdiv = document.getElementById("errorMessageDiv");
   msgdiv.style.display = showHow;
   
   if (!error)
   {
      msgdiv.innerHTML = "Unknown Error";
      return rowNum;
   }

   if (error.status && error.statusText)
   {
      msgdiv.innerHTML = error.status + " " + error.statusText;
      return;
   }
   
   if (error.code && error.code === "NetworkingError")
   {
      if (error.hostname && error.hostname.startsWith("lambda."))
      {
         msgdiv.innerHTML = "Cannot Access Cloud Server. Check Internet Connection";
         return;
      }
   }      
   
   if (error.message)
   {
      msgdiv.innerHTML = error.message;
      return;
   } 

   if (error.response)
   {
      try
      {
         var obj = JSON.parse(error.response);
         msgdiv.innerHTML = obj.err;
         return;
      } 
      catch (e)
      {
         if (error.toString() === "[object XMLHttpRequest]")
         {
            msgdiv.innerHTML = "No reply recieved from head unit";
            return;
         }
         else if (error.toString() === "[object ProgressEvent]")
         {
            msgdiv.innerHTML = "In Progress - Please Wait";
            return;
         }
      }
   }
   
   msgdiv.innerHTML = error;
}

function showInfoMessage(msg, showHow)
{
   hideMessages();
   document.getElementById("niceMessageDiv").style.display=showHow;
   document.getElementById("niceMessageDiv").innerHTML = msg; 
}

function hideMessages()
{
   document.getElementById("errorMessageDiv").innerHTML = ""; 
   document.getElementById("errorMessageDiv").style.display="none";
   document.getElementById("niceMessageDiv").innerHTML = ""; 
   document.getElementById("niceMessageDiv").style.display="none";
}

function scrollToTop()
{
   document.documentElement.scrollTop = 0;
   document.body.scrollTop = document.documentElement.scrollTop = 0;
}
