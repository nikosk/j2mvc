utils = {    
	add_field : function(){
		alert('1');
	}, 
    init: function(){		
      	$('add_field_b').addEvent('click', add_field);
		alert('1');
    }
};
window.addEvent('domready', utils.init.bind(utils));