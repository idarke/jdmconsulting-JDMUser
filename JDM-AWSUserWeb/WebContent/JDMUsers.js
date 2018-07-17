var data;
var appdata = [];

(function()
{
   function loadPages()
   {
      setTitle();
      getApplications();
   }

   function setTitle()
   {
      document.title = "JDM Users";
   }

   loadPages();

})();

function getApplications()
{
   var xhr = new XMLHttpRequest();
   xhr.onload = function() 
   {
      if (xhr.readyState == XMLHttpRequest.DONE)
      {
          var responseText = JSON.parse(xhr.responseText);
          if (responseText.errorMessage)
          {
             showBlockErrorMessage(responseText.errorMessage);
             return;
          }
          appdata = JSON.parse(responseText.body);
          var selectElement = document.getElementById("createApp");
          addAppsToDropdown(selectElement, "");
          
          showAllUsers();
      }
   }; 
   xhr.onerror = function(e) 
   {
      showBlockErrorMessage("Error getting applications: " + xhr.statusText);
   }; 
   
   var postContents = new Object();
   postContents.handler = "APPS";
   postContents.action = "GETALLAPPS";
   var postString = JSON.stringify(postContents);
   xhr.open("POST", JDMUserRequest(), true);
   xhr.send(postString); 
}

function showAllUsers()
{
   var xhr = new XMLHttpRequest();
   xhr.onload = function() 
   {
      if (xhr.readyState == XMLHttpRequest.DONE)
      {
          var responseText = JSON.parse(xhr.responseText);
          if (responseText.errorMessage)
          {
             showBlockErrorMessage(responseText.errorMessage);
             return;
          }
          data = JSON.parse(responseText.body);
          
          populateTable(data);
      }
   }; 
   xhr.onerror = function(e) 
   {
      showBlockErrorMessage("Error getting users: " + xhr.statusText);
   }; 
   
   var postContents = new Object();
   postContents.handler = "USER";
   postContents.action = "GETALL";
   var postString = JSON.stringify(postContents);
   xhr.open("POST", JDMUserRequest(), true);
   xhr.send(postString); 
}

function populateTable(data)
{
   var table = document.getElementById("dataTable");
   var existingRows = table.rows.length;

   // Retain first row (header) and last row (create new)
   if (existingRows > 2)
   {
      existingRows -= 1;
      while (existingRows > 1)
      {
         existingRows -= 1;
         table.deleteRow(existingRows);
      }
   }

   var editButton = "<button class='lightBlueButton' id='edit~xxx' onclick='doButton(this)'>Edit</button>";
   var saveButton = "<button class='greenButton' id='save~xxx' onclick='doButton(this)'>Save</button>";      
   var delButton = "<button class='redButton' id='del~xxx' onclick='doButton(this)'>Delete</button>";
   var nameField = "<input type='text' class='tableInputText' id='username~xxx' />";
   var fullnameField = "<input type='text' class='tableInputText' id='fullname~xxx' />";      
   var passwordField = "<input type='text' class='tableInputText' id='password~xxx' />";
   var roleField = "<select id='role~xxx' ><option value='U'>User</option><option value='A'>Administrator</option><option value='D'>DRE</option></select>";
   var applicationField = "<select id='app~xxx' ></select>";      

   var rowNum = 1;
   data.forEach(function(element)
   {
      var thisEditButton = editButton.replace("~xxx", "~" + rowNum);
      var thisSaveButton = saveButton.replace("~xxx", "~" + rowNum);            
      var thisDelButton = delButton.replace("~xxx", "~" + rowNum);
      var thisNameField = nameField.replace("~xxx", "~" + rowNum);
      var thisFullNameField = fullnameField.replace("~xxx", "~" + rowNum);
      var thisPassField = passwordField.replace("~xxx", "~" + rowNum);
      var thisRoleField = roleField.replace("~xxx", "~" + rowNum);
      var thisAppField = applicationField.replace("~xxx", "~" + rowNum);

      var row = table.insertRow(rowNum);
      var cell1 = row.insertCell(0);
      cell1.className = "cellMiddle";
      cell1.innerHTML = thisNameField;
      document.getElementById("username~"+rowNum).value=element.username;
         
      var cell2 = row.insertCell(1);
      cell2.className = "cellMiddle";
      cell2.innerHTML = thisPassField;
      document.getElementById("password~"+rowNum).value=element.password.replace(/./g, '*');
      
      var cell3 = row.insertCell(2);
      cell3.className = "cellMiddle";
      cell3.innerHTML = thisFullNameField;
      document.getElementById("fullname~"+rowNum).value=element.fullname;
      
      var cell4 = row.insertCell(3);
      cell4.className = "cellMiddle";
      cell4.innerHTML = thisAppField;
      var selectElement = document.getElementById("app~"+rowNum);
      addAppsToDropdown(selectElement, element.application);
         
      var cell5 = row.insertCell(4);
      cell5.className = "cellMiddle";
      cell5.innerHTML = thisRoleField;
      if (element.role === "D")
      {
         document.getElementById("role~"+rowNum).selectedIndex=2;
      }
      else if (element.role === "A")
      {
         document.getElementById("role~"+rowNum).selectedIndex=1;
      }
      else
      {
         document.getElementById("role~"+rowNum).selectedIndex=0;
      }
         
      var cell6 = row.insertCell(5);
      cell6.className = "cellMiddle";
      cell6.innerHTML = "<span class='nowrapSpan'>" + thisEditButton + thisSaveButton + "</span>";
      document.getElementById("save~"+rowNum).style.display="none";

      var cell7 = row.insertCell(6);
      cell7.className = "cellEnd";
      cell7.innerHTML = thisDelButton;

      rowNum++;
   });
}

function addAppsToDropdown(selectElement, selectedApp)
{
   appdata.forEach(function(appelement)
   {
      var option = document.createElement("option");
      option.text = appelement.name;
      option.value = appelement.id;
      if (appelement.id === selectedApp)
      {
         option.selected = true;   
      }
      selectElement.add(option);
   });
}

function doButton(source)
{
   hideMessages();
   var tokens = source.id.split("~");
   var action = tokens[0];
   var rowNum = tokens[1];
   var index = rowNum-1;
   if (action === "edit")
   {
      launchEdit(rowNum, index);
   } 
   else if (action === "del")
   {
      showConfirm("Do you really want to delete '" + data[index].username + "'?", function()
      {
         deleteUser(index);
      });
   } 
   else if (action === "add")
   {
      var userBean = new Object();
      userBean.username = document.getElementById("createUsername").value;
      userBean.fullname = document.getElementById("createFullName").value;
      userBean.password = document.getElementById("createPassword").value;
      userBean.role = document.getElementById("createRole").value;
      userBean.id = "-1";
      var de = document.getElementById("createApp");
      userBean.application = de.options[de.selectedIndex].value;      
      saveUser(userBean);
   }
   else if (action === "save")
   {
      var userBean = new Object();
      userBean.username = document.getElementById("username~"+rowNum).value;
      userBean.password = document.getElementById("password~"+rowNum).value;
      userBean.fullname = document.getElementById("fullname~"+rowNum).value;
      userBean.role = document.getElementById("role~"+rowNum).value;
      userBean.id = data[index].id;
      var de = document.getElementById("app~"+rowNum);
      userBean.application = de.options[de.selectedIndex].value;
      saveUser(userBean);
   }
}

function launchEdit(rowNum, index)
{
   document.getElementById("password~"+rowNum).value= data[index].password;
   document.getElementById("edit~"+rowNum).style.display="none";
   document.getElementById("save~"+rowNum).style.display="inline";
}

function saveUser(userBean)
{
   if (!userBean.username)
   {
      showBlockErrorMessage("Please enter a username");
      return;
   }
   if (!userBean.password)
   {
      showBlockErrorMessage("Please enter a password");
      return;
   }   
   
   var xhr = new XMLHttpRequest();
   xhr.onload = function() 
   {
      if (xhr.readyState == XMLHttpRequest.DONE)
      {
          var responseText = JSON.parse(xhr.responseText);
          if (responseText.errorMessage)
          {
             showBlockErrorMessage(responseText.errorMessage);
             return;
          }
          location.reload(true);
      }
   }; 
   xhr.onerror = function(e) 
   {
      showBlockErrorMessage("Error saving user: " + xhr.statusText);
   }; 
   
   var postContents = new Object();
   postContents.handler = "USER";
   postContents.action = "SAVE";
   postContents.params = encodeURIComponent(JSON.stringify(userBean));
   var postString = JSON.stringify(postContents);
   xhr.open("POST", JDMUserRequest(), true);
   xhr.send(postString);   
}

function deleteUser(index)
{
   var xhr = new XMLHttpRequest();
   xhr.onload = function() 
   {
      if (xhr.readyState == XMLHttpRequest.DONE)
      {
          var responseText = JSON.parse(xhr.responseText);
          if (responseText.errorMessage)
          {
             showBlockErrorMessage(responseText.errorMessage);
             return;
          }
          location.reload(true);
      }
   }; 
   xhr.onerror = function(e) 
   {
      showBlockErrorMessage("Error deleting user: " + xhr.statusText);
   }; 
   
   var userBean = new Object();
   userBean.id = data[index].id;   
   
   var postContents = new Object();
   postContents.handler = "USER";
   postContents.action = "DELETE";
   postContents.params = encodeURIComponent(JSON.stringify(userBean));
   var postString = JSON.stringify(postContents);
   xhr.open("POST", JDMUserRequest(), true);
   xhr.send(postString); 
   
}

