/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function checkAvaillability(){
  var textInputValue =  $('id_screen_name').value.trim();
  var span =  $('resultSpan');
  if(textInputValue != "" && textInputValue.length >= 3){
      span.innerHTML="<img alt='' src='images/admin/ajax-loader.gif'/>";
      var jsonRequest = new Request.JSON(
      {
          url: "/home/isAvaillable", 
          onComplete: function(screenName){
            if(screenName.isAvaillable =="true"){
                span.innerHTML="<img alt='' src='images/icons/famfam/accept.png'/>";
            }else{
                span.innerHTML="<img alt='' src='images/icons/famfam/cancel.png'/>";
            }
          },
          onFailure : function(){
            alert("Check Availlabilty of Screen Name error!");
          }
      }
    ).get({'screen_name': textInputValue});   
  }else{
      if($('resultSpan')!=null){
          $('resultSpan').innerHTML="";
      }
  }
}

window.addEvent('domready', function() {
    var span = new Element('span', {id: 'resultSpan',html:'&nbsp;'});
    var textInput =  $('id_screen_name');
    span.injectAfter(textInput);
    textInput.addEvent('change',checkAvaillability);
});