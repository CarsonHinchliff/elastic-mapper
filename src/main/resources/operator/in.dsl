{
    "terms": {
        "#(params.field)": [
               #for(item : params.multipleValue)
                "#(item)"
                #if(!for.last)
                ,
                #end
               #end
        ]
    }
}