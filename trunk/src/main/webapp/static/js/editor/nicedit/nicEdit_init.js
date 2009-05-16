bkLib.onDomLoaded(function() {
    var tarea = document.getElementById("id_content");
    var conf = {
        fullPanel:false,
        iconsPath:'/static/js/editor/nicedit/nicEditorIcons.gif',
        buttonList:['removeformat','bold','italic','underline','ol','ul','image','link','unlink','fontFormat','xhtml','subscript','superscript','strikethrough','indent','outdent','hr' ]
    };
    nicEditors.editors.push(new nicEditor(conf).panelInstance(tarea));
});
