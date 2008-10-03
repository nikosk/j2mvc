function selAll() {
    var messagesTable = $('messages');
    var checkboxes = messagesTable.getElements('input');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].checked = true;
    }
}

function selNone() {
    var messagesTable = $('messages');
    var checkboxes = messagesTable.getElements('input');
    for (var i = 0; i < checkboxes.length; i++) {
        checkboxes[i].checked = false;
    }
}

function getSelected() {
    var messagesTable = $('messages');
    var ids = "";
    var checkboxes = messagesTable.getElements('input');
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            ids += checkboxes[i].value + ",";
        }
    }
    if (ids.length > 0) {
        ids = ids.substr(0, ids.length - 1);
    }
    return ids;
}

function deleteMessages(){
    initAjaxMsgLog();
    var messageIds = getSelected();
    if (messageIds.length > 0) {
        var jsonRequest = new Request.JSON({
            url: "/messages/deleteMany", 
            onComplete: function(json){
                var ids = messageIds.split(",");
                for(var i=0; i<ids.length; i++) {
                    $('message-'+ids[i]).dispose();
                }
                $('ajaxMsgLog').innerHTML = json.message;
                $('ajaxMsgLog').set('class', json.style);
            },
            onFailure : function(){
                alert("Error!");
            }
        }).post({'messageIds': messageIds});        
    }
}

function markAsRead(){
    initAjaxMsgLog();
    var messageIds = getSelected();
    if (messageIds.length > 0) {
        var jsonRequest = new Request.JSON({
            url: "/messages/markAsRead", 
            onComplete: function(json){
                var ids = messageIds.split(",");
                $('ajaxMsgLog').innerHTML = json.message;
                $('ajaxMsgLog').set('class', json.style);
            },
            onFailure : function(){
                alert("Error!");
            }
        }).post({'messageIds': messageIds});
    }
}

function initAjaxMsgLog() {
    if ($('fhitLog') != null) {
        $('fhitLog').dispose();
    }
    $('ajaxMsgLog').innerHTML = '';
    $('ajaxMsgLog').set('class', null);
}

window.addEvent('domready', function() {
    var selAllLink = $('selAll');
    var selNoneLink = $('selNone');
    var deleteLink = $('delete');
    var markReadLink = $('markRead');
    if (selAllLink != null) {
        selAllLink.addEvent('click',selAll);
    }
    if (selNoneLink != null) {
        selNoneLink.addEvent('click',selNone);
    }
    if (deleteLink != null) {
        deleteLink.addEvent('click',deleteMessages);
    }
    if (markReadLink != null) {
        markReadLink.addEvent('click',markAsRead);
    }
});