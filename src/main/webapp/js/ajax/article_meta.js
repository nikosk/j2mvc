function getMeta(url , title_div, lead_div){
    var jsonRequest = new Request.JSON(
    {
        url: "/comments/ajax_getLead/"+url, 
        method: 'get',
        onComplete: function(meta){
            var title = $(title_div);
            var lead = $(lead_div);
            if(title != null ){
                title.innerHTML = meta.title;
            }
            if(lead != null ){
                lead.innerHTML = meta.lead;
            }
        }
    }); 
    jsonRequest.send();
}