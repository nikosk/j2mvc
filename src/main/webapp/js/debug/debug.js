/**
 * @author nikos
 */
function inspect(obj, maxLevels, level){
    var str = '', type, msg;
    if(level == null)  level = 0;
    if(maxLevels == null) maxLevels = 1;
    if (maxLevels < 1) {
        return '<font color="red">Error: Levels number must be > 0</font>';
    }
    if(obj == null){
        return '<font color="red">Error: Object <b>NULL</b></font>';
    }
    str += '<ul>';
    for(property in obj){
        try{
            type =  typeof(obj[property]);
            if (type != 'function') {
                str += '<li>(' + type + ') ' + property +
                    ((obj[property] == null) ? (': <b>null</b>') : (' : ' +
                    obj[property])) +
                    '</li>';
                if ((type == 'object') && (obj[property] != null) &&
                    (level + 1 < maxLevels))
                    str += inspect(obj[property], maxLevels, level + 1);
            }
        }
        catch(err)
        {
            // Is there some properties in obj we can't access? Print it red.
            if(typeof(err) == 'string') msg = err;
            else if(err.message)        msg = err.message;
            else if(err.description)    msg = err.description;
            else                        msg = 'Unknown';

            str += '<li><font color="red">(Error) ' + property + ': ' +
                msg +'</font></li>';
        }
    }

    // Close indent
    str += '</ul>';
    return str;
}