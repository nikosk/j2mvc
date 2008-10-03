var utils = {
    cloudUpdate : function(){
        var processed = new Array();
        var processed_titles = new Array();
        utils.all_urls.each(function(el,index,array){
            processed.include(el);// get unique urls
            processed_titles.include(utils.all_titles[index]); 
        });
        var count = new Array(processed.length);
        processed.each(function(el, index){ // get counts for each unique url
            for(i = 0 ; i < utils.all_urls.length; i++ ){
                if(utils.all_urls[i] == processed[index] ){
                    if(!count[index]){
                        count[index] = 0;
                    }
                    count[index] +=1;
                } 
            }
        });
        var sizes = new Array(processed.length);
        for(i = 0 ; i < processed.length ; i++){
            sizes[i] = ((100/utils.all_urls.length) * (count[i] * 0.03)) + 1; 
            var found = false;
            $$('#tagcloud a').each(function(el){
                if(processed[i] == el.href ){
                    found = true;
                    el.setStyle('font-size', sizes[processed.indexOf(el.href)]+ 'em');
                } 
            });     
            if(!found){
                var link = new Element('a',{
                    'href':processed[i],
                    'html':processed_titles[i]
                });
                link.setStyle('font-size' , sizes[processed.indexOf(processed[i])]+ 'em');
                $('tagcloud').grab(link);
            }
            $('ft').innerHTML = 'size = ((100/'+ utils.all_urls.length + ') * ('+ count[i] +' * 0.03)) + 0.7 = '+sizes[i]+"</br>" ;
        }       
        $('ft').innerHTML += inspect(processed) + inspect(count) + inspect(sizes);        
    },
    getNewComments : function(){
        var jsonRequest = new Request.JSON({
            url: "/conversations/aj_update_list/"+ utils.since,          
            onComplete: function(){
                jsn = this.response.json.comments;
                len = jsn.length;                
                utils.since = this.response.json.timestamp;                
                if(len > 0 ){
                    for(i = 0; i < len; i++){
                        $('bottom').innerHTML += jsn[i].title;                        
                        var comment           = new Element('div',{'class':'comment'});
                        var avatar            = new Element('div', {'class': 'avatar'});
                        var comment_right     = new Element('div', {'class': 'comment_right'});
                        var comment_info      = new Element('div', {'class': 'comment_info'});
                        var title_link        = new Element('a', {'href':'/comments/show_comments/'+ base64Encode(jsn[i].url) ,'html':jsn[i].title});
                        var username          = new Element('a', {'html':jsn[i].username , 'href':'/profile/wall/'+ jsn[i].username});
                        var comment_body      = new Element('div',{'class':'comment_body','html':jsn[i].comment });
                        var user_link         = new Element('a', {'href':'/profile/wall/'+ jsn[i].username}); 
                        var user_img          = new Element('img', {'src':jsn[i].thumbnail});                         
                        user_link.grab(user_img);
                        avatar.grab(user_link);                        
                        comment_info.grab(title_link);
                        comment_info.grab(new Element("span",{'html':' | Εγραψε ο/η : '}));  
                        comment_info.grab(username);                         
                        comment_right.grab(comment_info);
                        comment_right.grab(comment_body);                        
                        comment.grab(avatar);
                        comment.grab(comment_right);                        
                        comment.fade('hide');
                        $('comment_list').grab(comment,"top");
                        comment.fade('in');
                        var last = $('comment_list').getLast('div.comment');
                        last.fade('out');
                        last.dispose();
                        var new_url = root_url + 'comments/show_comments/'+ base64Encode(jsn[i].url);
                        var new_title = jsn[i].title;
                        utils.all_titles.push(new_title);
                        utils.all_urls.push(new_url);
                    }                    
                    $('ft').innerHTML = inspect(utils.all_urls) + inspect(utils.all_titles);
                    utils.cloudUpdate();
                }                          
            }
        }).get();        
    }, 
    getOldComments : function(){
        $$('.comment_info').each(function(el,index,array){
            var link = el.getFirst(); // first A tag is always article link.
            utils.all_urls.push(link.href);
            utils.all_titles.push(link.innerHTML);
        });
    },
    init : function(){        
        utils.since = new Date().getTime(); // begin requests from now
        utils.count = new Array(); // array to hold counts        
        utils.all_urls = new Array(); // list of all urls 
        utils.all_titles = new Array(); // list of all titles
        this.getOldComments();
        $('ft').innerHTML = inspect(utils.all_urls) + inspect(utils.all_titles);
        var c = this.getNewComments.periodical(5000, this);      
        //$('bottom').addEvent('click', utils.getNewComments);
    }
}
window.addEvent('domready',utils.init.bind(utils));
