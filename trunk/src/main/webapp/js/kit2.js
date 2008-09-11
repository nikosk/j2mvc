kit = { 
    /**
     * Prototypes for new fields in structure manager
     */
    form_fields : new Hash({}),
    show_help : function(){
        var input_limit = '<input type="text" id="" />';
        var input_image = "<select name='type' id='type_0' multiple style='height:40px;'><option value='text' >bmp</option><option value='int' >png</option><option value='date'>gif</option><option value='email' >jpg</option></select>";
        var input_date = '<select name="type" id=""><option value="1">dd-MM-yyy</option><option value="2">dd-MM-yyy hh:mm</option><option value="3" >hh:mm</option></select>';
        
        //alert(this.value);
        if(this.value == "date")
        {
            $('empty_0').innerHTML = input_date;
            $('size_0').innerHTML = "--" ;
        }    
        else if(this.value == "url" || this.value == "email" || this.value == "text")
        {
            $('empty_0').innerHTML = input_limit;
            $('size_0').innerHTML = "--" ;
        }
        else if(this.value == "image")
        {
            $('empty_0').innerHTML = input_image;
            $('size_0').innerHTML = '<input type="text" id="height" size="2"/> * <input type="text" id="width" size="2"/>' ;
        }        
        else if(this.value == "int")
        {
            $('empty_0').innerHTML = "--";
            $('size_0').innerHTML = "--" ;
        }  
        else if(this.value == "")
        {
            $('empty_0').innerHTML = "Select Type First";
            $('size_0').innerHTML = "--" ;
        }  
        
    },
    init: function(){
        form_fields.set('', '');
    }
};
window.addEvent('domready', kit.init.bind(kit));
