/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function confirmDelete(){
    var toDelete = confirm("As a result of deleting the admin, the group is going to be deleted. Do you want to proceed?");
    if (toDelete){
        deleteGroup();
    }
}

function deleteGroup(){
    var groupId = document.getElementById('groupId').value;
    var membId = document.getElementById('membId').value;
    document.getElementById('delete_member_admin').href = "/groups/delete_member/"+groupId+"/"+membId ; 
}

window.addEvent('domready', function() {
    $('delete_member_admin').addEvent('click',confirmDelete);
});

