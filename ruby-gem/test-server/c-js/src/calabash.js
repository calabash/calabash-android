(function () {
    /** David Mark's isHostMethod function,
      * http://peter.michaux.ca/articles/feature-detection-state-of-the-art-browser-scripting
      * Modified to use strict equality
      */
    function isHostMethod (object, property)
    {
      var t = typeof object[property];
      return t==='function' ||
             (!!(t==='object' && object[property])) ||
             t==='unknown';
    }
    /*http://www.w3.org/TR/DOM-Level-2-Core/core.html*/
    var NODE_TYPES = {
        /*ELEMENT_NODE                   : */ 1 : 'ELEMENT_NODE',
        /*ATTRIBUTE_NODE                 : */ 2: 'ATTRIBUTE_NODE',
        /*TEXT_NODE                      : */ 3 : 'TEXT_NODE',
        /*DOCUMENT_NODE                  : */ 9 : 'DOCUMENT_NODE'
    };

    function computeRectForNode(object)
    {
        var res = {}, boundingBox;
        if (isHostMethod(object,'getBoundingClientRect'))
        {
           boundingBox = object.getBoundingClientRect();
           res['rect'] = boundingBox;
           res['rect'].center_x = boundingBox.left + Math.floor(boundingBox.width/2);
           res['rect'].center_y = boundingBox.top + Math.floor(boundingBox.height/2);
        }
        res.nodeType = NODE_TYPES[object.nodeType] || res.nodeType + ' (Unexpected)';
        res.nodeName = object.nodeName;
        res.id = object.id || '';
        res['class'] = object.className || '';
        if (object.href)
        {
            res.href = object.href;
        }
        if (object.hasOwnProperty('value'))
        {
            res.value = object.value || '';
        }
        res.html = object.outerHTML || '';
        res.textContent = object.textContent;
        return res;
    }
    function toJSON(object)
    {
        var res, i, N, spanEl, parentEl;
        if (typeof object==='undefined')
        {
            throw {message:'Calling toJSON with undefined'};
        }
        else if (object instanceof Text)
        {
            parentEl = object.parentElement;
            if (parentEl)
            {
                spanEl = document.createElement("calabash");
                spanEl.style.display = "inline";
                spanEl.innerHTML = object.textContent;
                parentEl.replaceChild(spanEl, object);
                res = computeRectForNode(spanEl);
                res.nodeType = NODE_TYPES[object.nodeType];
                delete res.nodeName;
                delete res.id;
                delete res['class'];

                parentEl.replaceChild(object,spanEl);
            }
            else
            {
                res = object;
            }


        }
        else if (object instanceof Node)
        {
            res = computeRectForNode(object);
        }
        else if (object instanceof NodeList ||
                 (typeof object=='object' && object &&
                  typeof object.length === 'number' &&
                  object.length > 0
                  && typeof object[0] !== 'undefined'))
        {
            res = [];
            for (i=0,N=object.length;i<N;i++)
            {
                res[i] = toJSON(object[i]);
            }
        }
        else
        {
            res = object;
        }
        return res;
    }

    function applyMethods(object, arguments) {
        var length = arguments.length;

        for(var i = 0; i < length; i++) {
            var argument = arguments[i];

            if (typeof argument === 'string') {
                argument = {method_name: argument, arguments: []}
            }

            var methodName = argument.method_name;
            var methodArguments = argument.arguments;

            if (typeof object[methodName] === 'undefined') {
                var type = Object.prototype.toString.call(object);

                object =
                    {
                        error: "No such method '" + methodName + "'",
                        methodName: methodName,
                        receiverString: object.constructor.name,
                        receiverClass: type
                    };

                break;
            } else {
                object = object[methodName].apply(object, methodArguments);
            }
        }
    }

    var exp = '%@'/* dynamic */,
        queryType = '%@' /* dynamic */,
        arguments = '%@' /* dynamic */,
        nodes = null,
        res = [],
        i,N;
    try
    {
        if (queryType==='xpath')
        {
            nodes = document.evaluate(exp, document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
            for (i=0,N=nodes.snapshotLength;i<N;i++)
            {
                res[i] = nodes.snapshotItem(i);
            }
        }
        else
        {
            res = document.querySelectorAll(exp);
        }
    }
    catch (e)
    {
       return JSON.stringify({error:'Exception while running query: '+exp, details:e.toString()})
    }

    if (arguments !== '%@') {
        var length = res.length;

        for (var i = 0; i < length; i++) {
            res[i] = applyMethods(res[i], arguments);
        }
    }

    return JSON.stringify(toJSON(res));
})();
