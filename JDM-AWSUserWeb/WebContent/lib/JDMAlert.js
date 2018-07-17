var saveAlertCallback;

function showAlert( messageOrMessageArray, alertCallback )
{
   saveAlertCallback = alertCallback;
   showDialog(messageOrMessageArray, true, false);
}

function showConfirm( messageOrMessageArray, yesCallback )
{
   saveAlertCallback = yesCallback;
   showDialog(messageOrMessageArray, false, true);
}

function showDialog( message, okButton, confirmButtons )
{
   document.getElementById("greyscreen").style.display = "inline";
   document.body.style.overflow = "hidden";
   var table = document.getElementById("alertTable");
   
   var existingRows = table.rows.length;
   while (table.rows.length > 0)
   {
      existingRows--;
      table.deleteRow(existingRows);
   }
   
   var rowNum = 0;
   var messages = [];
   if (message.constructor === Array)
   {
      messages = message;
   }
   else
   {
      messages.push(message);
   }
   
   var rowNum=0;
   for (; rowNum<messages.length; rowNum++)
   {
      var row = table.insertRow(rowNum);
      var cell = row.insertCell(0);
      cell.innerHTML = "<span class='nowrapSpan'>" + messages[rowNum] + "</span>";
   }
   
   row = table.insertRow(rowNum++);
   cell = row.insertCell(0);
   cell = row.insertCell(1);
   
   row = table.insertRow(rowNum++);
   cell = row.insertCell(0);
   cell = row.insertCell(1);
   cell.className = "menuRow";
   
   var buttonRow = "<span class='nowrapSpan'>";
   if (okButton)
   {
      buttonRow += "<button class='blueButton mediumButton' onclick='jdmDialogOk()'>OK</button>";
   }
   if (confirmButtons)
   {
      buttonRow += "<button class='blueButton mediumButton' onclick='jdmDialogOk()'>Yes</button>";
      buttonRow += "<button class='blueButton mediumButton' onclick='hideAlertDialog()'>No</button>";
   }  
   buttonRow += "</span>";
   cell.innerHTML = buttonRow;
   
   document.getElementById("alertdiv").style.display="block";
}

function jdmDialogOk()
{
   hideAlertDialog();
   if (saveAlertCallback)
   {
      saveAlertCallback();
   }
}

function hideAlertDialog()
{
   document.getElementById("alertdiv").style.display="none";
   document.getElementById("greyscreen").style.display = "none";
   document.body.style.overflow = "auto";
}
