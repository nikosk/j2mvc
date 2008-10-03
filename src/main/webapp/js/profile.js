/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var body;
var avatar;    
var avatar_cords;

function uploadAvatarResonse(response){
    $('avatar_iframe_holder').dispose();
    avatar.src=response;
    $('loadingDiv').dispose();
}

function getQuote(){
    
    var quote_txt = prompt("Set your Status");
    if(quote_txt!=null && quote_txt!=""){
      setQuote(quote_txt)
    }
}

function setQuote(quote){
      var jsonRequest = new Request.JSON(
      {
          url: "/profile/set_quote", 
          onComplete: function(response){
            $('quote').innerHTML = "is..."+response.quote_response;
            $('date_updated').innerHTML = response.date_updated;
          },
          onFailure : function(){
            alert("set");
          }
      }
    ).get({'quote_txt': quote.trim() });  //send var quote_text the current value 

}

function dimAvatar(){
    var div = new Element('div', {id: 'avatarDim',html:'click to change'});
    div.setStyles({
    position: 'absolute',
    top: avatar_cords.top,
    left: avatar_cords.left,
    width: avatar_cords.width,
    height: avatar_cords.height,
    opacity: 0.6,
    background: '#FFF000'
    });
    div.inject(body);
    if( $('avatar_iframe_holder')==null ){  
      div.addEvent('mouseout',destroyDim);
    }
    div.setStyle('cursor','pointer');
    div.addEvent('click',requestUploadForm);
}

function destroyDim(){
    $('avatarDim').dispose();
}

function close_upload_avatar(){
    $('avatar_iframe_holder').dispose();
}

function close_loading(){
    $('loadingDiv').dispose();
}

function loading(){
      var avatarCords = avatar.getCoordinates();
      var image = new Element('img', {alt: '',src:'/images/admin/ajax-loader.gif'});
      image.setStyles({padding:80});
      var div = new Element('div', {id: 'loadingDiv'});
      div.setStyles({
      position: 'absolute',
      background: '#EFEFEF',
      opacity: 0.6,
      top:avatarCords.top,
      left:avatarCords.left,
      width:avatarCords.width,
      height:avatarCords.height
      });
      image.inject(div);
      div.inject(body);
}

function requestUploadForm(){
    if( $('avatar_iframe_holder')==null ){        
      //TAKE THE COORDINATES OF AVATAR IMG (t,l,r,b,w,h)
      var avatarCords = avatar.getCoordinates();

      //CREATE DIV
      var div = new Element('div', {id: 'avatar_iframe_holder'});
      div.setStyles({
      border: '1px solid #000',
      position: 'absolute',
      background: '#ccc',
      top:avatarCords.bottom,
      left:avatarCords.left
      });
      //CREATE DIV
      var iframe = new Element('iframe', {id: 'avatar_iframe', src:'/profile/upload_avatar',frameborder:'0',height:'200'});
      //ADD IFRAME INTO DIV AND FINALLY DIV INTO BODY
      iframe.inject(div);
      div.inject(body);
    }
}

window.addEvent('domready', function() {
    body =  $(document.body);
    avatar = $('avatar');    
    avatar_cords = $('avatar').getCoordinates();
    avatar.addEvent('click',requestUploadForm);
    avatar.setStyle('cursor','pointer');
    //avatar.addEvent('mouseover',dimAvatar);
    $('quote').addEvent('click',getQuote);
});

