window.addEvent('domready',function(){
    var tree;   
    tree = new MooTreeControl({
        div: 'menu',
        mode: 'folders',
        grid: true,
        theme : "/static/js/mootree.gif",
        onSelect: function(node, state) {
            if(node == this.root){
                this.root.toggle(true);
                this.selected = false;
            }else{
                if (state) window.location =  node.data.url;
            }
        }
    },{
        text: 'Category Root',
        open: false
    });
    if($('menu')){
        tree.adopt($('menu').getFirst());
    }
    var tree2 = new MooTreeControl({
        div: 'section_menu',
        mode: 'folders',
        grid: true,
        theme : "/static/js/mootree.gif",
        onSelect: function(node, state) {
            if(node == this.root){
                this.root.toggle(true);
                this.selected = false;
            }else{
                if (state) window.location =  node.data.url;
            }
        }
    },{
        text: 'Section Root',
        open: false
    });
    if($('section_menu')){
        tree2.adopt($('section_menu').getFirst());
    }
    tree.expand();
    tree2.expand();
});
