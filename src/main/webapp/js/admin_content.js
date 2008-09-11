function showCreateForm()
{
    document.getElementById("contentTable").style.display = "" ;
}

function hideCreateForm()
{
    document.getElementById("contentTable").style.display = "none" ;
}

function showCreateTemplateForm()
{
    document.getElementById("contentTable").style.display = "" ;
}

function hideCreateTemplateForm()
{
    document.getElementById("contentTable").style.display = "none" ;
}

function displayTempFields()
{
    if(document.getElementById("zipped").checked==true)
    {
        document.getElementById("temp_header").style.display = "none" ;    
        document.getElementById("temp_dropdown").style.display = "none" ;    
    }   
    else
    {
        document.getElementById("temp_header").style.display = "" ;
        document.getElementById("temp_dropdown").style.display = "" ;
    }
}

function showTempContent()
{
    document.getElementById('home1').style.display = "" ;
    document.getElementById('home2').style.display = "" ;
    document.getElementById('home3').style.display = "" ;
    document.getElementById('home4').style.display = "" ;
    document.getElementById('home5').style.display = "" ;
    
    document.getElementById('showHide_home').innerHTML = '<a href="javascript:hideTempContent()"><img alt="-" title="col" src="images/icons/col.gif"></a>' ;
}

function hideTempContent()
{
    document.getElementById('home1').style.display = "none" ;
    document.getElementById('home2').style.display = "none" ;
    document.getElementById('home3').style.display = "none" ;
    document.getElementById('home4').style.display = "none" ;
    document.getElementById('home5').style.display = "none" ;
    
    document.getElementById('showHide_home').innerHTML = '<a href="javascript:showTempContent()"><img alt="+" title="exp" src="images/icons/exp.gif"></a>' ;
}

function showEditTextArea()
{
    document.getElementById('editFile').style.display = "" ;
}

function hideEditTextArea()
{
    document.getElementById('editFile').style.display = "none" ; 
}

function deleteField(rowIns)
{
    var rowNum = parseInt(rowIns)-1 ;
    document.getElementById("field_"+(rowNum)).style.display = "none" ;
}

function addField(rowIns) 
{
    var rowNum = parseInt(rowIns)-1 ;
    var rowPlus = parseInt(rowIns)+1 ;
    document.getElementById("add_"+(rowNum)).innerHTML = "<a href='javascript:deleteField("+rowIns+")'>x</a>" ;
    var tbl =  document.getElementById('contentTable');
    
    var rowToCreate = parseInt(rowIns)+2 ;
    var row = tbl.insertRow(rowToCreate);
    row.style.borderBottom = "1px dotted #AAAAAA" ;
    row.id = 'field_'+ rowIns;
    var cell;
  
    cell = row.insertCell(0);
    cell.innerHTML = '<input type="text" id="name_'+rowIns+'" size="7"/>' ;
    
    cell = row.insertCell(1);
    cell.innerHTML = '<select name="type_'+rowIns+'" id="type_0" ><option value="" >--</option><option value="text" >text</option><option value="int" >int</option><option value="date" >date</option><option value="email" >email</option><option value="url" >url</option><option value="image" >image</option></select>';
    
    cell = row.insertCell(2);
    cell.innerHTML = '<input id="req_'+rowIns+'" type="checkbox" value="">';
    
    cell = row.insertCell(3);
    cell.id= 'empty_'+rowIns ;
    cell.innerHTML = 'Select Type First';
    
    cell = row.insertCell(4);
    cell.id= 'size_'+rowIns ;
    cell.innerHTML = 'Select Type First';
    
    cell = row.insertCell(5);
    cell.id= 'related' ;
    cell.innerHTML = "<select name='type' id='type_0' multiple style='height:40px;'><option value='text' >article</option><option value='int' >serial</option><option value='date'>episode</option><option value='email' >documentary</option></select>";    
    cell = row.insertCell(6);
    cell.id= 'add_'+ rowIns ;
    cell.innerHTML = '<a href="javascript:addField('+rowPlus+');">+</a>' ;
}
